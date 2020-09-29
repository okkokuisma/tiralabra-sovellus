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
        FileInputStream input = new FileInputStream(new File("test.txt"));
        byte[] bytearray = IOUtils.toByteArray(input);
        File file = new File("decoded.txt");
        file.createNewFile();
        FileOutputStream output = new FileOutputStream(file);
//        System.out.println(bytearray.length);
//        LZSS encoder = new LZSS();
//        ByteList encoded = encoder.encode(bytearray);
//        System.out.println(encoded.size());
//        ByteList decoded = encoder.decode(encoded);
//        byte[] juum = new byte[decoded.size()];
//        for (int i = 0; i < decoded.size(); i++) {
//            juum[i] = decoded.get(i);
//        }
        String test = "go go gophers";
        Huffman huf = new Huffman();
        System.out.println("alku: " + bytearray.length);
        byte[] dec = huf.encode(bytearray).getArray();
        System.out.println("koodattu: " + dec.length);
        byte[] juum = huf.decode(dec).getArray();
        System.out.println("loppu: " + juum.length);
        for (int i = 0; i < bytearray.length; i++) {
            if (bytearray[i] != juum[i]) {
                System.out.println("buu");
                break;
            }
        }
        System.out.println(0xFF & (byte) 254);
//        output.write(juum);
    }
}
