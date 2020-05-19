package com.zyd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUitls {

    private static Properties properties = null;

    static {
        InputStream stream = ClassLoader.getSystemResourceAsStream("kafka.properties");
        properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
