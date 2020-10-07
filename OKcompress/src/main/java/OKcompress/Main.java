package OKcompress;


import OKcompress.domain.IntegerQueue;
import OKcompress.utils.BitReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
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
//        System.out.println("alku: " + bytearray.length);
//        LZSS encoder = new LZSS(12, 3);
//        IntegerQueue lz = encoder.encodeUsingQueues(bytearray);
//        System.out.println("LZSS: " + lz.size());
//        byte[] decoded = encoder.decode(lz).getBytes();
        String test = "go go gophers";
//        Huffman huf = new Huffman();
////        
//        byte[] dec = huf.encode(bytearray).getArray();
//        System.out.println("huffman: " + dec.length);
//        byte[] juum = huf.decode(dec).getArray();

//        for (int i = 0; i < bytearray.length; i++) {
//            if (bytearray[i] != decoded[i]) {
//                System.out.println("buu" + i);
//                break;
//            }
//        }
//        output.write(juum);
        LZSS encoder = new LZSS(12, 4);
//        long[] times = new long[10];
//        for (int i = 0; i < 10; i++) {
//            long start = System.nanoTime();
//            IntegerQueue enc = encoder.encodeUsingQueues(bytearray);
//            long finish = System.nanoTime();
//            times[i] = finish - start;
//        }
//        long sum = 0;
//        for (int i = 0; i < 10; i++) {
//            sum += times[i];
//        }
//        System.out.println("average (): " + (sum / (double)10) / 1e9);
        System.out.println(bytearray.length);
        DeflateLite def = new DeflateLite();
        byte[] output = def.encode(bytearray);
        System.out.println(output.length);
        byte[] dec = def.decode(output);
        System.out.println(dec.length);
        byte[] lzssenc = encoder.encodeUsingQueues(bytearray).getBytes();
        for (int i = 0; i < lzssenc.length; i++) {
            if (dec[i] != lzssenc[i]) {
                System.out.println("indeksi: " + i);
                System.out.println("def: " + dec[i]);
                System.out.println("og: " + (0xFF & lzssenc[i]));
                break;
            }
        }
    }
}
