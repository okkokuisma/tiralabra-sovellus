
package OKcompress.domain;

import java.util.Arrays;

/**
 * A dynamic list structure for storing integers.
 */
public class IntegerQueue {
    private int[] array;
    private int startIndex;
    private int endIndex;
    private int size;
    
    public IntegerQueue(int queueSize) {
        array = new int[queueSize];
        startIndex = 0;
        endIndex = 0;
        size = 0;
    }
    
    /**
    * Adds an integer to the front of the IntegerQueue.
    * 
    * @param    added    Byte to be added
    */
    public void addFirst(int added) {
        if (size == 0) {
            array[startIndex] = added;
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
    * Adds an integer to the end of the IntegerQueue.
    * 
    * @param    added    Byte to be added
    */
    public void add(int added) {
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
    
    public void removeLast() {
        if (size > 0) {
            if (startIndex != endIndex) {
                if (endIndex == 0) {
                    endIndex = array.length - 1;
                } else {
                    endIndex--;                  
                }
            } 
            size--;               
        }
    }
    
    /**
    * Returns the integer stored in the given index.
    * 
    * @param    k    Index
    * 
    * @return The byte in the given index
    */
    public int get(int k) {      
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
    * @return The number of elements in this IntegerQueue.
    */
    public int size() {
        return size;
    }
    
    /**
    * @return 1 if this IntegerQueue is empty, 0 if there's at least one element stored.
    */
    public boolean isEmpty() {
        return (size == 0);
    }
    
    /**
    * @return The integers in this IntegerQueue as an array.
    */
    public int[] getArray() {
        int[] newArray = new int[size];
        if (size == 0) {
            return newArray;
        }
        if (startIndex < endIndex) {
            int index = 0;
            for (int i = startIndex; i <= endIndex; i++) {
                newArray[index] = array[i];
                index++;
            }
        } else if (startIndex == endIndex) {
            newArray[0] = array[startIndex];
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
        }
        return newArray;
    }
    
    /**
    * @return The integers in this IntegerQueue as a byte array.
    */
    public byte[] getBytes() {
        byte[] newArray = new byte[size];
        if (size == 0) {
            return newArray;
        }
        if (startIndex < endIndex) {
            int index = 0;
            for (int i = startIndex; i <= endIndex; i++) {
                newArray[index] = (byte) array[i];
                index++;
            }
        } else if (startIndex == endIndex) {
            newArray[0] = (byte) array[startIndex];
        } else {
            int index = 0;
            for (int i = startIndex; i < array.length; i++) {
                newArray[index] = (byte) array[i];
                index++;
            }
            
            for (int i = 0; i <= endIndex; i++) {
                newArray[index] = (byte) array[i];
                index++;
            }
        }
        return newArray;
    }
    
    /**
    * Moves the stored elements to a bigger array when the current one is filling up.
    */
    private void biggerArray() {
        int[] newArray = new int[array.length + (array.length / 2)];
        
        if (startIndex == 0) {
            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }
        } else {
            int index = 0;
            if (startIndex < endIndex) {
                for (int i = startIndex; i <= endIndex; i++) {
                    newArray[index] = array[i];
                    index++;
                }
            } else {                
                for (int i = startIndex; i < array.length; i++) {
                    newArray[index] = array[i];
                    index++;
                }

                for (int i = 0; i <= endIndex; i++) {
                    newArray[index] = array[i];
                    index++;
                }
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
