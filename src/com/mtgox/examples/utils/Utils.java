package com.mtgox.examples.utils;
 
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mtgox.api.ApiKeys;

/**
 *
 * @author advanced
 */
public class Utils {
     
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
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
                 
        return fileContent.toString();
    }
    

    public static void initSSL()
    {        
           
        // SSL Certificates  trustStore ----------------------------------------
        //Set the SSL certificate for mtgox - Read up on Java Trust store. 
        System.setProperty("javax.net.ssl.trustStore","res/ssl/mtgox.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","h4rdc0r_"); //I encripted the jks file using this pwd
        //System.setProperty("javax.net.debug","ssl"); //Uncomment for debugging SSL errors  
        
    }
    
     //readApiKeysFromFile
     public static ApiKeys readApiKeys(String pathToJsonFile) {
        //see https://code.google.com/p/json-simple/wiki/DecodingExamples
        JSONParser parser=new JSONParser();
        ApiKeys apiKeys = null;
        String apiStr = Utils.readFromFile(pathToJsonFile);
        try {
            JSONObject obj2=(JSONObject)(parser.parse(apiStr));
            apiKeys= new ApiKeys((String)obj2.get("mtgox_secret_key"), (String)obj2.get("mtgox_api_key"));
        } catch (ParseException ex) {
            System.err.println(ex);
        }
        return apiKeys;    
    }
     
}