/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OKcompress;

import OKcompress.domain.HuffmanNode;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author ogkuisma
 */
public class Huffman {
    
    public ArrayList<Byte> encode(byte[] input) {
        PriorityQueue<HuffmanNode> huffmanTree = new PriorityQueue();
        
        byte[] occurances = new byte[256];
        for (int i = 0; i < input.length; i++) {
            int index = 0xff & input[i];
            occurances[index]++;
        }
        for (int i = 0; i < occurances.length; i++) {
            if (occurances[i] != 0) {
                HuffmanNode node = new HuffmanNode((byte) i, occurances[i]);
                huffmanTree.add(node);
            }
        }
        while (huffmanTree.size() != 1) {
            HuffmanNode node = new HuffmanNode();
            node.leftChild = huffmanTree.poll();
            node.rightChild = huffmanTree.poll();
            node.weigth = node.leftChild.weigth + node.rightChild.weigth;
            huffmanTree.add(node);
        }   
        
        return null;
    }
}
