/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import OKcompress.logic.DeflateLite;
import OKcompress.domain.IntegerQueue;
import java.io.File;
import java.io.FileInputStream;
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
public class DeflateLiteTest {
    byte[] input;
    DeflateLite encoder;
    
    public DeflateLiteTest() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("alice29.txt")); // 152 kB text file
        input = IOUtils.toByteArray(inputStream);
        encoder = new DeflateLite();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
        
    @Test
    public void decodedOutputMatchesOriginalInput() {
        byte[] decoded = encoder.decode(encoder.encode(input));
        assertEquals(input.length, decoded.length); // same size
        
        for (int i = 0; i < decoded.length; i++) {
            if (input[i] != decoded[i]) { // fail if the data doesn't match at any point
                System.out.println("buu" + i);
                fail();
            }
        }
    } 
}
