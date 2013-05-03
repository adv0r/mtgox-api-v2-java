#Trading  Bitcoins using Mt.Gox API v2
This is a simple java client that uses the new Mt.Gox API (V2).  The API are not well documented and this libraries are still unstable. I must say thank you to nitrous which is putting effort in documenting the v2 [here](https://bitbucket.org/nitrous/mtgox-api/overview).

Disclaimer:  **This codebase is under development. Use it at your own risk** 

#Get the sources
Clone the repository with git (`git clone https://github.com/adv0r/mtgox-api-v2-java.git`) or [download the zipped archive](https://github.com/adv0r/mtgox-api-v2-java/archive/master.zip)

#Sample project
You can play around by opening your IDE (Netbeans, Eclipse, Sublime) and importing the [zipped sample project](https://github.com/adv0r/mtgox-api-v2-java/raw/master/dist/mtgox-apiv2-sampleproject.zip) (JRE >7)

#Setup
First setup the java trustStore adding mtgox certificates. Basically you neet to tell to the JVM to trust mtgox's public certificate. I generated an encrypted keystore under res/ssl/mtgox.jks. 

```java
System.setProperty("javax.net.ssl.trustStore","res/ssl/mtgox.jks");
System.setProperty("javax.net.ssl.trustStorePassword","h4rdc0r_"); //I encripted the jks file using this pwd
```

Create an object encapsulating your API keys :
```java
ApiKeys keys = new ApiKeys("your-secret-key", "your-api-key"); 
```

  Create the interface for trading on Mtgox, passing the apikeys object
  ```java
MtGox trade = new MtGox(keys);
//trade.setPrintHTTPResponse(true); //Uncomment this line to print the HTTP responses.
  ```

#Usage
 See the  [example class](https://github.com/adv0r/mtgox-api-v2-java/blob/master/src/com/mtgox/examples/UsageExample.java) for a working demo. 

## Current price
 Print the current price of BTC in USD and EUR. This functions use the undocumented ticker_fast function.
 ```java  
 System.out.println("Current price of 1 BTC : \n" +
                "   "+trade.getLastPriceUSD()+" $\n"+
                "   "+trade.getLastPriceEUR()+" €");
 ```


## Current balance
 Print the current balance from your MtGox account in USD,EUR, and BTC
  ```java    
System.out.println("Current account balance : \n" +
                "   "+trade.getBalance()[0]+" BTC\n"+
                "   "+trade.getBalance()[1]+" $\n"+
                "   "+trade.getBalance()[2]+" €");
  ```

## Engine lag
 Print the trading engine lag as a String 
  ```java  
System.out.println("Current Lag : "+ trade.getLag());
  ```

## Buy Bitcoins at market price
Buy 0.1 BTC at market price
```java  
String buyResult = trade.buyBTC(0.1);
System.out.println(buyResult);
```  

## Sell Bitcoins at market price
  Sell 0.1 BTC at market price
 ```java  
String sellResult = trade.sellBTC(0.1);
System.out.println(sellResult);
 ```  

## Withdraw Bitcoins (and donate!)
Withdraw 0.1 BTC from your mtgox to a wallet address (mine)
```java  
trade.withdrawBTC(0.1, "1N7XxSvek1xVnWEBFGa5sHn1NhtDdMhkA7")
```  
I'll be more than happy if you decide to test the above instruction ;) Indeed, by executing the code above you will donate 0.1 BTC to support my effort in completing this trading API. Feel free to integrate this library into your project. If you get rich (or if you design some trading algorithm to get rich) , let me know ;).

#Love it

Let me know if you like it and you find it useful. Fork it, clone it, talk to me 
If you want to support open source projects like this, support me donating to  **1N7XxSvek1xVnWEBFGa5sHn1NhtDdMhkA7.**