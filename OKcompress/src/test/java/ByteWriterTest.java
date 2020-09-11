/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import OKcompress.utils.ByteWriter;

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
    public void writeUncodedAddsZeroByteAndInput() {
        byte b = (byte) 10;
        writer.writeUncoded(b);
        ArrayList<Byte> bytes = writer.getByteArray();
        assertEquals(0, bytes.get(0).byteValue());
        assertEquals(10, bytes.get(1).byteValue());
    }
    
    @Test
    public void closeAddsZeroBits() {
        writer.writeBit((byte) 1);
        writer.close();
        
        writer.writeBit((byte) 1);
        writer.writeBit((byte) 1);
        writer.close();
        
        ArrayList<Byte> bytes = writer.getByteArray();
        assertEquals("10000000", Integer.toBinaryString(bytes.get(0) & 0xFF));
        assertEquals("11000000", Integer.toBinaryString(bytes.get(1) & 0xFF));
    }
    
    @Test
    public void writeBitOnlyFlushesAfterEightBits() {
        for (int i = 0; i < 7; i++) {
            writer.writeBit((byte) 1);
        }
        assertTrue(writer.getByteArray().isEmpty());

        writer.writeBit((byte) 1);
        assertTrue(writer.getByteArray().size() == 1);
    }

    @Test
    public void writeCodedFailsWithIllegalParameters() {
        RuntimeException offset = assertThrows(RuntimeException.class,
                () ->  writer.writeCoded(2048, 1) // offset and length values have to fit into 11 and 4 bits respectively
            );
        
        RuntimeException length = assertThrows(RuntimeException.class,
                () ->  writer.writeCoded(1, 16) // offset and length values have to fit into 11 and 4 bits respectively
            );
        
        assertTrue(offset.getMessage().contains("Offset input"));
        assertTrue(length.getMessage().contains("Length input"));
         
    }
}
