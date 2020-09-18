
package OKcompress;

import OKcompress.domain.HuffmanHeap;
import OKcompress.domain.HuffmanNode;
import OKcompress.utils.ByteWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * Huffman coding 
 */
public class Huffman {
    
    public ArrayList<Byte> encode(byte[] input) {
//        PriorityQueue<HuffmanNode> huffmanTree = new PriorityQueue();
        HuffmanHeap huffmanTree = new HuffmanHeap();
        
        int[] occurances = new int[256];
        for (int i = 0; i < input.length; i++) { // check how many occurances of each individual byte there are in the input array
            int index = 0xFF & input[i]; // get the positive index for byte
            occurances[index]++;
        }
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
        
        String[] codes = new String[256];
        walkTree(huffmanTree.poll(), "", codes); // create an array of the huffman codes
        
        ByteWriter writer = new ByteWriter();
        for (int i = 0; i < input.length; i++) {
            String binaryString = codes[0xFF & input[i]];
            for (int j = 0; j < binaryString.length(); j++) {
                if (binaryString.charAt(j) == 48) {
                    writer.writeBit((byte) 0);
                } else {
                    writer.writeBit((byte) 1);
                }
            }
        }
        return writer.getByteArray();
    }
    
    private void walkTree(HuffmanNode node, String binaryString, String[] codes) {
        if (node == null) {
            return;
        }
        if (node.leftChild == null & node.rightChild == null) {
            codes[0xFF & node.byteValue] = binaryString;
        } else {
            binaryString = binaryString + "0";
            walkTree(node.leftChild, binaryString, codes);
            binaryString = binaryString.substring(0, binaryString.length() - 1) + "1"; // replace the last 0 with 1
            walkTree(node.rightChild, binaryString, codes);
        }
    }
}
