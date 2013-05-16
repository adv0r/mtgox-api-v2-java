/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtgox.api;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * https://github.com/adv0r/mtgox-apiv2-java
 * @author adv0r <leg@lize.it>
 * MIT License (see LICENSE.md)
 * Implementation of MtGox Api V2
 * Consider donations @ 1N7XxSvek1xVnWEBFGa5sHn1NhtDdMhkA7
 * unofficial documentation by nitrous https://bitbucket.org/nitrous/mtgox-api/overview
 */
public class MtGox implements TradeInterface{
	
public enum Currency {BTC, USD, GBP, EUR, JPY, AUD, CAD, CHF, CNY, DKK, HKD, PLN, RUB, SEK, SGD, THB};
    
private ApiKeys keys;


private final HashMap<Currency, Integer> devisionFactors;


private final double MIN_ORDER = 0.01; //BTC

private final String API_BASE_URL = "https://data.mtgox.com/api/2/";

//Paths
private final String API_GET_INFO = "MONEY/INFO";
private final String API_TICKER_USD = "BTCUSD/MONEY/TICKER";
private final String API_TICKER_EUR = "BTCEUR/MONEY/TICKER";
private final String API_TICKER_FAST_USD = "BTCUSD/MONEY/TICKER_FAST"; 
private final String API_TICKER_FAST_EUR = "BTCEUR/MONEY/TICKER_FAST"; 

private final String API_WITHDRAW = "MONEY/BITCOIN/SEND_SIMPLE";
private final String API_LAG = "MONEY/ORDER/LAG";
private final String API_ADD_ORDER = "BTCUSD/MONEY/ORDER/ADD";

private final String SIGN_HASH_FUNCTION = "HmacSHA512";
private final String ENCODING = "UTF-8";

private boolean printHttpResponse ;

  public MtGox(ApiKeys keys) {
        this.keys = keys;
        printHttpResponse = false;
        // set division Factors
        devisionFactors = new HashMap<Currency, Integer>();
        devisionFactors.put(Currency.BTC, 100000000);
        devisionFactors.put(Currency.USD, 100000);
        devisionFactors.put(Currency.GBP, 100000);
        devisionFactors.put(Currency.EUR, 100000);
        devisionFactors.put(Currency.JPY, 1000);
        devisionFactors.put(Currency.AUD, 100000);
        devisionFactors.put(Currency.CAD, 100000);
        devisionFactors.put(Currency.CHF, 100000);
        devisionFactors.put(Currency.CNY, 100000);
        devisionFactors.put(Currency.DKK, 100000);
        devisionFactors.put(Currency.HKD, 100000);
        devisionFactors.put(Currency.PLN, 100000);
        devisionFactors.put(Currency.RUB, 100000);
        devisionFactors.put(Currency.SEK, 1000);
        devisionFactors.put(Currency.SGD, 100000);
        devisionFactors.put(Currency.THB, 100000);
        
    }
  
  public void setPrintHTTPResponse(boolean resp)
    {
        this.printHttpResponse = resp;
    }
  
