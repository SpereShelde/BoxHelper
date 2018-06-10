package models;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tools.ExecuteShell;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SpereShelde on 2018/6/9.
 */
public class Spider implements Runnable {

    private String site, passkey, url, path;
    private ArrayList<String> freeIDs;
    private double min,max;
    private HtmlUnitDriver driver;

    public Spider(String site, String passkey, String path, double min, double max, HtmlUnitDriver driver) {
        this.site = site;
        this.passkey = passkey;
        this.path = path;
        this.min = min;
        this.max = max;
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    private boolean getFreeIDs(){
        System.out.println("Searching free torrent links from " + url + "...");
        System.out.println(this.getUrl());
        driver.setJavascriptEnabled(false);
        driver.get(url);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#outer")));
        String originalString = driver.findElementByCssSelector("#outer").getAttribute("outerHTML");
        String searchString = originalString;
        String regexString = "id=[0-9]*.*Free.*</a></td>";
        Pattern datePattern = Pattern.compile(regexString);
        Matcher dateMatcher = datePattern.matcher(searchString);
        int beEndIndex = 0;
        this.freeIDs = new ArrayList<String>();
        double size;
        if (this.max == -1) this.max = 65535;
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
            if (!this.freeIDs.contains(id)) {
                if (size >= this.min && size <= this.max) {
                    String[] temp = {"wget", "https://" + site + "/download.php?id=" + id + "&passkey=" + this.passkey, "-O", this.path + this.url.substring(8, 16) + "." + id + ".torrent"};
                    System.out.println("Downloading " + id + "..." + " size: " + new DecimalFormat("#.00").format(size)  + " GB");
                    ExecuteShell executeShell = new ExecuteShell(temp);
                    executeShell.run();
                    this.freeIDs.add(this.url.substring(8, 16) + "." + id);
                }
            }
            int subIndex = searchString.indexOf(subString);
            int subLength = subString.length();
            beEndIndex = subIndex + subLength + beEndIndex;
            searchString = originalString.substring(beEndIndex);
            dateMatcher = datePattern.matcher(searchString);
        }
        System.out.println(url + " done");
        return true;
    }

    @Override
    public void run() {
        getFreeIDs();

    }
}
