
package OKcompress.domain;

/**
 * Small minimum heap structure used to create Huffman trees and determine Huffman code lengths.
 */
public class HuffmanHeap {
    HuffmanNode[] heap;
    int last;
    
    public HuffmanHeap() {
        heap = new HuffmanNode[256];
        last = 0;
    }
    
    /**
    * Returns the root node without removing it from the heap.
    * 
    * @return The root node
    */
    public HuffmanNode peek() {
        return heap[1];
    }
    
    /**
    * Returns the root node and removes it from the heap.
    * 
    * @return The root node
    */
    public HuffmanNode poll() {
        HuffmanNode returnValue = heap[1];
        heap[1] = heap[last];
        last--;
        moveDown(1);
        return returnValue;
    }
    
    /**
    * Inserts a new node to the heap.
    * 
    * @param    added    Node to be added
    */
    public void add(HuffmanNode added) {
        last++;
        int index = last;
        if (index > 1) {
            while (index > 1 & added.compareTo(heap[parent(index)]) < 0) {
                heap[index] = heap[parent(index)];
                index = parent(index);
            }         
        }
        heap[index] = added;
    }
    
    /**
    * Recursive method that moves a node down in the heap until the heap property is satisfied again.
    * 
    * @param    index    Always gets the value 1 when called the first time (index of the root node)
    */
    private void moveDown(int index) {
        int smallerChild;
        if (left(index) == 0) {
            return;
        } else if (left(index) == last) {
            smallerChild = left(index);
        } else {
            if (heap[left(index)].compareTo(heap[right(index)]) < 0) {
                smallerChild = left(index);
            } else {
                smallerChild = right(index);
            }
        }
        if (heap[index].compareTo(heap[smallerChild]) > 0) {
            HuffmanNode storeIt = heap[index];
            heap[index] = heap[smallerChild];
            heap[smallerChild] = storeIt;
            moveDown(smallerChild);
        }
    }
    
    /**
    * @return The number of nodes in this heap
    */
    public int getLast() {
        return last;
    }
    
    private int left(int index) {
        if (2 * index > last) {
            return 0;
        }
        return 2 * index;
    }
    
    private int right(int index) {
        if (2 * index + 1 > last) {
            return 0;
        }
        return 2 * index + 1;
    }
    
    private int parent(int index) {
        return index / 2;
    }
}
