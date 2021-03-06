
package OKcompress.utils;

/**
 * Utility class for reading a byte array as a bit stream.
 */
public class BitReader {
    private byte[] input;
    public int index;
    private int bits;
    private int position;
    
    public BitReader(byte[] input) {
        this.input = input;
        index = 0;
        nextByte();
    }
    
    /**
    * Reads next bits as an unsigned int, number of bits given as a parameter.
    *
    * @param   bits    The number of bits to be read
    *
    * @return Value of next bits as an unsigned int.
    */
    public int readInt(int bits) {
        int value = 0;
        for (int i = 0; i < bits; i++) {
            if (readBit() == 1) {
                value += Math.pow(2, (bits-1)-i);
            }
        }
        return value;
    }
    
    /**
    * Reads next 8 bits as an unsigned int.
    *
    * @return Next 8 bits as an unsigned int, 256 if there are less than 8 bits remaining.
    */
    public int readByte() {
        if (index == input.length && position != 0) {
            return 256;
        }
        return readInt(8);
    }
    
    /**
    * Reads the next bit.
    *
    * @return The value of next bit, -1 if at the end of input
    */
    public int readBit() {
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
