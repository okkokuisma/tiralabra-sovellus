/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OKcompress.utils;

import OKcompress.domain.IntegerQueue;
import OKcompress.io.FileManager;
import OKcompress.logic.DeflateLite;
import OKcompress.logic.Huffman;
import OKcompress.logic.LZSS;
import java.io.IOException;

/**
 *
 * @author ogkuisma
 */
public class Tester {
    private LZSS lzss;
    private Huffman huffman;
    private DeflateLite deflate;
    
    public Tester() {
        lzss = new LZSS(11,4);
        huffman = new Huffman();
        deflate = new DeflateLite();
    }
    
    public void run() throws IOException {
        FileManager filem = new FileManager();
        byte[] input1 =  filem.readFile("test.txt");
        byte[] input2 =  filem.readFile("alice29.txt");
        byte[] input3 =  filem.readFile("world192.txt");
        byte[] input4 =  filem.readFile("bible.txt");

        // differences in compression rates between the algorithms with four input files of different sizes
        System.out.println("COMPRESSION RATES\n");
        System.out.println("12.9 kB text file:");
        byte[] lzssencoded = lzss.encodeUsingQueues(input1);
        System.out.println("LZSS: encoded file " + lzssencoded.length + " bytes, compression rate " + (double) lzssencoded.length / input1.length * 100);
        byte[] hufencoded = huffman.encode(input1);
        System.out.println("Huffman: encoded file " + hufencoded.length + " bytes, compression rate " + (double) hufencoded.length / input1.length * 100);
        byte[] defencoded = deflate.encode(input1);
        System.out.println("DeflateLite: encoded file " + defencoded.length + " bytes, compression rate " + (double) defencoded.length / input1.length * 100);    
        
        System.out.println("\n152.1 kB text file:");
        lzssencoded = lzss.encodeUsingQueues(input2);
        System.out.println("LZSS: encoded file " + lzssencoded.length + " bytes, compression rate " + (double) lzssencoded.length / input2.length * 100);
        hufencoded = huffman.encode(input2);
        System.out.println("Huffman: encoded file " + hufencoded.length + " bytes, compression rate " + (double) hufencoded.length / input2.length * 100);
        defencoded = deflate.encode(input2);
        System.out.println("DeflateLite: encoded file " + defencoded.length + " bytes, compression rate " + (double) defencoded.length / input2.length * 100);        
        
        System.out.println("\n2.5 MB text file:");
        lzssencoded = lzss.encodeUsingQueues(input3);
        System.out.println("LZSS: encoded file " + lzssencoded.length + " bytes, compression rate " + (double) lzssencoded.length / input3.length * 100);
        hufencoded = huffman.encode(input3);
        System.out.println("Huffman: encoded file " + hufencoded.length + " bytes, compression rate " + (double) hufencoded.length / input3.length * 100);
        defencoded = deflate.encode(input3);
        System.out.println("DeflateLite: encoded file " + defencoded.length + " bytes, compression rate " + (double) defencoded.length / input3.length * 100);        
        
        System.out.println("\n4.0 MB text file:");
        lzssencoded = lzss.encodeUsingQueues(input4);
        System.out.println("LZSS: encoded file " + lzssencoded.length + " bytes, compression rate " + (double) lzssencoded.length / input4.length * 100);
        hufencoded = huffman.encode(input4);
        System.out.println("Huffman: encoded file " + hufencoded.length + " bytes, compression rate " + (double) hufencoded.length / input4.length * 100);
        defencoded = deflate.encode(input4);
        System.out.println("DeflateLite: encoded file " + defencoded.length + " bytes, compression rate " + (double) defencoded.length / input4.length * 100);   
        System.out.println("");
        
        // differences in computing times between the algorithms
        System.out.println("COMPUTING TIMES (ENCODING)\n");
        long[] times = new long[10];
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            byte[] enc = lzss.encodeUsingBruteForce(input3);
            long finish = System.nanoTime();
            times[i] = finish - start;
        }
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += times[i];
        }
        System.out.println("LZSS (brute force) -- average time (seconds): " + (sum / (double)10) / 1e9 );

        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            byte[] enc = lzss.encodeUsingQueues(input3);
            long finish = System.nanoTime();
            times[i] = finish - start;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += times[i];
        }
        System.out.println("LZSS (queues) -- average time (seconds): " + (sum / (double)10) / 1e9 );
        
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            byte[] enc = huffman.encode(input3);
            long finish = System.nanoTime();
            times[i] = finish - start;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += times[i];
        }
        System.out.println("Huffman -- average time (seconds): " + (sum / (double)10) / 1e9 );
        
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            byte[] enc = deflate.encode(input3);
            long finish = System.nanoTime();
            times[i] = finish - start;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += times[i];
        }
        System.out.println("DeflateLite -- average time (seconds): " + (sum / (double)10) / 1e9 );
        System.out.println("");
        
        System.out.println("COMPUTING TIMES (DECODING)\n");
        byte[] a = lzss.encodeUsingQueues(input3);
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            lzss.decode(a);
            long finish = System.nanoTime();
            times[i] = finish - start;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += times[i];
        }
        System.out.println("LZSS -- average time (seconds): " + (sum / (double)10) / 1e9 );
        
        a = huffman.encode(input3);
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            huffman.decode(a);
            long finish = System.nanoTime();
            times[i] = finish - start;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += times[i];
        }
        System.out.println("Huffman -- average time (seconds): " + (sum / (double)10) / 1e9 );
        
        a = deflate.encode(input3);
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            deflate.decode(a);
            long finish = System.nanoTime();
            times[i] = finish - start;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += times[i];
        }
        System.out.println("DeflateLite -- average time (seconds): " + (sum / (double)10) / 1e9 );
        
        // dirrerences in compression rates with different dictionary sizes, number of length bits set as 4
        System.out.println("DIFFERENCES IN LZSS COMPRESSION RATES BETWEEN DIFFERENT DICTIONARY SIZES\n");
        for (int i = 8; i < 17; i++) {
            LZSS encoder = new LZSS(i, 4);
            byte[] enc = encoder.encodeUsingQueues(input2);
            double rate = (double)enc.length / input2.length * 100;
            int dic = (int) Math.pow(2, i);
            System.out.println("Number of offset bits " + i + ", dictionary size " + dic + " -- encoded file " + enc.length + ", compression rate " + rate);
        }
        System.out.println("");
        
        // dirrerences in compression rates with different maximum match lengths, number of offset bits set as 11
        System.out.println("DIFFERENCES IN LZSS COMPRESSION RATES BETWEEN DIFFERENT MAXIMUM MATCH LENGTHS\n");
        for (int i = 2; i < 10; i++) {
            LZSS encoder = new LZSS(11, i);
            byte[] enc = encoder.encodeUsingQueues(input2);
            double rate = (double)enc.length / input2.length * 100;
            int maxLength = (int) Math.pow(2, i) - 1;
            System.out.println("Number of length bits " + i + ", max match length " + maxLength + " -- encoded file " + enc.length + ", compression rate " + rate);
        }
        System.out.println("");
        
        // differences in computing time between brute force and queues with different dictionary sizes, number of length bits set as 4
        System.out.println("DIFFERENCES IN LZSS COMPUTING TIMES BETWEEN BRUTE FORCE AND QUEUES\n");
        for (int j = 8; j < 17; j++) {
            LZSS encoder = new LZSS(j, 4);
            for (int i = 0; i < 10; i++) {
                long start = System.nanoTime();
                byte[] enc = encoder.encodeUsingBruteForce(input2);
                long finish = System.nanoTime();
                times[i] = finish - start;
            }
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += times[i];
            }
            int dic = (int) Math.pow(2, j);
            System.out.println("Number of offset bits " + j + ", dictionary size " + dic + " -- average time (seconds): " + (sum / (double)10) / 1e9);
        }       
    }
    
}
