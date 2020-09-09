
import io.LZSS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        encoder.encode(bytearray);
    }
    
}
