/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import OKcompress.logic.LZSS;
import OKcompress.domain.IntegerQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogkuisma
 */
public class LZSSTest {
    byte[] input;
    LZSS encoder;
    
    public LZSSTest() throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream(new File("alice29.txt")); // 152 kB text file
        input = IOUtils.toByteArray(inputStream);
        encoder = new LZSS(12, 3);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void encodedOutputIsSmallerInSize() {
        byte[] encoded = encoder.encodeUsingBruteForce(input);
        assertTrue(encoded.length < input.length);
    }

    @Test
    public void queuesEncodeRight() {
        byte[] decoded = encoder.decode(encoder.encodeUsingQueues(input));
        assertEquals(input.length, decoded.length); // same size
        
        for (int i = 0; i < decoded.length; i++) {
            if (input[i] != decoded[i]) { // fail if the data doesn't match at any point
                fail();
            }
        }
    }
    
    @Test
    public void bruteForceEncodesRight() {
        byte[] decoded = encoder.decode(encoder.encodeUsingBruteForce(input));
        assertEquals(input.length, decoded.length); // same size
        
        for (int i = 0; i < decoded.length; i++) {
            if (input[i] != decoded[i]) { // fail if the data doesn't match at any point
                fail();
            }
        }
    }
}
