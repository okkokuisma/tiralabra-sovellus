/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OKcompress.domain;

/**
 * A dynamic hash map structure for storing integers.
 */
public class IntegerHashMap {
    private IntegerHashMapNode[] table;
    private IntegerQueue[] queues;

    public IntegerHashMap() {
        table = new IntegerHashMapNode[883];
    }
    
    /**
    * Adds a new (key, value) pair to this IntegerHashMap.
    *
    * @param   key    The key which is used to find the value
    * @param   value    The value to be stored
    * 
    */
    public void put(int key, int value) {
        int hash = key % 883;
        for (IntegerHashMapNode node = table[hash]; node != null; node = node.nextNode) {
            if (node.key == key) {
                node.value = value;
                return;
            }
        }
        if (table[hash] == null) {
            table[hash] = new IntegerHashMapNode(key, value, null);
        } else {
            IntegerHashMapNode node = table[hash];
            table[hash] = new IntegerHashMapNode(key, value, node);
        }
    }
    
    /**
    * Returns the value stored with the given key.
    *
    * @param   key    The key of the value to be searched for
    * 
    * @return The respective value.
    */
    public int get(int key) {
        int hash = key % 883;
        for (IntegerHashMapNode node = table[hash]; node != null; node = node.nextNode) {
            if (node.key == key) {
                return node.value;
            }
        }         
        return -1;
    }
    
    /**
    * @param   key    The key to be searched for
    * 
    * @return True if this IntegerHashMap contains the given key, false if not.
    */
    public boolean containsKey(int key) {
        int hash = key % 883;
        for (IntegerHashMapNode node = table[hash]; node != null; node = node.nextNode) {
            if (node.key == key) {
                return true;
            }
        }
        return false;
    }

}
