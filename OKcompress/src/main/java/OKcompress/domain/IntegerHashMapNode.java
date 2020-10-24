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
public class IntegerHashMapNode {
    int key;
    int value;
    IntegerHashMapNode nextNode;
    int hashValue;
    
    public IntegerHashMapNode(int key, int value, IntegerHashMapNode nextNode) {
        this.key = key;
        this.value = value;
        this.nextNode = nextNode;
    }
}
