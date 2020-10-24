package OKcompress.utils;

import OKcompress.domain.IntegerQueue;

/**
 * Utility class for writing individual bits to a byte.
 */
public class ByteWriter {

    byte buffer;
    byte position;
    IntegerQueue output;

    public ByteWriter() {
        buffer = 0x00;
        output = new IntegerQueue(10000);
    }
    
    public void writeBit(byte bit) {
        buffer = (byte) (buffer << 1);
        buffer |= bit;
        position++;

        if (position == 8) {
            flushByte();
        }
    }
    
    /**
    * Writes the given byte with a sign bit 0 preceding it.
    *
    * @param   input    Byte to be written
    */
    public void writeLZSSUncoded(byte input) {
        writeBit((byte) 0); // 0 bit to sign an uncoded byte

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
    
    /**
    * Writes a (offset, length) pair with given offset and length values and a sign bit 1 preceding it.
    *
    * @param   offset    Offset value of found match
    * @param   length    Length value of found match
    */
    public void writeLZSSCoded(int offset, int length, int offsetBits, int lengthBits) {
        writeBit((byte) 1); // sign bit for coded bytes 
        int highestBit = (int) Math.pow(2, (offsetBits - 1)); // get the bits
        for (int i = 0; i < offsetBits; i++) { // match position coded into 11 bits
            int a = offset & highestBit;
            if (a == highestBit) { // check whether the first bit is 1 or 0
                writeBit((byte) 1);
            } else {
                writeBit((byte) 0);
            }
            offset = offset << 1;
        }

        highestBit = (int) Math.pow(2, (lengthBits - 1));
        for (int i = 0; i < lengthBits; i++) { // match length coded into 4 bits
            int a = length & highestBit;
            if (a == highestBit) { // check whether the first bit is 1 or 0
                writeBit((byte) 1);
            } else {
                writeBit((byte) 0);
            }
            length = length << 1;
        }
    }
    
    /**
    * Adds zero bits as padding to the last byte and adds it to IntegerQueue.
    */
    public void close() {
        if (position != 0) {
            buffer = (byte) (buffer << (8 - position));
            flushByte();
        }
    }
    
    private void flushByte() {
        output.add(buffer);
        buffer = 0x00;
        position = 0;
    }
    
    public IntegerQueue getOutput() {
        return output;
    }
    
    public byte[] getBytes() {
        return output.getBytes();
    }
}