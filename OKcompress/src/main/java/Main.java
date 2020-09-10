
import io.LZSS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        FileInputStream input = new FileInputStream(new File("testi.txt"));
        byte[] bytearray = IOUtils.toByteArray(input);
        LZSS encoder = new LZSS();
        ArrayList<Byte> encoded = encoder.encode(bytearray);
        ArrayList<Byte> decoded = encoder.decode(encoded);
        for (byte tavu : bytearray) {
            System.out.println(tavu);
        }
        System.out.println("--");
        for (byte tavu : encoded) {
            System.out.println(tavu);
        }
        System.out.println("--");
        for (byte tavu : decoded) {
            System.out.println(tavu);
        }
        
    }
    
}
