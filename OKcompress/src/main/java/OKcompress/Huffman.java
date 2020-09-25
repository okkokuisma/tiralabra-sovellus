
package OKcompress;

import OKcompress.domain.ByteList;
import OKcompress.domain.HuffmanHeap;
import OKcompress.domain.HuffmanNode;
import OKcompress.utils.BitReader;
import OKcompress.utils.ByteWriter;

/**
 *
 * Huffman coding 
 */
public class Huffman {
    
    public ByteList encode(byte[] input) {
        HuffmanNode huffmanTreeRoot = createHuffmanHeap(input);
        ByteWriter writer = new ByteWriter();
        createFileHeader(huffmanTreeRoot, writer); // store the Huffman tree in the beginning of the output
//        String[] codes = new String[256];
//        createCodeArray(huffmanTreeRoot, "", codes); // create an array of the huffman codes
        
//        for (int i = 0; i < input.length; i++) {
//            String binaryString = codes[0xFF & input[i]];
//            for (int j = 0; j < binaryString.length(); j++) {
//                if (binaryString.charAt(j) == 48) {
//                    writer.writeBit((byte) 0);
//                } else {
//                    writer.writeBit((byte) 1);
//                }
//            }
//        }
        int[] codeLengths = new int[15];
        createCodeLengthArray(huffmanTreeRoot, codeLengths, 0);
        int [] minimumCodeNumericalValues = createMinimumNumericalValueArray(codeLengths);
        int[] codes = new int[256];
        createCodeArray(huffmanTreeRoot, codes, minimumCodeNumericalValues);
        for (int i = 0; i < input.length; i++) {
            int code = codes[0xFF & input[i]];
            if (code == 0) {
                writer.writeBit((byte) 0);
                writer.writeBit((byte) 0);
            } else {
                int highestBit = Integer.highestOneBit(code);
                int codeLength = (int)(Math.log(highestBit) / Math.log(2)) + 1;
                for (int j = 0; j < codeLength; j++) { 
                    int a = code & highestBit;
                    if (a == highestBit) { // check whether the first bit is 1 or 0
                        writer.writeBit((byte) 1);
                    } else {
                        writer.writeBit((byte) 0);
                    }
                    code = code << 1;
                }               
            }
        }
        return writer.getByteArray();
    }

    
    private HuffmanNode createHuffmanHeap(byte[] input) {
        HuffmanHeap huffmanTree = new HuffmanHeap();
        
        int[] occurances = new int[256];
        for (int i = 0; i < input.length; i++) { // check how many occurances of each individual byte there are in the input array
            int index = 0xFF & input[i]; // get the positive index for byte
            occurances[index]++;
        }
        int index = 0xFF & (byte) 254; // add an ending byte for decoding
        occurances[index]++;
        
        // create a node of each individual byte in the input array where the number of occurances is the weight of the created node
        for (int i = 0; i < occurances.length; i++) { 
            if (occurances[i] != 0) {
                HuffmanNode node = new HuffmanNode((byte) i, occurances[i]);
                huffmanTree.add(node);
            }
        }
        while (huffmanTree.getLast() != 1) { // create a tree which determines the huffman code for each individual byte
            HuffmanNode node = new HuffmanNode();
            node.leftChild = huffmanTree.poll();
            node.rightChild = huffmanTree.poll();
            node.weigth = node.leftChild.weigth + node.rightChild.weigth;
            huffmanTree.add(node);
        }  
        return huffmanTree.poll();
    }
    
//    private void createCodeArray(HuffmanNode node, String binaryString, String[] codes) {
//        if (node == null) {
//            return;
//        }
//        if (node.leftChild == null & node.rightChild == null) {
//            codes[0xFF & node.byteValue] = binaryString;
//        } else {
//            binaryString = binaryString + "0";
//            createCodeArray(node.leftChild, binaryString, codes);
//            binaryString = binaryString.substring(0, binaryString.length() - 1) + "1"; // replace the last 0 with 1
//            createCodeArray(node.rightChild, binaryString, codes);
//        }
//    }
    
    private void createCodeLengthArray(HuffmanNode node, int[] codeLengths, int length) {
        if (node == null) {
            return;
        }
        if (node.leftChild == null) {
            codeLengths[length]++;
            node.codeLength = length;
        } else {
            length++;
            createCodeLengthArray(node.leftChild, codeLengths, length);
            createCodeLengthArray(node.rightChild, codeLengths, length);
        }
    }
    
    private int[] createMinimumNumericalValueArray(int[] codeLengths) {
        int[] numericalValues = new int[codeLengths.length];
        int code = 0;
        for (int i = 1; i < 15; i++) {
            code = (code + codeLengths[i - 1]) << 1;
            numericalValues[i] = code;
        }
        return numericalValues;
    }
    
    private void createCodeArray(HuffmanNode node, int[] codes, int[] numericalValues) {
        if (node == null) {
            return;
        }
        if (node.leftChild == null) {
            codes[0xFF & node.byteValue] = numericalValues[node.codeLength];
            numericalValues[node.codeLength]++;
        } else {
            createCodeArray(node.leftChild, codes, numericalValues);
            createCodeArray(node.rightChild, codes, numericalValues);
        }
    }
    
    private void createFileHeader(HuffmanNode node, ByteWriter writer) {
        if (node == null) {
            return;
        }
        createFileHeader(node.leftChild, writer);
        createFileHeader(node.rightChild, writer);
        if (node.leftChild == null) { // a leaf node
            writer.writeBit((byte) 1); // zero but indicates a leaf node
            writer.addByte(node.byteValue);
        } else {
            writer.writeBit((byte) 0);
        }
    }
}
