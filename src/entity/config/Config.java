package entity.config;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by SpereShelde on 2018/10/29.
 */
public class Config {

    public static Properties loadConfig() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(new File("config.properties")), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Properties loadPages() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(new File("pages.properties")), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
