/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import OKcompress.domain.IntegerQueue;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogkuisma
 */
public class IntegerQueueTest {
    IntegerQueue queue;
    static int[] a1;
    static int[] a2;
    
    public IntegerQueueTest() {
        queue = new IntegerQueue(100);
        int n = 1000000;
        a1 = new int[n];
        a2 = new int[n];
        Random r = new Random(1337);
        for (int i = 0; i < n; i++) {
            a1[i] = r.nextInt(1000000000)+1;
            if (i != 0) a2[i] = r.nextInt(i);
        }
    }
    
    @Test
    public void addingToBothEnds() {
        long sum = 0;
        for (int i = 1; i <= 500000; i++) {
            if (i%2 == 0) queue.addFirst(a1[i]);
            else queue.add(a1[i]);
            sum += queue.get(a2[i]);
        }
        if (sum == 249859746047147L) return;
        fail();
    }
    
    @Test
    public void removingLast() {
        for (int i = 0; i < 1000; i++) {
            if (i%2 == 0) queue.addFirst(i);
            else queue.add(i);
        }
        for (int i = 0; i < 500; i++) {
            assertTrue(queue.get(queue.size() - 1) % 2 != 0);
            queue.removeLast();
        }
        for (int i = 0; i < 500; i++) {
            assertTrue(queue.get(queue.size() - 1) % 2 == 0);
            queue.removeLast();
        }
        assertEquals(0, queue.size());
    }
    
    @Test
    public void getArray() {
        for (int i = 0; i < 1000; i++) {
            if (i%2 == 0) queue.addFirst(i);
            else queue.add(i);
        }
        
        int[] a = queue.getArray();
        
        assertEquals(1000, a.length);
        
        for (int i = 0; i < 1000; i++) {
            if (i < 500) {
                assertTrue(a[i] % 2 == 0);              
            } else {
                assertTrue(a[i] % 2 != 0);
            }
        }
    }
    
    @Test
    public void getBytes() {
        for (int i = 0; i < 1000; i++) {
            if (i%2 == 0) queue.addFirst(i);
            else queue.add(i);
        }
        
        byte[] a = queue.getBytes();
        
        assertEquals(1000, a.length);
        
        for (int i = 0; i < 1000; i++) {
            if (i < 500) {
                assertTrue(a[i] % 2 == 0);              
            } else {
                assertTrue(a[i] % 2 != 0);
            }
        }
    }
    
    @Test
    public void removingFromEmptyQueue() {
        assertTrue(queue.isEmpty());
        queue.removeLast();
        assertTrue(queue.size() == 0);
    }
    
}
