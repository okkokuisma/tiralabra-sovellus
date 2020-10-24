package OKcompress;


import OKcompress.domain.IntegerQueue;
import OKcompress.io.FileManager;
import OKcompress.logic.DeflateLite;
import OKcompress.logic.Huffman;
import OKcompress.logic.LZSS;
import OKcompress.ui.OKcompressUI;
import OKcompress.utils.Tester;
import java.io.IOException;
import javafx.application.Application;


public class Main {  
    public static void main(String[] args) throws IOException { 
//        Application.launch(OKcompressUI.class);
        Tester test = new Tester();
        test.run();
    }
}
