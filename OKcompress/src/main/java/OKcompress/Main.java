package OKcompress;


import OKcompress.domain.ByteList;
import OKcompress.utils.BitReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;


/**
 *
 * @author ogkuisma
 */
public class Main {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(new File("alice29.txt"));
        byte[] bytearray = IOUtils.toByteArray(input);
//        File file = new File("decoded.txt");
//        file.createNewFile();
//        FileOutputStream output = new FileOutputStream(file);
//        System.out.println(bytearray.length);
        System.out.println("alku: " + bytearray.length);
        LZSS encoder = new LZSS();
        ByteList lz = encoder.encode(bytearray);
        System.out.println("LZSS: " + lz.size());
        byte[] decoded = encoder.decode(lz).getArray();
//        String test = "go go gophers";
        Huffman huf = new Huffman();
//        
        byte[] dec = huf.encode(bytearray).getArray();
        System.out.println("huffman: " + dec.length);
//        byte[] juum = huf.decode(dec).getArray();

        for (int i = 0; i < bytearray.length; i++) {
            if (bytearray[i] != decoded[i]) {
                System.out.println("buu" + i);
                break;
            }
        }
//        output.write(juum);
    }
}
