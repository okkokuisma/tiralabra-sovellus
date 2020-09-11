/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import io.LZSS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
        FileInputStream inputStream = new FileInputStream(new File("test.txt")); // 12.9 kB text file
        input = IOUtils.toByteArray(inputStream);
        encoder = new LZSS();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void decodedOutputMatchesOriginalInput() {
        ArrayList<Byte> decoded = encoder.decode(encoder.encode(input));
        assertEquals(input.length, decoded.size()); // same size
        
        for (int i = 0; i < decoded.size(); i++) {
            if (input[i] != decoded.get(i)) { // fail if the data doesn't match
                fail();
            }
        }
    }
}
