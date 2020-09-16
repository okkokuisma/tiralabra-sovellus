/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OKcompress;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author ogkuisma
 */
public class Huffman {
    
    public ArrayList<Byte> encode(byte[] input) {
        PriorityQueue huffmanTree = new PriorityQueue();
        
        byte[] occurances = new byte[256];
        for (int i = 0; i < input.length; i++) {
            int idx = 0xff & input[i];
            occurances[idx]++;
        }
        for (int i = 0; i < input.length; i++) {
            int idx = 0xff & input[i];
            occurances[idx]++;
        }
        
        return null;
    }
}
