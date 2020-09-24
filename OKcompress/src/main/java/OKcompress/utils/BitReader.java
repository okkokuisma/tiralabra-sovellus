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
    private int bits;
    private int position;
    
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
    
    public int readBit() { // return next bit, -1 if at the end of input
        if (bits == -1) {
            return -1;
        }
        int result;
        int a = bits & 128;
        if (a == 128) { // check whether the first bit is 1 or 0
            result = 1;
        } else {
            result = 0;
        }
        bits = bits << 1;
        position++;
        if (position == 8) { // read all 8 bits
            nextByte();
        }
        return result;
    }

    private void nextByte() {
        if (index == input.length) {
            bits = -1;
            return;
        }
        bits = 0x000000FF & input[index]; // int value of next byte
        position = 0;
        index++;
    }
}
