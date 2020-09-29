
package OKcompress;

import OKcompress.domain.ByteList;
import OKcompress.domain.HuffmanHeap;
import OKcompress.domain.HuffmanNode;
import OKcompress.utils.BitReader;
import OKcompress.utils.ByteWriter;
import com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter;

/**
 *
 * Huffman coding 
 */
public class Huffman {
    
    public ByteList encode(byte[] input) {
        ByteWriter writer = new ByteWriter();
        int[] codeLengths = getCodeLengths(input);
        int[] codes = createCodeArray(codeLengths);
        // encode
        createFileHeader(codeLengths, writer);
        for (int i = 0; i < input.length; i++) {
            int code = codes[0xFF & input[i]];
            int codeLength = codeLengths[0xFF & input[i]];
            int highestBit = (int) Math.pow(2, (codeLength-1));
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
        int code = codes[254];
        int codeLength = codeLengths[254];
        int highestBit = (int) Math.pow(2, (codeLength-1));
        for (int j = 0; j < codeLength; j++) { 
            int a = code & highestBit;
            if (a == highestBit) { // check whether the first bit is 1 or 0
                writer.writeBit((byte) 1);
            } else {
                writer.writeBit((byte) 0);
            }
            code = code << 1;               
        }
        return writer.getByteArray();
    }
    
    public ByteList decode(byte[] input) {
        ByteList output = new ByteList();
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
            if (nextBit == 1) { // right child
                treeIndex = 2 * treeIndex + 1;
            } else { // left child
                treeIndex = 2 * treeIndex;
            }
            if (nextBit == -1) {
                
                break;
            }
            if (huffmanTree[treeIndex] > 0) {
                byte nextByte = (byte) (huffmanTree[treeIndex] - 1);
                if (nextByte == (byte) 254) {
                    System.out.println("moi"); // doesn't happen and my tired eyes can't see why
                    break;
                }
                output.add((byte) (huffmanTree[treeIndex] - 1));
                treeIndex = 1;
            }
        }
        return output;
    }
    
    private int[] getCodeLengths(byte[] input) {
        int[] occurances = new int[256];
        for (int i = 0; i < input.length; i++) { // check how many occurances of each individual byte there are in the input array
            int index = 0xFF & input[i]; // get the positive index for byte
            occurances[index]++;
        }
        int index = 0xFF & (byte) 254; // add an ending byte for decoding
        occurances[index]++;
        
        int[] nodes = new int[256];
        int[] nodeCounts = new int[256];
        int nodeCounter = 0;
        
        for (int i = 0; i < occurances.length; i++) { 
            if (occurances[i] != 0) {
                nodes[nodeCounter] = i + 1; // add a node pointer for each symbol that occured in the input
                nodeCounts[nodeCounter] = occurances[i]; // weigth for each node for sorting the nodes
                nodeCounter++;
            }
        }
        
        int[] codeLengths = new int[256];
        int[] pointerArray = new int[256];
        
        while (nodeCounter > 1) {
            // sorting the leaf nodes by occurances (nodes with most occurances to the start)
            for (int i = 0; i < nodeCounter; i++) {
                if (nodeCounts[i] < nodeCounts[i + 1]) {
                    int swap = nodes[i];
                    nodes[i] = nodes[i + 1];
                    nodes[i + 1] = swap;
                    swap = nodeCounts[i];
                    nodeCounts[i] = nodeCounts[i + 1];
                    nodeCounts[i + 1] = swap;
                    if (i > 0) {
                        i = i - 2;
                    }
                }
            }
            
            // increment bit lengths
            int left = nodes[nodeCounter - 2];
            int right = nodes[nodeCounter - 1];
            while (left > 0) {
                codeLengths[left - 1]++;
                int next = pointerArray[left - 1];
                if (next == 0 && right > 0) {
                    pointerArray[left - 1] = right;
                    next = right;
                    right = 0;
                }
                left = next;
            }
            
            nodeCounts[nodeCounter - 2] += nodeCounts[nodeCounter - 1]; // combine last two nodes
            nodeCounter--;
        }
        return codeLengths;
    }
    
    private int[] createCodeArray(int[] codeLengths) {
        int[] bitLengths = new int[16];
        for (int i = 0; i < codeLengths.length; i++) {
            if (codeLengths[i] > 0) {
                bitLengths[codeLengths[i]]++; // 
            }
        }
        int[] minimumValues = createMinimumNumericalValueArray(bitLengths);
        int[] codes = new int[256];
        for (int i = 0; i < 256; i++) {
            if (codeLengths[i] > 0) {
                codes[i] = minimumValues[codeLengths[i]]++;  
            }
        }
        return codes;
    }
    
    private int[] createMinimumNumericalValueArray(int[] codeLengths) {
        int[] numericalValues = new int[codeLengths.length];
        int code = 0;
        for (int i = 0; i < 16; i++) {
            numericalValues[i] = code;
            code = (code + codeLengths[i]) << 1;
        }
        return numericalValues;
    }
    
    private int[] recreateHuffmanTree(int[] codes, int[] codeLengths) {
        int[] huffmanTree = new int[65535];
        for (int i = 0; i < codes.length; i++) {
            if (codes[i] >= 0) {
                int code = codes[i];
                int treeIndex = 1;
                int codeLength = codeLengths[i];
                int highestBit = (int)Math.pow(2, (codeLength-1));
                for (int j = 0; j < codeLength; j++) { 
                    int a = code & highestBit;
                    if (a == highestBit) {
                        treeIndex = 2 * treeIndex + 1;
                    } else {
                        treeIndex = 2 * treeIndex;
                    }
                    code = code << 1;
                }
                huffmanTree[treeIndex] = i + 1;
            }
        }
        return huffmanTree;
    }
    
    private void createFileHeader(int[] codeLengths, ByteWriter writer) {
        for (int i = 0; i < codeLengths.length; i++) {
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
