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
    private ArrayList<String> freeIDs = new ArrayList<String>();
    private double min,max;
    private HtmlUnitDriver driver;

    public Spider(String site, String url, String path, double min, double max, HtmlUnitDriver driver) {
        this.site = site;
        this.url = url;
        this.path = path;
        this.min = min;
        this.max = max;
        this.driver = driver;
        getPasskey();
    }

    private void getPasskey() {
        String source;
        synchronized (driver){
            driver.get(url);
            source = driver.getPageSource();
            Pattern passkeylink = Pattern.compile("href=.*details.php\\?id=[0-9]*.*hit=1");
            Matcher sizeMatcher = passkeylink.matcher(source);
            if  (sizeMatcher.find()){
                driver.get("https://" + this.site + "/" + sizeMatcher.group().substring(6));
            }
            if (driver.getPageSource().contains("passkey=")){
                source = driver.getPageSource();
                passkey = source.substring(source.indexOf("passkey=") + 8, source.indexOf("passkey=") + 40);
            } else {
                System.out.println("Cannot acquire passkey");
            }
        }
    }

    private void getFreeIDs() {
        String originalString = null;
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        synchronized (driver){
            driver.get(url);
            System.out.println("Searching free torrent links from " + driver.getCurrentUrl() + "...");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("torrents")));
                originalString = driver.findElementByClassName("torrents").getAttribute("outerHTML");
            } catch (Exception e) {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("torrent_list")));
                originalString = driver.findElementByClassName("torrent_list").getAttribute("outerHTML");
            }
        }

        String searchString = originalString;
        String regexString = "id=[0-9]*.*[fF]ree";
        Pattern datePattern = Pattern.compile(regexString);
        Matcher dateMatcher = datePattern.matcher(searchString);
        int beEndIndex = 0;

        double size = 0;
        if (this.max == -1) this.max = 65535;
        String id = "";
        String subString = "";
        while(dateMatcher.find()) {

            subString = dateMatcher.group();
            id = subString.substring(3, subString.indexOf("&"));
            Pattern sizeAndUnitPattern = Pattern.compile("id=" + id + ".*[GM][B]");
            Matcher sizeAndUnitMatcher = sizeAndUnitPattern.matcher(searchString.substring(searchString.indexOf(id)));
            if  (sizeAndUnitMatcher.find()){
                String sizeAndUnitString = sizeAndUnitMatcher.group().substring(sizeAndUnitMatcher.group().lastIndexOf("class"));
                Pattern sizePattern = Pattern.compile("[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|[1-9]\\d*");
                Matcher sizeMatcher = sizePattern.matcher(sizeAndUnitString);
                if  (sizeMatcher.find()) {
                    size = Double.valueOf(sizeMatcher.group());
                }
                if ("MB".equals(sizeAndUnitString.substring(sizeAndUnitString.length() - 2))){
                    size /= 1024;
                }
            }
            if (!this.freeIDs.contains(id)) {
                if (size >= this.min && size <= this.max) {
                    String[] temp = {"/usr/bin/wget", "https://" + site + "/download.php?id=" + id + "&passkey=" + this.passkey, "-O", this.path + site + "." + id + ".torrent"};
                    System.out.println("Downloading to " + this.path + site + "." + id + ".torrent, size: " + new DecimalFormat("#0.00").format(size)  + " GB");
                    ExecuteShell executeShell = new ExecuteShell(temp);
                    executeShell.run();
                    this.freeIDs.add(id);
                }
            } else {
                System.out.println("Already downloaded " + id + "... Skip");
            }
            int subIndex = searchString.indexOf(subString);
            int subLength = subString.length();
            beEndIndex = subIndex + subLength + beEndIndex;
            searchString = originalString.substring(beEndIndex);
            dateMatcher = datePattern.matcher(searchString);
        }
        System.out.println(url + " done");
    }

    @Override
    public void run() {
        if (!"/".equals(this.path.substring(this.path.length() - 1))){
            this.path = this.path + "/";
        }
        getFreeIDs();

    }
}
