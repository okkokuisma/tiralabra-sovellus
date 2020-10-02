/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import OKcompress.domain.ByteList;
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
    public void writeUncodedAddsZeroBitAndInput() {
        byte b = (byte) 65;
        writer.writeLZSSUncoded(b);
        writer.close();
        ByteList bytes = writer.getBytes();
        assertEquals(32, bytes.get(0).byteValue()); // byte b shifts to right (b >> 1)
        assertEquals(-128, bytes.get(1).byteValue()); // last 1 bit of byte b leaks to next byte
    }
    
    @Test
    public void closeAddsZeroBits() {
        writer.writeBit((byte) 1);
        writer.close();
        
        writer.writeBit((byte) 1);
        writer.writeBit((byte) 1);
        writer.close();
        
        ByteList bytes = writer.getBytes();
        assertEquals(-128, bytes.get(0).byteValue());
        assertEquals(-64, bytes.get(1).byteValue());
    }
    
    @Test
    public void writeBitOnlyFlushesAfterEightBits() {
        for (int i = 0; i < 7; i++) {
            writer.writeBit((byte) 1);
        }
        assertTrue(writer.getBytes().isEmpty());

        writer.writeBit((byte) 1);
        assertTrue(writer.getBytes().size() == 1);
    }

//    @Test
//    public void writeCodedFailsWithIllegalParameters() {
//        RuntimeException offset = assertThrows(RuntimeException.class,
//                () ->  writer.writeLZSSCoded(2048, 1) // offset and length values have to fit into 11 and 4 bits respectively
//            );
//        
//        RuntimeException length = assertThrows(RuntimeException.class,
//                () ->  writer.writeLZSSCoded(1, 16)
//            );
//        
//        assertTrue(offset.getMessage().contains("Offset input"));
//        assertTrue(length.getMessage().contains("Length input"));
//         
//    }
}
