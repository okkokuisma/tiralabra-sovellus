/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import OKcompress.domain.HuffmanHeap;
import OKcompress.domain.HuffmanNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogkuisma
 */
public class HuffmanHeapTest {
    HuffmanHeap h;
    
    public HuffmanHeapTest() {
        h = new HuffmanHeap();
    }
    
    @Test
    public void addingToHeap() {
        for (int i = 100; i >= 50; i--) {
            h.add(new HuffmanNode(i, i));
            assertEquals(i, h.peek().byteValue);
        }
        for (int i = 1; i < 50; i++) {
            h.add(new HuffmanNode(i, i));
            assertEquals(1, h.peek().byteValue);
        }
    }
    
    @Test
    public void pollingFromHeap() {
        for (int i = 100; i > 0; i--) {
            h.add(new HuffmanNode(i, i));
        }
        for (int i = 1; i <= 100; i++) {
            assertEquals(i, h.poll().byteValue);
        }
    }
}
