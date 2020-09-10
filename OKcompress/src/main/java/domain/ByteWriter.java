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
        writeBit((byte) 0);
        String bits = Integer.toBinaryString(Byte.toUnsignedInt(input));
        for (int i = 0; i < bits.length(); i++) {
            writeBit((byte) Character.getNumericValue(bits.charAt(i)));
        }
    }

    public void writeCoded(int pos, int length) {
        String posBits = Integer.toBinaryString(pos);
        String lenBits = Integer.toBinaryString(length);
        int l = 11 - posBits.length();
        for (int i = 0; i < l; i++) { // match position coded into 11 bits
            posBits = "0" + posBits;
        } 
        l = 4 - lenBits.length();
        for (int i = 0; i < l; i++) { //match length coded into 4 bits
            lenBits = "0" + lenBits;
        }
        
        writeBit((byte) 1); // sign bit for coded 
        for (int i = 0; i < posBits.length(); i++) {
            writeBit((byte) Character.getNumericValue(posBits.charAt(i)));
        }
        for (int i = 0; i < lenBits.length(); i++) {
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