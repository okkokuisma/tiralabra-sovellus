/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OKcompress.utils;

/**
 *
 * @author ogkuisma
 */
public class BitReader {
    private byte[] input;
    private int index;
    private int digits;
    private int numDigits;
    
    public BitReader(byte[] input) {
        this.input = input;
        index = 0;
        nextByte();
    }
    
    public int readInt(int bits) { // returns a {bits} bit unsigned int
        int value = 0;
        for (int i = 0; i < bits; i++) {
            if (readBit() == 1) {
                value += Math.pow(2, (bits-1)-i);
            }
        }
        return value;
    }
    
    public byte readByte() {
        return (byte) readInt(8);
    }
    
    public int readBit() {
        if (digits == -1) {
            return -1;
        }
        int result;
        int a = digits & 128;
        if (a == 128) { // check whether the first bit is 1 or 0
            result = 1;
        } else {
            result = 0;
        }
        digits = digits << 1;
        numDigits++;
        if (numDigits == 8) {
            nextByte();
        }
        return result;
    }

    private void nextByte() {
        if (index == input.length) {
            digits = -1;
            return;
        }
        digits = 0x000000FF & input[index];
        numDigits = 0;
        index++;
    }
}
