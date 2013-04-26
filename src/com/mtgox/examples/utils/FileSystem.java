package com.mtgox.examples.utils;
 
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author advanced
 */
public class FileSystem {
     
    public static String readFromFile(String path) {
         
        File file = new File(path);
         
        StringBuilder fileContent = new StringBuilder();
        BufferedReader bufferedReader = null;
         
        try {
 
            bufferedReader = new BufferedReader(new FileReader(file));
             
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                fileContent.append(text);
            }
 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
                 
        return fileContent.toString();
    }
     
}