
package OKcompress.domain;

import java.util.Arrays;

/**
 *
 * @author ogkuisma
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
    
    public void addFirst(byte x) {
        if (size == 0) {
            array[0] = x;
            size++;
            return;
        }
        
        if (startIndex == 0) {
            startIndex = array.length - 1;
            array[startIndex] = x;
            size++;
            return;
        }
        
        startIndex--;
        size++;
        array[startIndex] = x;
        
        if (size >= ((array.length / 4) * 3)) {
            biggerArray();
        }
    }
 
    public void add(byte x) {
        if (size == 0) {
            array[0] = x;
            size++;
            return;
        }
        
        array[endIndex + 1] = x;
        endIndex++;
        size++;
        
        if (size >= ((array.length / 4) * 3)) {
            biggerArray();
        }
    }
 
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
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return (size == 0);
    }
    
    public byte[] getArray() {
        byte[] newArray = new byte[size];
        
        if (startIndex == 0) {
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
        }   
        return newArray;
    }
 
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
