/*
 Utility class for writing individual bits to a byte
 */
package domain;

import java.io.*;
import java.util.ArrayList;

public class ByteOutputStream extends FileOutputStream {

    byte buffer;
    byte position;
    ArrayList<Byte> byteArray;

    public ByteOutputStream(File file) throws FileNotFoundException {
        super(file);
        buffer = 0x00;
        byteArray = new ArrayList<>();
    }

    public void write(byte bit) throws IOException {
        if (!(bit == 0 || bit == 1))
            throw new RuntimeException("A bit can only be a 1 or 0");

        buffer = (byte)(buffer << 1);
        buffer |= bit;

        position++;

        if (position == 8)
            flushByte();
    }

    public void write(byte[] bits) throws IOException {
        for (byte bit : bits)
            write(bit);
    }

    public void close() throws IOException {
        if (position != 0) {
            buffer = (byte)(buffer << (8 - position));
            flushByte();
        }
        super.close();
    }

    private void flushByte() throws IOException {
        System.out.println(Integer.toBinaryString(Byte.toUnsignedInt(buffer)));
        super.write(buffer);
        buffer = 0x00;
        position = 0;
    }
    
    public ArrayList<Byte> getByteArray() {
        return byteArray;
    }
}