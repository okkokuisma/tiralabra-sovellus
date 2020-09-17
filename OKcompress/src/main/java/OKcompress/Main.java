package OKcompress;


import OKcompress.LZSS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
//        for (int i = 0; i < decoded.size(); i++) {
//            if (bytearray[i] != decoded.get(i)) {
//                System.out.println("buu");
//                break;
//            }
//        }
//        byte[] juum = new byte[encoded.size()];
//        for (int i = 0; i < encoded.size(); i++) {
//            juum[i] = encoded.get(i);
//        }
//        output.write(juum);
        Huffman huf = new Huffman();
        System.out.println(huf.encode(bytearray).size());
    }
    
}
