
import OKcompress.utils.BitReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ogkuisma
 */
public class BitReaderTest {
    BitReader reader;
    byte[] input;
    
    public BitReaderTest() {
        input = new byte[]{65, 65, 65, 65};
        reader = new BitReader(input);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void readIntReturnsRightValue() {
        assertEquals(65, reader.readInt(8));
        assertEquals(522, reader.readInt(11));
        assertEquals(1, reader.readInt(5));
    }
    
    @Test
    public void readBitReturnsRightValue() {
        for (int i = 0; i < 8; i++) {
            int bit = reader.readBit();
            if ((i == 1 || i == 7) & bit != 1) { // 2nd and 8th bits are 1
                fail();
            } else if (!(i == 1 || i == 7) & bit != 0) {
                fail();
            }
        }
    }
    
    @Test
    public void readByteReturnsRightValue() {
        assertEquals(65, reader.readByte());
        reader.readBit(); // remove a bit
        assertEquals(-126, reader.readByte());
        for (int i = 0; i < 4; i++) { // remove 4 bits
            reader.readBit();
        }
        assertEquals(40, reader.readByte());
    }
}
