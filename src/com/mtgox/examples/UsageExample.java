
package com.mtgox.examples;

import com.mtgox.api.ApiKeys;
import com.mtgox.api.MtGox;
import com.mtgox.api.TradeInterface;
import com.mtgox.examples.utils.FileSystem;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * https://github.com/adv0r/mtgox-apiv2-java
 * @author adv0r <leg@lize.it>
 * MIT License (see LICENSE.md)
 * Run examples at your own risk. Only partially implemented and untested.
 * Consider donations @ 1N7XxSvek1xVnWEBFGa5sHn1NhtDdMhkA7
 */
public class UsageExample {
    
    public static void main(String args[])
    {
      
        initSSL(); //Setup the SSL certificate to interact with mtgox over secure http.
        
        
        //Read api Keys---------------------------------------------------------------------------------
        //from the JSON file located in res/api-keys.json
        //ApiKeys keys = readApiKeys("res/api-keys.json");
        
        //or simply create the keys passing them to the constructor
        ApiKeys keys = new ApiKeys("your-secret-key",
                "your-api-key"); 
              
           
        //Library Usage Examples -----------------------------------------------------------------------
        //Create the interface for trading on Mtgox, passing the apikeys object
        MtGox trade = new MtGox(keys);
        //trade.setPrintHTTPResponse(true); //Uncomment this line if you want to read the JSON HTTP response
        
        //Get the current balance in USD,EUR, and BTC---------------------------------------------------
        double[] balance = trade.getBalance(); //The answer consist of an array of three elements
        
        double balanceBTC = balance[0]; //balance in BTC
        double balanceUSD = balance[1]; //balance in USD
        double balanceEUR = balance[2]; //balance in EUR
    
        System.out.println("Current account balance : \n" +
                "   "+balanceBTC+" BTC\n"+
                "   "+balanceUSD+" $\n"+
                "   "+balanceEUR+" €");
        
       
        
        //Get the current trading engine lag (as a String)---------------------------------------------
        System.out.println("Current Lag : "+ trade.getLag());
        
        
        //Get the current price of a bitcoin using the ticker fast-------------------------------------
        double lastPriceUSD = trade.getLastPriceUSD();
        double lastPriceEUR = trade.getLastPriceEUR();
        System.out.println("Current price of 1 BTC : \n" +
                "   "+lastPriceUSD+" $\n"+
                "   "+lastPriceEUR+" €");
        
        
        //Buy 0.1 BTC at market price -----------------------------------------------------------------
        //String buyResult = trade.buyBTC(0.1); //If you uncomment this, be sure of what you are doing
        //System.out.println(buyResult);
        
        //Sell 0.1 BTC at market price -----------------------------------------------------------------
        //String sellResult = trade.sellBTC(0.1); //If you uncomment this, be sure of what you are doing
        //System.out.println(sellResult);
        
        
        //Finally, if you are using this library in some of your project consider-----------------------
        //giving me a small donation by simpling uncommenting the line below  :)
        //withdraw 0.1 BTC from your mtgox to my wallet
        //trade.withdrawBTC(0.1, "1N7XxSvek1xVnWEBFGa5sHn1NhtDdMhkA7"); //thanks! ;)
        
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
        String apiStr = FileSystem.readFromFile(pathToJsonFile);
        try {
            JSONObject obj2=(JSONObject)(parser.parse(apiStr));
            apiKeys= new ApiKeys((String)obj2.get("mtgox_secret_key"), (String)obj2.get("mtgox_api_key"));
        } catch (ParseException ex) {
            System.err.println(ex);
        }
        return apiKeys;    
    }
}
