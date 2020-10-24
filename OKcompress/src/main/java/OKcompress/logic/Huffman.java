
package OKcompress.logic;

import OKcompress.domain.IntegerQueue;
import OKcompress.domain.HuffmanHeap;
import OKcompress.domain.HuffmanNode;
import OKcompress.domain.IntegerHashMap;
import OKcompress.utils.BitReader;
import OKcompress.utils.ByteWriter;

/**
 * Data compression and decompression using Huffman coding.
 */
public class Huffman {
    static int[] deflateLengthCodes = { 
        4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
    };
    
    static int[] deflateOffsetCodes = { 
        2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 
        257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097
    };
    
    public byte[] encode(byte[] input) {
        ByteWriter writer = new ByteWriter();
        int[] codeLengths = getCodeLengthsUsingHeap(input);
        int[] codes = createCodeArray(codeLengths);
        writer.writeBit((byte) 1);
        writer.writeBit((byte) 0);
        createFileHeader(codeLengths, writer);
        for (int i = 0; i < input.length; i++) {
            int code = codes[0xFF & input[i]];
            int codeLength = codeLengths[0xFF & input[i]];
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
        int code = codes[256];
        int codeLength = codeLengths[256];
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
        writer.close();
        return writer.getBytes();
    }
    
    public byte[] decode(byte[] input) {
        IntegerQueue output = new IntegerQueue(2 * input.length);
        BitReader reader = new BitReader(input);
        reader.readInt(2);
        int[] codeLengths = new int[257];
        for (int i = 0; i < 257; i++) {
            codeLengths[i] = reader.readInt(5); // read code lengths from file header
        }
        int[] codes = createCodeArray(codeLengths);
        IntegerHashMap codeh = new IntegerHashMap();
        IntegerHashMap codel = new IntegerHashMap();
        for (int i = 0; i < codes.length; i++) {
            if (codeLengths[i] != 0) {
                codeh.put(codes[i], i);
                codel.put(codes[i], codeLengths[i]);               
            }
        }
        int codeLength = 0;
        int code = 0;
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
                    output.add(nextByte);
                    code = 0;
                    codeLength = 0;
                }
            }
        }
        return output.getBytes();
    }
    
    /**
    * Creates a Huffman tree using a HuffmanHeap to calculate the code length for each 
    * byte value that occurs in the given input.
    *
    * @param   input    The original, uncompressed data
    *
    * @return The code length of each byte value [0-255] (0 for byte values not found in input)
    */
    private int[] getCodeLengthsUsingHeap(byte[] input) {
        HuffmanHeap huffmanTree = new HuffmanHeap();
        
        int[] occurrences = createByteOccurrenceArray(input);
        
        // create a node of each individual byte in the input array where the number of occurances is the weight of the created node
        for (int i = 0; i < occurrences.length; i++) { 
            if (occurrences[i] != 0) {
                HuffmanNode node = new HuffmanNode(i, occurrences[i]);
                huffmanTree.add(node);
            }
        }
        while (huffmanTree.getLast() != 1) { // create a tree which determines the code length for each byte value
            HuffmanNode node = new HuffmanNode();
            node.leftChild = huffmanTree.poll();
            node.rightChild = huffmanTree.poll();
            node.weight = node.leftChild.weight + node.rightChild.weight;
            huffmanTree.add(node);
        }  
        
        int[] codeLengths = new int[257];
        createCodeLengthArray(huffmanTree.poll(), codeLengths, 0); 
        return codeLengths;
    }
    
    /**
    * Recursive method that checks each node's code length (depth from root node in Huffman tree).
    *
    * @param   node    The root of a Huffman tree
    * @param   codeLengths    Array in which code length values are stored 
    * @param   length   The depth of the starting node (0 if starting from root)
    *
    * @return The code length of each byte value in the Huffman tree
    */
    private void createCodeLengthArray(HuffmanNode node, int[] codeLengths, int length) {
        if (node == null) {
            return;
        }
        if (node.leftChild == null) {
            codeLengths[node.byteValue] = length;
        } else {
            length++;
            createCodeLengthArray(node.leftChild, codeLengths, length);
            createCodeLengthArray(node.rightChild, codeLengths, length);
        }
    }
    
    /**
    * Calculates integer values of the final Huffman codes for each byte value in the input data using the code lengths
    * calculated before.
    *
    * @param   codeLengths    Array of code lengths for each byte value [0-255]
    *
    * @return The Huffman codes for each byte value [0-255] as integers. The actual bits of the code
    * are determined based on the code lengths.
    */
    public int[] createCodeArray(int[] codeLengths) {
        int[] bitLengths = new int[31];
        for (int i = 0; i < codeLengths.length; i++) {
            if (codeLengths[i] > 0) {
                bitLengths[codeLengths[i]]++; // how many instances of each code length [i] there are
            }
        }
        int[] minimumValues = createMinimumNumericalCodeValueArray(bitLengths);
        int[] codes = new int[codeLengths.length];
        for (int i = 0; i < codeLengths.length; i++) {
            if (codeLengths[i] > 0) {
                // code is created by incrementing the base value of each code length for each instance of that particular code length
                // that way the symbol order remains lexicographical between symbols with the same code length
                codes[i] = minimumValues[codeLengths[i]]++;
            }
        }
        return codes;
    }
    
    /**
    * Calculates how many times each byte value [0-255] occurs in the given input data.
    *
    * @param   input    The original, uncompressed data
    *
    * @return Array of number of occurrences of each byte value [0-255] in the input data.
    */
    private int[] createByteOccurrenceArray(byte[] input) {
        int[] occurrences = new int[257];
        for (int i = 0; i < input.length; i++) { // check how many occurances of each individual byte there are in the input array
            int index = 0xFF & input[i]; // get the positive index for byte
            occurrences[index]++;
        }
 
        occurrences[256]++; // add an ending byte for decoding
        return occurrences;
    }
    
    /**
    * Calculates the numerical base value of Huffman codes for each code length. This base value is shared with every Huffman code 
    * of certain length.
    *
    * @param   codeLengthOccurances    Array of number of occurrences of each code length [0-20]
    *
    * @return Numerical base value of Huffman codes for each code length.
    */
    private int[] createMinimumNumericalCodeValueArray(int[] codeLengthOccurances) {
        int[] numericalValues = new int[codeLengthOccurances.length];
        int code = 0;
        for (int i = 1; i < codeLengthOccurances.length; i++) {
            code = (code + codeLengthOccurances[i - 1]) << 1;
            numericalValues[i] = code;
        }
        return numericalValues;
    }
    
    public void createFileHeader(int[] codeLengths, ByteWriter writer) {
        for (int i = 0; i < codeLengths.length; i++) { // store the code length of each symbol for decoding
            int codeLength = codeLengths[i];
            for (int j = 0; j < 5; j++) { 
                int a = codeLength & 16; // code lengths into 5 bits
                if (a == 16) {
                    writer.writeBit((byte) 1);
                } else {
                    writer.writeBit((byte) 0);
                }
                codeLength = codeLength << 1;
            }
        }
    }
    
    private int[] createDeflateCompatibleOccurrenceArray(byte[] input) {
        int[] occurrences = new int[270];
        BitReader reader = new BitReader(input);
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
                        occurrences[257 + i]++;
                        break;
                    }
                }
                for (int i = 0; i < deflateOffsetCodes.length; i++) {
                    if (offset < deflateOffsetCodes[i]) {
                        occurrences[i]++;
                        break;
                    }
                }
            } else { // left child
                int nextByte = reader.readByte();
                if (nextByte > 255) {
                    break;
                }
                occurrences[nextByte]++;
            }
        }
        occurrences[256]++;
        return occurrences;
    }
    
    public int[] getDeflateCodeLengths(byte[] input) {
        HuffmanHeap huffmanTree = new HuffmanHeap();
        
        int[] occurrences = createDeflateCompatibleOccurrenceArray(input);
        
        // create a node of each individual byte in the input array where the number of occurances is the weight of the created node
        for (int i = 0; i < occurrences.length; i++) { 
            if (occurrences[i] != 0) {
                HuffmanNode node = new HuffmanNode(i, occurrences[i]);
                huffmanTree.add(node);
            }
        }
        while (huffmanTree.getLast() != 1) { // create a tree which determines the code length for each byte value
            HuffmanNode node = new HuffmanNode();
            node.leftChild = huffmanTree.poll();
            node.rightChild = huffmanTree.poll();
            node.weight = node.leftChild.weight + node.rightChild.weight;
            huffmanTree.add(node);
        }  
        
        int[] codeLengths = new int[occurrences.length];
        createCodeLengthArray(huffmanTree.poll(), codeLengths, 0); 
        return codeLengths;
    }
}
