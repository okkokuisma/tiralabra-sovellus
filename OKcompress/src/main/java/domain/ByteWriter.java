/*
 Utility class for writing individual bits to a byte
 */
package domain;

import java.io.*;
import java.util.ArrayList;

public class ByteWriter {

    byte buffer;
    byte position;
    ArrayList<Byte> byteArray;

    public ByteWriter(){
        buffer = 0x00;
        byteArray = new ArrayList<>();
    }

    public void writeBit(byte bit) {
        if (!(bit == 0 || bit == 1))
            throw new RuntimeException("A bit can only be a 1 or 0");

        buffer = (byte)(buffer << 1);
        buffer |= bit;

        position++;

        if (position == 8)
            flushByte();
    }
    
    public void writeUncoded(byte input) {
        flushByte(); // 0 byte to sign an uncoded byte
        String bits = String.format("%8s", Integer.toBinaryString(input & 0xFF)).replace(' ', '0');

        for (int i = 0; i < bits.length(); i++) {
            writeBit((byte) Character.getNumericValue(bits.charAt(i)));
        }
    }

    public void writeCoded(int pos, int length) {
        String posBits = String.format("%11s", Integer.toBinaryString(pos & 0xFFF)).replace(' ', '0'); // match position coded into 11 bits
        String lenBits = String.format("%4s", Integer.toBinaryString(length & 0xF)).replace(' ', '0'); // match length coded into 4 bits
        if (pos == 2167) {
            System.out.println("bits: " + posBits);
        }
        writeBit((byte) 1); // sign bit for coded 
        for (int i = 0; i < 11; i++) {
            writeBit((byte) Character.getNumericValue(posBits.charAt(i)));
        }
        for (int i = 0; i < 4; i++) {
            writeBit((byte) Character.getNumericValue(lenBits.charAt(i)));
        }
    }

    public void close(){
        if (position != 0) {
            buffer = (byte)(buffer << (8 - position));
            flushByte();
        }
    }

    private void flushByte() {
        byteArray.add(buffer);
        buffer = 0x00;
        position = 0;
    }
    
    public ArrayList<Byte> getByteArray() {
        return byteArray;
    }
}