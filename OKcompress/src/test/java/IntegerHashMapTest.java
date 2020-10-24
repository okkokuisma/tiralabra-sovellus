/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import OKcompress.domain.IntegerHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogkuisma
 */
public class IntegerHashMapTest {
    private IntegerHashMap hm;
    
    public IntegerHashMapTest() {
        hm = new IntegerHashMap();
        for (int i = 0; i < 270; i++) {
            hm.put(i, i);
        }
    }
    
    @Test
    public void addingWithSameKey() {
        hm.put(1, 1);
        assertEquals(1, hm.get(1));
        hm.put(1, 2);
        assertEquals(2, hm.get(1));
    }
    
    @Test
    public void mapContainsAllKeys() {
        for (int i = 0; i < 270; i++) {
            assertTrue(hm.containsKey(i));
        }
        assertTrue(!hm.containsKey(270));
    }
    
    @Test
    public void rightValuesAreReturned() {
        for (int i = 0; i < 270; i++) {
            assertEquals(i, hm.get(i));
        }
        assertEquals(-1, hm.get(270));
    }
}
