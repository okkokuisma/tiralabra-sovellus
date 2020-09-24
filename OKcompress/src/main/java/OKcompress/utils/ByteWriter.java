/*
 Utility class for writing individual bits to a byte
 */
package OKcompress.utils;

import OKcompress.domain.ByteList;

public class ByteWriter {

    byte buffer;
    byte position;
    ByteList byteArray;

    public ByteWriter() {
        buffer = 0x00;
        byteArray = new ByteList();
    }

    public void writeBit(byte bit) {
        if (!(bit == 0 || bit == 1)) {
            throw new RuntimeException("A bit can only be a 1 or 0");
        }
        buffer = (byte) (buffer << 1);
        buffer |= bit;
        position++;

        if (position == 8) {
            flushByte();
        }
    }
    
    public void writeUncoded(byte input) {
        writeBit((byte) 0); // 0 bit to sign an uncoded byte
//        String bits = String.format("%8s", Integer.toBinaryString(input & 0xFF)).replace(" ", "0");
//
//        for (int i = 0; i < bits.length(); i++) {
//            writeBit((byte) Character.getNumericValue(bits.charAt(i)));
//        
        int bits = 0x000000FF & input; // get the bits
            for (int i = 0; i < 8; i++) {
                int a = bits & 128;
                if (a == 128) { // check whether the first bit is 1 or 0
                    writeBit((byte) 1);
                } else {
                    writeBit((byte) 0);
                }
                bits = bits << 1;
            }
    }

    public void writeCoded(int offset, int length) {
        if (offset > 2047) {
            throw new RuntimeException("Offset input over 2047");
        } else if (length > 15) {
            throw new RuntimeException("Length input over 15");
        }
//        String posBits = String.format("%11s", Integer.toBinaryString(offset & 0xFFF)).replace(" ", "0"); // match position coded into 11 bits
//        String lenBits = String.format("%4s", Integer.toBinaryString(length & 0xF)).replace(" ", "0"); // match length coded into 4 bits
//        writeBit((byte) 1); // sign bit for coded 
//        for (int i = 0; i < 11; i++) {
//            writeBit((byte) Character.getNumericValue(posBits.charAt(i)));
//        }
//        for (int i = 0; i < 4; i++) {
//            writeBit((byte) Character.getNumericValue(lenBits.charAt(i)));
//        }
        writeBit((byte) 1); // sign bit for coded bytes 
        int posBits = 0x00000FFF & offset; // get the bits
            for (int i = 0; i < 11; i++) { // match position coded into 11 bits
                int a = posBits & 1024;
                if (a == 1024) { // check whether the first bit is 1 or 0
                    writeBit((byte) 1);
                } else {
                    writeBit((byte) 0);
                }
                posBits = posBits << 1;
            }
        int lenBits = 0x0000000F & length;
            for (int i = 0; i < 4; i++) { // match length coded into 4 bits
                int a = lenBits & 8;
                if (a == 8) { // check whether the first bit is 1 or 0
                    writeBit((byte) 1);
                } else {
                    writeBit((byte) 0);
                }
                lenBits = lenBits << 1;
            }
    }
    
    public void addByte(byte input) {
        if (input == 0) {
            flushByte();
        } else {
            int bits = 0x000000FF & input;
            for (int i = 0; i < 8; i++) {
                int a = bits & 128;
                if (a == 128) { // check whether the first bit is 1 or 0
                    writeBit((byte) 1);
                } else {
                    writeBit((byte) 0);
                }
                bits = bits << 1;
            }
        }
    }

    public void close() {
        if (position != 0) {
            buffer = (byte) (buffer << (8 - position));
            flushByte();
        }
    }

    private void flushByte() {
        byteArray.add(buffer);
        buffer = 0x00;
        position = 0;
    }
    
    public ByteList getByteArray() {
        return byteArray;
    }
}