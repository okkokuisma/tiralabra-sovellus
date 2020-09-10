/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import domain.ByteWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ogkuisma
 */
public class LZSS {
    public ArrayList<Byte> encode(byte[] input) throws IOException {
        ByteWriter output = new ByteWriter();
        
        int dictionaryStartIndex = 0;
        int dictionaryEndIndex = -1;
        boolean coded = false;
        for (int i = 0; i < input.length; i++) {         
            for (int j = dictionaryStartIndex; j <= dictionaryEndIndex; j++) {
                if (input[i] == input[j]) { // found a match
                    int length = 1;
                    int inputIndex = i;
                    int dictionaryIndex = j;
                    while (true) { // check how long the match is
                        if (length > 10) { // max length for match is 11
                            break;
                        }
                        
                        inputIndex++;
                        dictionaryIndex++;
                        
                        if (inputIndex >= input.length) {
                            break;
                        }
                        
                        if (input[inputIndex] == input[dictionaryIndex]) {
                            length++;
                        } else {
                            break;
                        }
                    }
                    
                    if (length >= 3) { // match has to be at least 3 bytes long to encode
                        output.writeCoded(j, length);
                        coded = true;
                        i += length;
                    }
                }               
            }
            
            if (!coded) { // didn't found a match at least 3 bytes long
                output.writeUncoded(input[i]);
            } else {
                coded = false;
            }
            
            if (i > 2047) {
                dictionaryStartIndex++;
                dictionaryEndIndex++;
            } else {
                dictionaryEndIndex++;
            }
        }
        
        output.close();
        return output.getByteArray();
    }
}
