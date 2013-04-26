/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtgox.api;

/**
 *
 * @author advanced
 */
public interface TradeInterface{
    //Constructor
    
    //Methods
    
   
    /**
    * Returns an array with the user's balance in different currencies. 
    * 
    * The first element of the array is the amount in BTC,
    * the second element in USD and the third in EUR.
    * @return      an array with the current balance
    */
    public double[] getBalance() ;
    
   
    /**
    * Returns an array with the last price in USD and EUR. 
    * @return      a double value with the current price of 1 BTC in USD
    */
    public double getLastPriceUSD();
    
    /**
    * Returns an array with the last price in USD and EUR. 
    * @return      a double value with the current price of 1 BTC in EUR
    */
    public double getLastPriceEUR();
    
   
    /**
    * Returns the lag of the trading engine. 
    * @return      a string with the lag
    */
    public String getLag();
    
    /**
    * Withdraws an amount of BTC from the user's mtg account to a BTC address.
    * @param  amount  the amount of BTC to withdraw (min 0.1)
    * @param  dest_address  the address of the BTC wallet
    * @return      the transaction id (if success), (???) if failed //TODO
    */
    public String withdrawBTC(double amount, String dest_address);
    
    /**
    * Sells BTC at market price
    * @param  amount  the amount of BTC to sell (min 0.1)
    * @return  the transaction id (if success), (???) if failed //TODO
    */
    public String sellBTC(double amount);

    /**
    * Sells BTC at market price
    * @param  amount  the amount of BTC to buy (min 0.1)
    * @return  the transaction id (if success), (???) if failed //TODO
    */
    public String buyBTC(double amount);

    
}
