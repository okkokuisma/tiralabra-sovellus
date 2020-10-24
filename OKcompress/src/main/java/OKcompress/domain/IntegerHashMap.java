/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OKcompress.domain;

/**
 *
 * @author ogkuisma
 */
public class IntegerHashMap {
    private IntegerHashMapNode[] table;
    private IntegerQueue[] queues;

    public IntegerHashMap() {
        table = new IntegerHashMapNode[883];
    }
    
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
    
    public int get(int key) {
        int hash = key % 883;
        for (IntegerHashMapNode node = table[hash]; node != null; node = node.nextNode) {
            if (node.key == key) {
                return node.value;
            }
        }         
        return -1;
    }
    
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
