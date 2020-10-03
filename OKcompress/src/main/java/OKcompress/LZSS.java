
package OKcompress;

import OKcompress.domain.ByteList;
import OKcompress.utils.BitReader;
import OKcompress.utils.ByteWriter;
import java.util.ArrayDeque;

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
    
    public ByteList encode(byte[] input) {
        ByteWriter output = new ByteWriter();
        int maxOffset = (int) Math.pow(2, offsetBits) - 1;
        int maxLength = (int) Math.pow(2, lengthBits) - 1;
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
                        output.writeLZSSCoded(i - j, length, offsetBits, lengthBits);
                        coded = true;
                        i += length - 1;
                        break;
                    }
                }               
            }
            
            if (!coded) { // didn't find a match at least 3 bytes long
                output.writeLZSSUncoded(input[i]);
            } else {
                coded = false;
            }
        }
        
        output.close();
        return output.getBytes();
    }
    
    public ByteList decode(ByteList bytes) {
        ByteList output = new ByteList();    
        BitReader reader = new BitReader(bytes.getArray());
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
                    output.add((byte) nextByte);
                }
            }
        }
        return output;
    }
    
    public ByteList encodeUsingQueues(byte[] input) {
        ByteWriter output = new ByteWriter();
        ArrayDeque<Integer>[] matchQueues = new ArrayDeque[256];
        for (int i = 0; i < 256; i++) {
            matchQueues[i] = new ArrayDeque();
        }
        boolean coded = false;
        for (int i = 0; i < input.length; i++) {
            maintainMatchQueues(input, matchQueues, i, 1);
            Integer[] matchIndexes = new Integer[matchQueues[0xFF & input[i]].size()];
            matchQueues[0xFF & input[i]].toArray(matchIndexes);
            for (int j = 0; j < matchIndexes.length; j++) {
                int matchIndex = matchIndexes[j];
                int length = checkMatchLength(input, i, matchIndex);
                if (length >= 3) { // match has to be at least 3 bytes long to encode
                    output.writeLZSSCoded(i - matchIndex, length, offsetBits, lengthBits);
                    coded = true;
                    maintainMatchQueues(input, matchQueues, i+1, length-1);
                    i += length - 1;
                    break;
                }            
            }
            
            if (!coded) { // didn't find a match at least 3 bytes long
                output.writeLZSSUncoded(input[i]);
            } else {
                coded = false;
            }
        }
        output.close();
        return output.getBytes();
    }
    
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
    
    private void maintainMatchQueues(byte[] input, ArrayDeque[] matchQueues, int inputIndex, int numberOfShifts) {
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
