/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OKcompress.utils;

import OKcompress.logic.DeflateLite;
import OKcompress.logic.Huffman;
import OKcompress.logic.LZSS;
import OKcompress.io.FileManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ogkuisma
 */
public class Service {
    Huffman huffman;
    LZSS lzss;
    DeflateLite deflate;
    FileManager fileManager;
    
    public Service() {
        huffman = new Huffman();
        lzss = new LZSS(11, 4);
        deflate = new DeflateLite();
        fileManager = new FileManager();
    }
    
    public void huffmanEncode(String filePath) {
        try {
            byte[] data = fileManager.readFile(filePath);
            byte[] encoded = huffman.encode(data);
            fileManager.writeFile(filePath, encoded);
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void lzssEncode(String filePath) {
        try {
            byte[] data = fileManager.readFile(filePath);
            byte[] encoded = lzss.encodeUsingQueues(data);
            fileManager.writeFile(filePath, encoded);
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deflateLiteEncode(String filePath) {
        try {
            byte[] data = fileManager.readFile(filePath);
            byte[] encoded = deflate.encode(data);
            fileManager.writeFile(filePath, encoded);
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void decode(String filePath) {
        try {
            byte[] data = fileManager.readFile(filePath);
            BitReader reader = new BitReader(data);
            int encodedWith = reader.readInt(2);
            byte[] decoded;
            if (encodedWith == 1) {
                decoded = deflate.decode(data);
            } else if (encodedWith == 2) {
                decoded = huffman.decode(data);
            } else { 
                decoded = lzss.decode(data);
            }
            fileManager.writeFile(filePath, decoded);
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
