package com.ctsi.ssdc.util;

import java.util.HashMap;
import java.util.Random;

/**
 * 短链接工具类
 * @author ctsi
 *
 */
public class ShortURL {

    // storage for generated keys
    private HashMap<String, String> keyMap; // key-url map
    private HashMap<String, String> valueMap;// url-key map to quickly check
                                                // whether an url is
    // already entered in our system
    private String domain; // Use this attribute to generate urls for a custom
                            // domain name defaults to http://fkt.in
    private char myChars[]; // This array is used for character to number
                            // mapping
    private Random myRand; // Random object used to generate random integers
    private int keyLength; // the key length in URL defaults to 8

    // Default Constructor
    ShortURL() {
        keyMap = new HashMap<String, String>();
        valueMap = new HashMap<String, String>();
        myRand = new Random();
        keyLength = 8;
        myChars = new char[62];
        for (int i = 0; i < 62; i++) {
            int j = 0;
            if (i < 10) {
                j = i + 48;
            } else if (i > 9 && i <= 35) {
                j = i + 55;
            } else {
                j = i + 61;
            }
            myChars[i] = (char) j;
        }
        domain = "";
    }

    // Constructor which enables you to define tiny URL key length and base URL
    // name
    public ShortURL(int length, String newDomain) {
        this();
        this.keyLength = length;
        if (!newDomain.isEmpty()) {
            newDomain = sanitizeURL(newDomain);
            domain = newDomain;
        }
    }

    // shortenURL
    // the public method which can be called to shorten a given URL
    /**
     * 获取短链接
     * @param longURL
     * @return shortURL
     */
    

    public String shortenURL(String longURL) {
        String shortURL = "";
        longURL = sanitizeURL(longURL);
        if (valueMap.containsKey(longURL)) {
        	shortURL = domain + valueMap.get(longURL);
        } else {
            shortURL = domain + getKey(longURL);
        }
        return shortURL;
    }

    // expandURL
    // public method which returns back the original URL given the shortened url
    /**
     * 获取长链接
     * @param shortURL
     * @return shortURL
     */
    

    public String expandURL(String shortURL) {
        String longURL = "";
        String key = shortURL.substring(domain.length() + 1);
        longURL = keyMap.get(key);
        return longURL;
    }


    // sanitizeURL
    // This method should take care various issues with a valid url
    // e.g. www.google.com,www.google.com/, http://www.google.com,
    // http://www.google.com/
    // all the above URL should point to same shortened URL
    // There could be several other cases like these.
    String sanitizeURL(String url) {
        if ("http://".equals(url.substring(0, 7))) {
        	url = url.substring(7);
        }
        if ("https://".equals(url.substring(0, 8))) {
            url = url.substring(8);
        }
        if (url.charAt(url.length() - 1) == '/') {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 获取key
     * @param longURL
     * @return key
     */
    private String getKey(String longURL) {
        String key;
        key = generateKey();
        keyMap.put(key, longURL);
        valueMap.put(longURL, key);
        return key;
    }
    /**
     * 生成KEY值
     * @return key
     */
    // generateKey
    private String generateKey() {
        StringBuffer key = new StringBuffer();
        boolean flag = true;
        while (flag) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i <= keyLength; i++) {
                sb.append(myChars[myRand.nextInt(62)]);
            }
            // System.out.println("Iteration: "+ counter + "Key: "+ key);
            key.append(sb);
            if (!keyMap.containsKey(sb.toString())) {
                flag = false;
            }
        }
        return key.toString();
    }

}