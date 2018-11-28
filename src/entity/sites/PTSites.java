package entity.sites;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SpereShelde on 2018/10/25.
 */
public class PTSites {

    String domain;
    String page = "";// page to listen
    HashMap<String, String> cookies = new HashMap<>();
    String passkey;
    HtmlUnitDriver driver;
    float upLimit, downLimit;

//    public PTSites(String page, HashMap<String, String> cookies, float upLimit, float downLimit) {
//        this.page = page;
//        this.cookies = cookies;
//        this.upLimit = upLimit;
//        this.downLimit = downLimit;
//    }

    public float getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(float upLimit) {
        this.upLimit = upLimit;
    }

    public float getDownLimit() {
        return downLimit;
    }

    public void setDownLimit(float downLimit) {
        this.downLimit = downLimit;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public HashMap<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(HashMap<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public HtmlUnitDriver getDriver() {
        return driver;
    }

    public void setDriver(HtmlUnitDriver driver) {
        this.driver = driver;
    }

}
