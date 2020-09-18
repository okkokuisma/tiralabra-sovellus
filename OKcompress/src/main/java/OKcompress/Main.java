package OKcompress;


import OKcompress.LZSS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;


/**
 *
 * @author ogkuisma
 */
public class Main {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(new File("test.txt"));
        byte[] bytearray = IOUtils.toByteArray(input);
//        File file = new File("decoded.txt");
//        file.createNewFile();
//        FileOutputStream output = new FileOutputStream(file);
//        System.out.println(bytearray.length);
//        LZSS encoder = new LZSS();
//        ArrayList<Byte> encoded = encoder.encode(bytearray);
//        System.out.println(encoded.size());
//        ArrayList<Byte> decoded = encoder.decode(encoded);
//        byte[] juum = new byte[encoded.size()];
//        for (int i = 0; i < encoded.size(); i++) {
//            juum[i] = encoded.get(i);
//        }
//        output.write(juum);
        Huffman huf = new Huffman();
        System.out.println(huf.encode(bytearray).size());
    }
    
}
