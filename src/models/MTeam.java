package models;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tools.ExecuteShell;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by SpereShelde on 2018/5/31.
 */
public class MTeam implements Runnable {

    private Map configures = new HashMap();
    private ArrayList<String> freeIDs;

    private String currentUrl;

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public Map getConfigures() {
        return configures;
    }

    public MTeam() {
        this.configures.put("cookie", "");
        this.configures.put("passkey", "");

    }

    public boolean getConfig() {

        if (!("".equals(this.configures.get("cookie").toString()) || "".equals(this.configures.get("passkey").toString()))){
            return true;
        }
        if (new File("spider.mt.txt").exists()){
            try {
                Object[] config = Files.readAllLines(Paths.get("spider.mt.txt")).toArray();
                for (Object o: config) {
                    if (!("".equals(o) || o.toString().trim().charAt(0)=='#')){
                        String s = o.toString() + " ";
                        String[] line = s.split("<=>");
                        configures.put(line[0].trim(), line[1].trim());
                    }
                }
            } catch (IOException e) {
                System.out.println("Unable to read file mt.cookie.txt");
            }
            System.out.println("Acquiring cookie and passkey from M-Team website...");
            HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
            driver.manage().deleteAllCookies();
            driver.setJavascriptEnabled(false);
            driver.get("https://tp.m-team.cc/login.php");
            WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(new By.ByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[1]/td[2]/input")));
            driver.findElementByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[1]/td[2]/input").sendKeys(configures.get("username").toString());
            driver.findElementByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[2]/td[2]/input").sendKeys(configures.get("password").toString());
            driver.findElementByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[3]/td/input").click();
            driver.get("https://tp.m-team.cc/index.php");
            if (!"https://tp.m-team.cc/index.php".equals(driver.getCurrentUrl())) {
                System.out.println("Username or Password error!");
                System.exit(100);
            }
            String cookies = driver.manage().getCookies().toString();
            driver.get("https://tp.m-team.cc/details.php?id=200000&hit=1");
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#outer > table:nth-child(6) > tbody > tr:nth-child(6) > td.rowfollow > b:nth-child(1) > a:nth-child(1)")));
            String link = driver.findElementByCssSelector("#outer > table:nth-child(6) > tbody > tr:nth-child(6) > td.rowfollow > b:nth-child(1) > a:nth-child(1)").getAttribute("outerHTML");
            String[] temp = link.split("=");
            String passkey = temp[temp.length - 1].substring(0, 32);
            if (!"".equals(cookies) && !"".equals(passkey)) {
                configures.replace("cookie", cookies);
                configures.replace("passkey", passkey);
                System.out.println("Cookie and passkey acquired.");
            } else {
                System.out.println("Unable to acquire cookie and password...");
                System.exit(101);
            }
        }
        return true;
    }

    private boolean getFreeIDs(String url){
        System.out.println("Acquiring free torrent links from " + url + "...");
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
        String[] fragments = this.configures.get("cookie").toString().split("=|; ");
        Cookie cookie = new Cookie(fragments[0].substring(1, fragments[0].length()), fragments[1], fragments[7].substring(0, fragments[7].length() - 1), fragments[5], new Date(new Date().getTime() + 60*24*60*60*1000));
        driver.setJavascriptEnabled(false);
        driver.get("https://tp.m-team.cc/login.php");
        driver.manage().addCookie(cookie);
        driver.get(url);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#form_torrent > table")));
        String originalString = driver.findElementByCssSelector("#form_torrent > table").getAttribute("outerHTML");
        String searchString = originalString;
        String regexString = "id=[0-9]*.*Free.*</a></td>";
        Pattern datePattern = Pattern.compile(regexString);
        Matcher dateMatcher = datePattern.matcher(searchString);
        int beEndIndex = 0;
        freeIDs = new ArrayList<String>();
        double size = 0;
        double max = Double.valueOf(configures.get("max").toString());
        if (max == -1) max = 65535;
        while(dateMatcher.find()) {
            String subString = dateMatcher.group();
            String id = subString.substring(3, subString.indexOf("&"));

            int indexOfSize = searchString.indexOf("</span></td><td class=\"rowfollow\">");
            int indexOfUnit = searchString.indexOf("B</td><td");
            String sizeAndUnitString = searchString.substring(indexOfSize, indexOfUnit);

            if ("M".equals(sizeAndUnitString.substring(sizeAndUnitString.length() - 1))){
                size = Double.valueOf(sizeAndUnitString.substring(34, sizeAndUnitString.lastIndexOf("<"))) / 1024;
            } else {
                size = Double.valueOf(sizeAndUnitString.substring(34, sizeAndUnitString.lastIndexOf("<")));
            }
            if (!freeIDs.contains(id)) {
                if (size >= Double.valueOf(configures.get("min").toString()) && size <= max) {
                    String[] temp = {this.configures.get("wgetPath").toString(), "https://tp.m-team.cc/download.php?id=" + id + "&passkey=" + this.configures.get("passkey").toString(), "-O", this.configures.get("downloadPath") + id + ".torrent"};
                    System.out.println("Downloading " + id + "..." + " size: " + size + " GB");
                    ExecuteShell executeShell = new ExecuteShell(temp);
                    executeShell.run();
                    freeIDs.add(id);
                }
            }
            int subIndex = searchString.indexOf(subString);
            int subLength = subString.length();
            beEndIndex = subIndex + subLength + beEndIndex;
            searchString = originalString.substring(beEndIndex);
            dateMatcher = datePattern.matcher(searchString);
        }
        System.out.println("Spider of " + url + " done");
        return true;
    }

    @Override
    public void run() {
        this.getFreeIDs(this.currentUrl.trim());
    }

}
