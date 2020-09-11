/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.ByteWriter;

/**
 *
 * @author ogkuisma
 */
public class ByteWriterTest {
    ByteWriter writer;
    
    public ByteWriterTest() {
        writer = new ByteWriter();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void writeCodedFailsWithIllegalParameters() {
        assertThrows(RuntimeException.class,
                () -> writer.writeCoded(2048, 16) // offset and length values have to fit into 11 and 4 bits respectively
            );
         
    }
}
