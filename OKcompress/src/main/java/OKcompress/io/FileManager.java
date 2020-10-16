
package OKcompress.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author ogkuisma
 */
public class FileManager {
    
    public byte[] readFile(String filePath) throws IOException {
        FileInputStream input = new FileInputStream(new File(filePath));
        byte[] bytearray = IOUtils.toByteArray(input);
        return bytearray;
    }
    
    public void writeFile(String filePath, byte[] data) throws IOException {
        File original = new File(filePath);
        File newFile;
        if (original.isAbsolute()) {
            newFile = new File(original.getParent() + "/OKcompress");
        } else {
            newFile = new File("OKcompress");
        }
        newFile.mkdir();
        FileOutputStream output = new FileOutputStream(newFile.getAbsolutePath() + "/" + original.getName());
        output.write(data);
    }
    
}
