
package OKcompress;

import OKcompress.domain.IntegerQueue;
import OKcompress.utils.BitReader;
import OKcompress.utils.ByteWriter;

/**
 *
 * @author ogkuisma
 */
public class DeflateLite {
    
    private Huffman huf;
    private LZSS lzss;
    static int[] deflateLengthCodes = { 
        4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
    };
    
    static int[] deflateOffsetCodes = { 
        2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 
        257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097
    };
    
    static int[] deflateOffsetMins = { 
        1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 
        129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073
    };
    
    static int[] deflateOffsetExtraBits = { 
        0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 
        6, 7, 7, 8, 8, 9, 9, 10, 10
    };
    
    public DeflateLite() {
        lzss = new LZSS(12, 4);
        huf = new Huffman();
    }
    
    public byte[] encode(byte[] input) {
        ByteWriter writer = new ByteWriter();
        byte[] lzssEncoded = lzss.encodeUsingQueues(input).getBytes();
        
        int[] codeLengths = huf.getDeflateCodeLengths(lzssEncoded);
        int[] codes = huf.createCodeArray(codeLengths);
        huf.createFileHeader(codeLengths, writer);
        BitReader reader = new BitReader(lzssEncoded);
        int written = 0;
        while (true) {
            int code = 0;
            int codeLength = 0;
            int nextBit = reader.readBit();
            if (nextBit == -1) {
                break;
            }
            if (nextBit == 1) {
                int offset = reader.readInt(12);
                int length = reader.readInt(4);
                for (int i = 0; i < deflateLengthCodes.length; i++) {
                    if (length < deflateLengthCodes[i]) {
                        code = codes[257 + i];
                        codeLength = codeLengths[257 + i];
                        writeCode(writer, code, codeLength);
                        written++;
                        break;
                    }
                }
                for (int i = 0; i < deflateOffsetCodes.length; i++) {
                    if (offset < deflateOffsetCodes[i]) {
                        code = codes[i];
                        codeLength = codeLengths[i];
                        writeCode(writer, code, codeLength);
                        if (offset > 4) {
                            int extraBits = offset - deflateOffsetCodes[i-1];
                            writeCode(writer, extraBits, deflateOffsetExtraBits[i]);
                        }
                        written++;
                        break;
                    }
                }
            } else { // left child
                int nextByte = reader.readByte();
                if (nextByte > 255) {
                    break;
                }
                code = codes[nextByte];
                codeLength = codeLengths[nextByte];
                writeCode(writer, code, codeLength);
                written++;
            }
        }

        writer.close();
        return writer.getBytes().getBytes();
    }
    
    public byte[] decode(byte[] input) {
        IntegerQueue output = new IntegerQueue(3 * input.length);
        BitReader reader = new BitReader(input);
        int[] codeLengths = new int[270];
        for (int i = 0; i < 270; i++) {
            codeLengths[i] = reader.readInt(5); // read code lengths from file header
        }
        int[] codes = huf.createCodeArray(codeLengths);
        int[] huffmanTree = huf.recreateHuffmanTree(codes, codeLengths);
        int treeIndex = 1;
        boolean offsetCode = false;
        int length = 0;
        while (true) {
            int nextBit = reader.readBit();
            if (nextBit == -1) {
                break;
            }
            if (nextBit == 1) {
                treeIndex = 2 * treeIndex + 1;
            } else {
                treeIndex = 2 * treeIndex;
            }
            if (huffmanTree[treeIndex] > 0) {
                if (!offsetCode & (reader.index == input.length)) {
                    System.out.println("moi");
                }
                int nextByte = huffmanTree[treeIndex] - 1;
                if (offsetCode) {
                    try {    
                        int offset = (deflateOffsetMins[nextByte] + reader.readInt(deflateOffsetExtraBits[nextByte]));
                        for (int j = 0; j < length; j++) { // add {length} bytes starting from index {size - offset} 
                            output.add(output.get(output.size() - offset));
                        }
                    } catch (ArrayIndexOutOfBoundsException x) {
                        System.out.println(x.getMessage());
                        break;
                    }
                    offsetCode = false;
                } else {
                    if (nextByte > 256) {
                        length = deflateLengthCodes[nextByte - 257] - 1;
                        offsetCode = true;
                    } else {
                        output.add(nextByte);
                    }     
                }
                treeIndex = 1;
            }
        }
        return output.getBytes();
    }
    
    private void writeCode(ByteWriter writer, int code, int codeLength) {
        int highestBit = (int) Math.pow(2, (codeLength-1));
        for (int j = 0; j < codeLength; j++) { 
            int a = code & highestBit;
            if (a == highestBit) {
                writer.writeBit((byte) 1);
            } else {
                writer.writeBit((byte) 0);
            }
            code = code << 1;               
        }
    }
}
