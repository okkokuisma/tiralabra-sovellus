/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import domain.ByteOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ogkuisma
 */
public class LZSS {
    public void encode(byte[] input) throws IOException {
        File output = new File("OKcompressed");
        output.createNewFile();
        ByteOutputStream outputStream = new ByteOutputStream(new File("OKcompressed"));
        
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
                        outputStream.write((byte)1); // sign bit for encoded
                        
                        char[] posBits = Integer.toBinaryString(j).toCharArray();
                        char[] lenBits = Integer.toBinaryString(length).toCharArray();
                        for (char bit : posBits) {
                            outputStream.write((byte)Character.getNumericValue(bit));
                        }
                        for (char bit : lenBits) {
                            outputStream.write((byte)Character.getNumericValue(bit));
                        }
                        coded = true;
                        i += length;
                    }
                }               
            }
            
            if (!coded) { // didn't found a match at least 3 bytes long
                outputStream.write((byte)0); // sign bit for uncoded
                char[] bits = Integer.toBinaryString(Byte.toUnsignedInt(input[i])).toCharArray();
                for (char bit : bits) {
                    outputStream.write((byte)Character.getNumericValue(bit));
                }
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
    }
}