    @Override
    public String getLag()  {        
        String urlPath = API_LAG;
        HashMap<String, String> query_args = new HashMap<>();
         /*Params
         * 
         */
        String queryResult = query(urlPath, query_args);
         /*Sample result
         * the lag in milliseconds
         */
         JSONParser parser=new JSONParser();
         String lag="";
         try {
            JSONObject httpAnswerJson=(JSONObject)(parser.parse(queryResult));
            JSONObject dataJson = (JSONObject)httpAnswerJson.get("data");
            lag = (String)dataJson.get("lag_text");                      
         } catch (ParseException ex) {
            Logger.getLogger(MtGox.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return lag;
    }

    @Override
    public String withdrawBTC(double amount, String dest_address) {  //TODO
        String urlPath = API_WITHDRAW;
        HashMap<String, String> query_args = new HashMap<>();
        /*Params
         * address : Target bitcoin address
         * amount_int : Amount of bitcoins to withdraw
         * fee_int : Fee amount to be added to transaction (optional), maximum 0.01 BTC
         * no_instant : Setting this parameter to 1 will prevent transaction from being processed internally, and force usage of the bitcoin blockchain even if receipient is also on the system
         * green : Setting this parameter to 1 will cause the TX to use MtGox’s green address
         */
        query_args.put("amount_int", Long.toString(Math.round(amount*devisionFactors.get(Currency.BTC))));
        query_args.put("address",dest_address);
        String queryResult = query(urlPath, query_args);
        

         /*Sample result
         * On success, this method will return the transaction id (in offser trx ) which will contain either the bitcoin transaction id as hexadecimal or a UUID value in case of internal transfer.
         */
        
         JSONParser parser=new JSONParser();
         try {
            JSONObject obj2=(JSONObject)(parser.parse(queryResult));
            //JSONObject data = (JSONObject)obj2.get("data"); //TODO
  
            
         } catch (ParseException ex) {
            Logger.getLogger(MtGox.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ""; //TODO Edit
    }

    @Override
    public String sellBTC(double amount) {
       return placeOrder("sell", Math.round(amount*devisionFactors.get(Currency.BTC)));
    }

    @Override
    public String buyBTC(double amount) {
       return placeOrder("buy", Math.round(amount*devisionFactors.get(Currency.BTC)));
    }
    
     public String placeOrder(String type, long amount_int) {
         
        String toReturn = "";
        String result = "" ;
        String data= "";
        String urlPath = API_ADD_ORDER;
        HashMap<String, String> query_args = new HashMap<>();
        /*Params
         * type : {ask (sell) | bid(buy) }
         * amount_int : amount of BTC to buy or sell, as an integer
         * price_int : The price per bitcoin in the auxiliary currency, as an integer, optional if you wish to trade at the market price
         */
        query_args.put("amount_int",Long.toString(amount_int));
        if (type.equals("sell"))
           query_args.put("type", "ask");
        else 
           query_args.put("type", "bid");
        
        String queryResult = query(urlPath, query_args); 
         /*Sample result
         * {"result":"success","data":"abc123-def45-.."} 
         */
         JSONParser parser=new JSONParser();
         try {
            JSONObject obj2=(JSONObject)(parser.parse(queryResult));
            result = (String)obj2.get("result");
            data = (String)obj2.get("data");

            //lastPriceArray[0] = (Double)obj2.get("last"); //USD
  
            
         } catch (ParseException ex) {
            Logger.getLogger(MtGox.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(result.equals("success"))
        {
            toReturn="executed : " +data; 
        }
        else 
            toReturn="not executed : " +data; //TODO test this branch
        
        return toReturn; //TODO change
    }

    @Override
    public double[] getBalance() {
        String urlPath = API_GET_INFO;
        HashMap<String, String> query_args = new HashMap<>();
        
        /*Params
         * 
         */
        double[] balanceArray = new double[3];

        
        
        String queryResult = query(urlPath, query_args); 
         /*Sample result
         * {
         *   "data": {
         *       "Created": "yyyy-mm-dd hh:mm:ss",
         *       "Id": "abc123",
         *       "Index": "123",
         *       "Language": "en_US",
         *       "Last_Login": "yyyy-mm-dd hh:mm:ss",
         *       "Login": "username",
         *       "Monthly_Volume":                   **Currency Object**,
         *       "Trade_Fee": 0.6,
         *       "Rights": ['deposit', 'get_info', 'merchant', 'trade', 'withdraw'],
         *       "Wallets": {
         *           "BTC": {
         *               "Balance":                  **Currency Object**,
         *               "Daily_Withdraw_Limit":     **Currency Object**,
         *               "Max_Withdraw":             **Currency Object**,
         *               "Monthly_Withdraw_Limit": null,
         *               "Open_Orders":              **Currency Object**,
         *               "Operations": 1,
         *           },
         *           "USD": {
         *               "Balance":                  **Currency Object**,
         *               "Daily_Withdraw_Limit":     **Currency Object**,
         *               "Max_Withdraw":             **Currency Object**,
         *               "Monthly_Withdraw_Limit":   **Currency Object**,
         *               "Open_Orders":              **Currency Object**,
         *               "Operations": 0,
         *           },
         *           "JPY":{...}, "EUR":{...},
         *           // etc, depends what wallets you have
         *       },
         *   },
         *   "result": "success"
         * }
         */
         JSONParser parser=new JSONParser();
         try {
            JSONObject httpAnswerJson=(JSONObject)(parser.parse(queryResult));
            JSONObject dataJson = (JSONObject)httpAnswerJson.get("data");  
            JSONObject walletsJson = (JSONObject)dataJson.get("Wallets"); 
            
            JSONObject BTCwalletJson = (JSONObject)((JSONObject)walletsJson.get("BTC")).get("Balance");  
        
            String BTCBalance = (String)BTCwalletJson.get("value");
                      
            boolean hasDollars = true;
            boolean hasEuros = true;
            JSONObject USDwalletJson,EURwalletJson;
            String USDBalance ="" , EURBalance ="";

            try{
                 USDwalletJson = (JSONObject)((JSONObject)walletsJson.get("USD")).get("Balance"); 
                 USDBalance = (String)USDwalletJson.get("value");
            }
            catch (Exception e)
            {
                hasDollars = false;
            }
            
            try{
                 EURwalletJson = (JSONObject)((JSONObject)walletsJson.get("EUR")).get("Balance");
                 EURBalance = (String)EURwalletJson.get("value");
            }
            catch (Exception e)
            {
                hasEuros = false;  
            }
            
            balanceArray[0] = Double.parseDouble(BTCBalance); //BTC
            
            if(hasDollars)
                balanceArray[1] = Double.parseDouble(USDBalance); //USD
            else 
                balanceArray[1] = -1; //Account does not have USD wallet

            if(hasEuros)
                balanceArray[2] = Double.parseDouble(EURBalance); //EUR
            else 
                balanceArray[2] = -1; //Account does not have EUR wallet

            
         } catch (ParseException ex) {
            Logger.getLogger(MtGox.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return balanceArray;
    }

    
    public String query(String path, HashMap<String, String> args) {
        GoxService query = new GoxService(path, args, keys);
        String queryResult = query.executeQuery();
        return queryResult;
        //TODO should be done by a different thread ...
    }
    

    public double getLastPrice(Currency cur) {    
  
        String urlPath="";
        long divideFactor;
        switch (cur) {
        case USD:
            urlPath = API_TICKER_FAST_USD ;
            divideFactor = devisionFactors.get(cur);
            break;
        case EUR:
            urlPath = API_TICKER_FAST_EUR ; //TODO When they will fix it change to ticker fast!! It is not working properly today 17Apr2013
            divideFactor = devisionFactors.get(cur);
            break;
        default:
            throw new UnsupportedOperationException("MTGOX API ERROR: Currency - "+cur.toString()+ " - Not supported yet.");
        }
        HashMap<String, String> query_args = new HashMap<>();
        
        /*Params :
        * No params required
        */
        String queryResult = query(urlPath, query_args);

         /* Result sample :
         *{
         *   "result":"success",
         *   "data": {
         *       "high":       **Currency Object - USD**,
         *       "low":        **Currency Object - USD**,
         *       "avg":        **Currency Object - USD**,
         *       "vwap":       **Currency Object - USD**,
         *       "vol":        **Currency Object - BTC**,
         *       "last_local": **Currency Object - USD**,
         *       "last_orig":  **Currency Object - ???**,
         *       "last_all":   **Currency Object - USD**,
         *       "last":       **Currency Object - USD**,
         *       "buy":        **Currency Object - USD**,
         *       "sell":       **Currency Object - USD**,
         *       "now":        "1364689759572564"
         *   }
         *}
         */
        JSONParser parser=new JSONParser();
        double last=0;
        try {
            JSONObject httpAnswerJson=(JSONObject)(parser.parse(queryResult));
            JSONObject dataJson = (JSONObject)httpAnswerJson.get("data");
            JSONObject lastJson = (JSONObject)dataJson.get("last");
            String last_String = (String)lastJson.get("value");
            last = Double.parseDouble(last_String);
            
         } catch (ParseException ex) {
            Logger.getLogger(MtGox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return last;
    }
   
    private class GoxService  {
        protected String path;
        protected HashMap args;
        protected ApiKeys keys;
        

        public GoxService(String path,HashMap<String, String> args, ApiKeys keys)
        {
            this.path = path;
            this.args = args;
            this.keys = keys;
        }
        
    //Build the query string given a set of query parameters
    private String buildQueryString(HashMap<String, String> args) {
        String result = new String();
        for (String hashkey : args.keySet()) {
            if (result.length() > 0) result += '&';
            try {
                result += URLEncoder.encode(hashkey, ENCODING) + "="
                        + URLEncoder.encode(args.get(hashkey), ENCODING);
            } catch (Exception ex) {
                Logger.getLogger(MtGox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    private String signRequest(String secret, String hash_data) {
       String signature = "";
        try{
          Mac mac = Mac.getInstance(SIGN_HASH_FUNCTION);
          SecretKeySpec secret_spec = new SecretKeySpec(Base64.decodeBase64(secret), SIGN_HASH_FUNCTION);
          mac.init(secret_spec);
          signature = Base64.encodeBase64String(mac.doFinal(hash_data.getBytes()));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e){
          Logger.getLogger(MtGox.class.getName()).log(Level.SEVERE, null, e);            
        }
        return signature;
    }
    
   

    private String executeQuery() {
                        String answer = "";
                        boolean httpError = false;
                        HttpsURLConnection connection=null;
                        String nonce = String.valueOf(System.currentTimeMillis())+"000";
                        try {
                            // add nonce and build arg list
                            args.put("nonce", nonce);     
                            String post_data= buildQueryString(args);
                            String hash_data = path + "\0" + post_data; //Should be correct

                            // args signature with apache cryptografic tools
                            String signature = signRequest(keys.getPrivateKey(), hash_data);

                            // build URL
                            URL queryUrl = new URL(API_BASE_URL + path); 

                            // create and setup a HTTP connection
                            connection = (HttpsURLConnection)queryUrl.openConnection();

                            connection.setRequestMethod("POST"); 

                            connection.setRequestProperty("User-Agent", "Advanced-java-client API v2");
                            connection.setRequestProperty("Rest-Key", keys.getApiKey());
                            connection.setRequestProperty("Rest-Sign", signature.replaceAll("\n", ""));

                            connection.setDoOutput(true);
                            connection.setDoInput(true);

                            //Read the response

                            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                            os.writeBytes(post_data);
                            os.close();

                            BufferedReader br = null;
                            boolean toLog=false; 
                            if (connection.getResponseCode() >= 400) {
                                httpError = true;//TODO , if HTTP error, do something else with output!
                                br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
                                toLog = true;
                                }
                            else 
                                br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

                            String output;

                            if(httpError)
                                System.err.println("Post Data: "+post_data);
                            if (printHttpResponse)
                                System.out.println("Query to :" + path + " , HTTP response : \n"); //do not log unless is error > 400
                            while ((output = br.readLine()) != null) {
                                        if(printHttpResponse)
                                            System.out.println(output);
                                        answer+=output;
                                    }  
                        } 

                        //Capture Exceptions
                        catch (IllegalStateException ex) {
                             System.err.println(ex);
                        }
                        catch (IOException ex) {
                             System.err.println(ex);
                        }
                            finally
                        {
                            //close the connection, set all objects to null
                            connection.disconnect();
                            connection = null;
                        }
                        return answer;        
        }
    }
}