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
//        File file = new File("decoded.txt");
//        file.createNewFile();
//        FileOutputStream output = new FileOutputStream(file);
//        System.out.println(bytearray.length);
//        LZSS encoder = new LZSS();
//        ByteList encoded = encoder.encode(bytearray);
//        System.out.println(encoded.size());
//        ByteList decoded = encoder.decode(encoded);
//        byte[] juum = new byte[decoded.size()];
//        for (int i = 0; i < decoded.size(); i++) {
//            juum[i] = decoded.get(i);
//        }
//        output.write(juum);
        Huffman huf = new Huffman();
        System.out.println(huf.encode(bytearray).size());

    }
    
}
