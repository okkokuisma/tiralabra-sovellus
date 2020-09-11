/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import utils.ByteWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ogkuisma
 */
public class LZSS {
    public ArrayList<Byte> encode(byte[] input) {
        ByteWriter output = new ByteWriter();
        
        int dictionaryStartIndex = 0;
        int dictionaryEndIndex = -1;
        boolean coded = false;
        for (int i = 0; i < input.length; i++) {
            dictionaryEndIndex = i - 1;
            if (i > 2046) {
                dictionaryStartIndex = dictionaryEndIndex - 2046;
            }
            for (int j = dictionaryStartIndex; j <= dictionaryEndIndex; j++) {
                if (input[i] == input[j]) { // found a match
                    int length = 1;
                    int inputIndex = i;
                    int dictionaryIndex = j;
                    while (true) { // check how long the match is
                        if (length > 14) { // max length for match is 15
                            break;
                        }
                        
                        inputIndex++;
                        dictionaryIndex++;
                        
                        if (inputIndex >= input.length) {
                            break;
                        }
                        
                        if (input[inputIndex] == input[dictionaryIndex]) { // next bytes match as well
                            length++;
                        } else {
                            break;
                        }
                    }
                    
                    if (length >= 3) { // match has to be at least 3 bytes long to encode
                        output.writeCoded(i-j, length);
                        coded = true;
                        i += length-1;
                        break;
                    }
                }               
            }
            
            if (!coded) { // didn't found a match at least 3 bytes long
                output.writeUncoded(input[i]);
            } else {
                coded = false;
            }
        }
        
        output.close();
        return output.getByteArray();
    }
    
    public ArrayList<Byte> decode(ArrayList<Byte> bytes) {
        int n = bytes.size();
        ArrayList<Byte> output = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (bytes.get(i) == 0) { // a zero byte indicates and precedes an uncoded byte
                output.add(bytes.get(i+1));
                i++;
            } else {
                String encoded = String.format("%8s", Integer.toBinaryString(bytes.get(i) & 0xFF)).replace(' ', '0') + 
                        String.format("%8s", Integer.toBinaryString(bytes.get(i+1) & 0xFF)).replace(' ', '0');
                int offset = Integer.parseInt(encoded.substring(1, 12), 2);
                int length = Integer.parseInt(encoded.substring(12, 16), 2);
                for (int j = 0; j < length; j++) {
                    output.add(output.get(output.size() - offset));
                }
                i++;
            }
        }
        
        return output;
    }
}
