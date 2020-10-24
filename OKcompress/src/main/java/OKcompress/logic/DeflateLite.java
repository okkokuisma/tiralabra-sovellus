
package OKcompress.logic;

import OKcompress.domain.IntegerHashMap;
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
        byte[] lzssEncoded = lzss.encodeUsingQueues(input);        
        int[] codeLengths = huf.getDeflateCodeLengths(lzssEncoded);
        int[] codes = huf.createCodeArray(codeLengths);
        writer.writeBit((byte) 0);
        writer.writeBit((byte) 1);
        huf.createFileHeader(codeLengths, writer);
        BitReader reader = new BitReader(lzssEncoded);
        reader.readInt(2);
        while (true) {
            int nextBit = reader.readBit();
            if (nextBit == -1) {
                break;
            }
            if (nextBit == 1) {
                int offset = reader.readInt(12);
                int length = reader.readInt(4);
                for (int i = 0; i < deflateLengthCodes.length; i++) {
                    if (length < deflateLengthCodes[i]) {
                        writeCode(writer, codes[257 + i], codeLengths[257 + i]);
                        break;
                    }
                }
                for (int i = 0; i < deflateOffsetCodes.length; i++) {
                    if (offset < deflateOffsetCodes[i]) {
                        writeCode(writer, codes[i], codeLengths[i]);
                        if (offset > 4) {
                            int extraBits = offset - deflateOffsetCodes[i-1];
                            writeCode(writer, extraBits, deflateOffsetExtraBits[i]);
                        }
                        break;
                    }
                }
            } else {
                int nextByte = reader.readByte();
                if (nextByte > 255) {
                    break;
                }
                writeCode(writer, codes[nextByte], codeLengths[nextByte]);
            }
        }
        writeCode(writer, codes[256], codeLengths[256]);
        writer.close();
        return writer.getBytes();
    }
    
    public byte[] decode(byte[] input) {
        IntegerQueue output = new IntegerQueue(3 * input.length);
        BitReader reader = new BitReader(input);
        reader.readInt(2);
        int[] codeLengths = new int[270];
        for (int i = 0; i < 270; i++) {
            codeLengths[i] = reader.readInt(5); // read code lengths from file header
        }
        int[] codes = huf.createCodeArray(codeLengths);
        IntegerHashMap codeh = new IntegerHashMap();
        IntegerHashMap codel = new IntegerHashMap();
        for (int i = 0; i < codes.length; i++) {
            if (codeLengths[i] != 0) {
                codeh.put(codes[i], i);
                codel.put(codes[i], codeLengths[i]);               
            }
        }

        int code = 0;
        int codeLength = 0;
        boolean offsetCode = false;
        int length = 0;
        while (true) {
            code = code << 1;
            codeLength++;
            int nextBit = reader.readBit();
            if (nextBit == -1) {
                break;
            }
            if (nextBit == 1) {
                code++;
            }
            if (codeh.containsKey(code)) {
                if (codel.get(code) == codeLength) {
                    int nextByte = codeh.get(code);
                    if (nextByte == 256) {
                        break;
                    }
                    if (offsetCode) {
                        int offset = deflateOffsetMins[nextByte] + reader.readInt(deflateOffsetExtraBits[nextByte]);
                        for (int j = 0; j < length; j++) { // add {length} bytes starting from index {size - offset} 
                            output.add(output.get(output.size() - offset));
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
                    code = 0;
                    codeLength = 0;
                }
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
