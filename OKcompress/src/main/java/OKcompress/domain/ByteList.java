
package OKcompress.domain;

import java.util.Arrays;

/**
 * A dynamic list structure for storing bytes.
 */
public class ByteList {
    private byte[] array;
    private int startIndex;
    private int endIndex;
    private int size;
    
    public ByteList() {
        array = new byte[1000];
        startIndex = 0;
        endIndex = 0;
        size = 0;
    }
    
    /**
    * Adds a byte to the front of the list.
    * 
    * @param    added    Byte to be added
    */
    public void addFirst(byte added) {
        if (size == 0) {
            array[0] = added;
            size++;
            return;
        }
        
        if (startIndex == 0) {
            startIndex = array.length - 1;
            array[startIndex] = added;
            size++;
            return;
        }
        
        startIndex--;
        size++;
        array[startIndex] = added;
        
        if (size >= ((array.length / 4) * 3)) {
            biggerArray();
        }
    }
    
    /**
    * Adds a byte to the end of the list.
    * 
    * @param    added    Byte to be added
    */
    public void add(byte added) {
        if (size == 0) {
            array[0] = added;
            size++;
            return;
        }
        
        array[endIndex + 1] = added;
        endIndex++;
        size++;
        
        if (size >= ((array.length / 4) * 3)) {
            biggerArray();
        }
    }
    
    /**
    * Returns the byte in the given index.
    * 
    * @param    k    Index
    * 
    * @return The byte in the given index
    */
    public Byte get(int k) {
        if (k > array.length - 1) {
            return 0;
        }
        
        if (startIndex == 0) {
            return array[k];
        }
        
        int lengthAfterStartIndex = array.length - startIndex;
        if (k < lengthAfterStartIndex) {
            return array[startIndex + k];
        } else {
            return array[k - lengthAfterStartIndex];
        }
    }
    
    /**
    * @return The number of bytes in this ByteList
    */
    public int size() {
        return size;
    }
    
    /**
    * @return 1 if this ByteList is empty, 0 if there's at least one byte stored
    */
    public boolean isEmpty() {
        return (size == 0);
    }
    
    /**
    * @return The bytes in this ByteList as a byte array
    */
    public byte[] getArray() {
        byte[] newArray = new byte[size];
        
        if (startIndex == 0) {
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
        }   
        return newArray;
    }
    
    /**
    * Moves the stored bytes to a bigger array when the current one is filling up.
    */
    private void biggerArray() {
        byte[] newArray = new byte[array.length + (array.length / 2)];
        
        if (startIndex == 0) {
            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }
        } else {
            int index = 0;
            for (int i = startIndex; i < array.length; i++) {
                newArray[index] = array[i];
                index++;
            }
            
            for (int i = 0; i <= endIndex; i++) {
                newArray[index] = array[i];
                index++;
            }
            
            endIndex = index - 1;
        }
        
        startIndex = 0;
        array = newArray;
    }
    
    public String toString() {
        return Arrays.toString(array);
    }
}
