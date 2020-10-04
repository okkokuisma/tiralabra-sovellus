
package OKcompress;

import OKcompress.domain.IntegerQueue;
import OKcompress.domain.HuffmanHeap;
import OKcompress.domain.HuffmanNode;
import OKcompress.utils.BitReader;
import OKcompress.utils.ByteWriter;

/**
 * Data compression and decompression using Huffman coding.
 */
public class Huffman {
    
    public IntegerQueue encode(byte[] input) {
        ByteWriter writer = new ByteWriter();
        int[] codeLengths = getCodeLengthsUsingHeap(input);
        int[] codes = createCodeArray(codeLengths);
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
        int code = codes[254];
        int codeLength = codeLengths[254];
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
        return writer.getBytes();
    }
    
    public IntegerQueue decode(byte[] input) {
        IntegerQueue output = new IntegerQueue();
        BitReader reader = new BitReader(input);
        int[] codeLengths = new int[256];
        for (int i = 0; i < 256; i++) {
            codeLengths[i] = reader.readInt(5); // read code lengths from file header
        }
        int[] codes = createCodeArray(codeLengths);
        int[] huffmanTree = recreateHuffmanTree(codes, codeLengths);
        int treeIndex = 1;
        while (true) {
            int nextBit = reader.readBit();
            if (nextBit == -1) {
                break;
            }
            if (nextBit == 1) { // right child
                treeIndex = 2 * treeIndex + 1;
            } else { // left child
                treeIndex = 2 * treeIndex;
            }
            if (huffmanTree[treeIndex] > 0) {
                int nextByte = (huffmanTree[treeIndex] - 1);
                if (nextByte == 254) {
                    break;
                }
                output.add((huffmanTree[treeIndex] - 1));
                treeIndex = 1;
            }
        }
        return output;
    }
    
    /**
//    * Calculates the code length for each byte value that occurs in the given input.
//    *
//    * @param   input    The original, uncompressed data
//    *
//    * @return The code length of each byte value [0-255] (0 for byte values not found in input)
//    */
//    private int[] getCodeLengths(byte[] input) {
//        int[] occurrences = createByteOccurrenceArray(input);
//        
//        int[] nodes = new int[256];
//        int[] nodeCounts = new int[256];
//        int nodeCounter = 0;
//        
//        for (int i = 0; i < occurrences.length; i++) { 
//            if (occurrences[i] != 0) {
//                nodes[nodeCounter] = i + 1; // add a node pointer for each symbol that occured in the input
//                nodeCounts[nodeCounter] = occurrences[i]; // weigth for each node for sorting the nodes
//                nodeCounter++;
//            }
//        }
//        
//        int[] codeLengths = new int[256];
//        int[] pointerArray = new int[256]; // array for pointers to other nodes in the same tree
//        
//        while (nodeCounter > 1) {
//            // sorting nodes by occurances (nodes with most occurances to the start)
//            for (int i = 0; i < nodeCounter; i++) {
//                if (nodeCounts[i] < nodeCounts[i + 1]) {
//                    int swap = nodes[i];
//                    nodes[i] = nodes[i + 1];
//                    nodes[i + 1] = swap;
//                    swap = nodeCounts[i];
//                    nodeCounts[i] = nodeCounts[i + 1];
//                    nodeCounts[i + 1] = swap;
//                    if (i > 0) {
//                        i = i - 2;
//                    }
//                }
//            }
//            
//            // increment bit lengths of all nodes in the same tree (created when combining the last two nodes)
//            int leftNode = nodes[nodeCounter - 2];
//            int rightNode = nodes[nodeCounter - 1]; // the next pointer to be added
//            while (leftNode > 0) {
//                codeLengths[leftNode - 1]++;
//                int pointerNode = pointerArray[leftNode - 1];
//                if (pointerNode == 0 && rightNode > 0) {
//                    pointerArray[leftNode - 1] = rightNode;
//                    pointerNode = rightNode;
//                    rightNode = 0;
//                }
//                leftNode = pointerNode;
//            }
//            
//            nodeCounts[nodeCounter - 2] += nodeCounts[nodeCounter - 1]; // combine last two nodes
//            nodeCounter--;
//        }
//        return codeLengths;
//    }
    
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
                HuffmanNode node = new HuffmanNode((byte) i, occurrences[i]);
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
        
        int[] codeLengths = new int[256];
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
            codeLengths[0xFF & node.byteValue] = length;
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
    private int[] createCodeArray(int[] codeLengths) {
        int[] bitLengths = new int[20];
        for (int i = 0; i < codeLengths.length; i++) {
            if (codeLengths[i] > 0) {
                bitLengths[codeLengths[i]]++; // how many instances of each code length [i] there are
            }
        }
        int[] minimumValues = createMinimumNumericalCodeValueArray(bitLengths);
        int[] codes = new int[256];
        for (int i = 0; i < 256; i++) {
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
        int[] occurrences = new int[256];
        for (int i = 0; i < input.length; i++) { // check how many occurances of each individual byte there are in the input array
            int index = 0xFF & input[i]; // get the positive index for byte
            occurrences[index]++;
        }
        int index = 0xFF & (byte) 254; // add an ending byte for decoding
        occurrences[index]++;
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
    
    /**
    * Recreates a Huffman tree as an array using codes and code lengths of each byte value [0-255].
    *
    * @param   codes     Huffman code for each byte value [0-255]
    * @param   codeLengths     Code length for each byte value [0-255]
    *
    * @return Huffman tree as an array with byte values as nodes
    */
    private int[] recreateHuffmanTree(int[] codes, int[] codeLengths) {
//        int[] huffmanTree = new int[65535];
        int[] huffmanTree = new int[1000000];
        for (int i = 0; i < codes.length; i++) {
            if (codes[i] >= 0) {
                int code = codes[i];
                int treeIndex = 1;
                int codeLength = codeLengths[i];
                int highestBit = (int) Math.pow(2, (codeLength-1));
                for (int j = 0; j < codeLength; j++) { 
                    int a = code & highestBit;
                    if (a == highestBit) {
                        treeIndex = 2 * treeIndex + 1; // right child
                    } else {
                        treeIndex = 2 * treeIndex; // left child
                    }
                    code = code << 1;
                }
                huffmanTree[treeIndex] = i + 1;
            }
        }
        return huffmanTree;
    }
    
    private void createFileHeader(int[] codeLengths, ByteWriter writer) {
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
}
