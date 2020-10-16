
package OKcompress;

import OKcompress.domain.IntegerQueue;
import OKcompress.utils.BitReader;
import OKcompress.utils.ByteWriter;

/**
 *
 * Lempel–Ziv–Storer–Szymanski algorithm for data compression and decompression.
 */
public class LZSS {
    
    private int offsetBits;
    private int lengthBits;
    
    public LZSS(int offsetBits, int lengthBits) {
        this.offsetBits = offsetBits;
        this.lengthBits = lengthBits;
    }
    
    /**
    * Encodes given input data using LZSS data compression algorithm. Potential matches in the sliding window are searched
    * using a brute force method of scanning the whole sliding window.
    *
    * @param   input    The original, uncompressed data
    * 
    * @return Encoded data as a IntegerQueue.
    */
    public byte[] encodeUsingBruteForce(byte[] input) {
        ByteWriter writer = new ByteWriter();
        writer.writeBit((byte) 1);
        writer.writeBit((byte) 1);
        int maxOffset = (int) Math.pow(2, offsetBits) - 1;
        int dictionaryStartIndex = 0;
        int dictionaryEndIndex = -1;
        boolean coded = false;
        for (int i = 0; i < input.length; i++) {
            dictionaryEndIndex = i - 1;
            if (i > maxOffset) {
                dictionaryStartIndex = i - maxOffset;
            }
            for (int j = dictionaryStartIndex; j <= dictionaryEndIndex; j++) {
                if (input[i] == input[j]) { // found a match
                    int length = checkMatchLength(input, i, j);
                    if (length >= 3) { // match has to be at least 3 bytes long to encode
                        writer.writeLZSSCoded(i - j, length, offsetBits, lengthBits);
                        coded = true;
                        i += length - 1;
                        break;
                    }
                }               
            }            
            if (!coded) { // didn't find a match at least 3 bytes long
                writer.writeLZSSUncoded(input[i]);
            } else {
                coded = false;
            }
        }       
        writer.close();
        return writer.getBytes();
    }
    
    /**
    * Encodes given input data using LZSS data compression algorithm. Queues are used to store the indexes of all occurrences of every byte value
    * [0-255] in the sliding window.
    *
    * @param   input    The original, uncompressed data
    * 
    * @return Encoded data as a IntegerQueue.
    */
    public byte[] encodeUsingQueues(byte[] input) {
        ByteWriter writer = new ByteWriter();
        writer.writeBit((byte) 1);
        writer.writeBit((byte) 1);
        IntegerQueue[] matchQueues = new IntegerQueue[256];
        for (int i = 0; i < 256; i++) {
            matchQueues[i] = new IntegerQueue(1000);
        }
        boolean coded = false;
        for (int i = 0; i < input.length; i++) {
            maintainMatchQueues(input, matchQueues, i, 1);
            int[] matchIndexes = matchQueues[0xFF & input[i]].getArray();
            for (int j = 0; j < matchIndexes.length; j++) {
                int matchIndex = matchIndexes[j];
                int length = checkMatchLength(input, i, matchIndex);
                if (length >= 3) { // match has to be at least 3 bytes long to encode
                    writer.writeLZSSCoded(i - matchIndex, length, offsetBits, lengthBits);
                    coded = true;
                    maintainMatchQueues(input, matchQueues, i+1, length-1);
                    i += length - 1;
                    break;
                }            
            }
            
            if (!coded) { // didn't find a match at least 3 bytes long
                writer.writeLZSSUncoded(input[i]);
            } else {
                coded = false;
            }
        }
        writer.close();
        return writer.getBytes();
    }
    
    public byte[] decode(byte[] bytes) {
        IntegerQueue output = new IntegerQueue(2 * bytes.length);    
        BitReader reader = new BitReader(bytes);
        reader.readInt(2);
        while (true) {
            int signBit = reader.readBit();
            if (signBit == -1) { // end of input
                break;
            } else if (signBit == 1) { // coded bytes
                int offset = reader.readInt(offsetBits);
                int length = reader.readInt(lengthBits);
                for (int j = 0; j < length; j++) { // add {length} bytes starting from index {size - offset} 
                    output.add(output.get(output.size() - offset));
                }
            } else { // uncoded byte
                int nextByte = reader.readByte();
                if (nextByte < 256) {
                    output.add(nextByte);
                }
            }
        }
        return output.getBytes();
    }
    
    /**
    * Checks how long a match is from the initial matching point.
    *
    * @param   input    The original, uncompressed data
    * @param   inputIndex    The index that immediately follows the sliding window
    * @param   dictionaryIndex    The initial matching point
    */
    private int checkMatchLength(byte[] input, int inputIndex, int dictionaryIndex) {
        int length = 1;
        int maxLength = (int) Math.pow(2, lengthBits) - 1;
        while (true) { // check how long the match is
            if (length > (maxLength - 1)) {
                break;
            }
            inputIndex++;
            dictionaryIndex++;
            if (inputIndex >= input.length) {
                break;
            }
            if (input[inputIndex] == input[dictionaryIndex]) { // next bytes match as well
                length++;
            } else {
                break;
            }
        }
        return length;
    }
    
    /**
    * Updates the queues containing match indexes when the sliding window is shifted forwards.
    *
    * @param   input    The original, uncompressed data
    * @param   matchQueues    Queues containing all occurrences of every byte value
    * [0-255] in the sliding window
    * @param   inputIndex    The index that immediately follows the sliding window
    * @param   numberOfShifts    The number of steps/indexes the sliding window is to be shifted forwards
    */
    private void maintainMatchQueues(byte[] input, IntegerQueue[] matchQueues, int inputIndex, int numberOfShifts) {
        int maxOffset = (int) Math.pow(2, offsetBits) - 1;
        
        if (inputIndex > 0) {
            for (int i = 0; i < numberOfShifts; i++) {
                if (inputIndex > maxOffset) {
                    int dictionaryStartIndex = inputIndex - maxOffset;
                    if (!matchQueues[0xFF & input[dictionaryStartIndex - 1]].isEmpty()) {
                        matchQueues[0xFF & input[dictionaryStartIndex - 1]].removeLast();                   
                    }     
                }

                int dictionaryEndIndex = inputIndex - 1;
                matchQueues[0xFF & input[dictionaryEndIndex]].addFirst(dictionaryEndIndex);              
                inputIndex++;
            }           
        }
    }
}
