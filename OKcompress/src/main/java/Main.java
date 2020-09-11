
import io.LZSS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        File file = new File("decoded.txt");
        file.createNewFile();
        FileOutputStream output = new FileOutputStream(file);
        byte[] bytearray = IOUtils.toByteArray(input);
        System.out.println(bytearray.length);
        LZSS encoder = new LZSS();
        ArrayList<Byte> encoded = encoder.encode(bytearray);
        ArrayList<Byte> decoded = encoder.decode(encoded);
        System.out.println(encoded.size());
        for (int i = 0; i < decoded.size(); i++) {
            if (bytearray[i] != decoded.get(i)) {
                System.out.println("buu");
                break;
            }
        }
        byte[] juum = new byte[decoded.size()];
        for (int i = 0; i < decoded.size(); i++) {
            juum[i] = decoded.get(i);
        }
        output.write(juum);

    }
    
}
