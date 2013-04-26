
package com.mtgox.api;

/**
 * @author Nicolò Paternoster <paternoster.nicolo at gmail.com>
 * This class encapsulate private and public key
 */
public class ApiKeys {

//Class Variables
private String privateKey,apiKey;

//Constructor

public ApiKeys(String privateKey, String apiKey)
{
    this.privateKey=privateKey;
    this.apiKey=apiKey;
}


//Methods

    /**
     * @return the privateKey
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return the apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
