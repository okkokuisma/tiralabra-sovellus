/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import OKcompress.LZSS;
import OKcompress.domain.ByteList;
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
        FileInputStream inputStream = new FileInputStream(new File("alice29.txt")); // 12.9 kB text file
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
        ByteList encoded = encoder.encode(input);
        assertTrue(encoded.size() < input.length);
    }

    @Test
    public void queuesEncodeRight() {
        ByteList decoded = encoder.decode(encoder.encodeUsingQueues(input));
        assertEquals(input.length, decoded.size()); // same size
        
        for (int i = 0; i < decoded.size(); i++) {
            if (input[i] != decoded.get(i)) { // fail if the data doesn't match at any point
                fail();
            }
        }
    }
    
    @Test
    public void bruteForceEncodesRight() {
        ByteList decoded = encoder.decode(encoder.encode(input));
        assertEquals(input.length, decoded.size()); // same size
        
        for (int i = 0; i < decoded.size(); i++) {
            if (input[i] != decoded.get(i)) { // fail if the data doesn't match at any point
                fail();
            }
        }
    }
}
