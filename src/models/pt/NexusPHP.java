package models.pt;

import models.cli.de.Deluge;
import models.cli.qb.QBittorrent;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SpereShelde on 2018/6/9.
 */
public class NexusPHP extends Pt implements Runnable {

    private String domain, passkey, url, cli;
    private ArrayList<String> urls = new ArrayList<String>();
    private ArrayList<String> newUrls = new ArrayList<String>();
    private double min, max, down, up;
    private HtmlUnitDriver driver;
    private String[] cliConfig;
    private boolean load;
    private boolean download;

    public NexusPHP(String url, String cli, double min, double max, double down, double up, HtmlUnitDriver driver, String[] cliConfig, boolean load, boolean download) {
        this.url = url;
        this.cli = cli;
        this.domain = url.substring(url.indexOf("//") + 2, url.indexOf("/", url.indexOf("//") + 2));
        this.min = min;
        this.max = max;
        this.down = down;
        this.up  = up;
        this.driver = driver;
        this.cliConfig = cliConfig;
        this.getPasskey();
        this.load = load;
        this.download = download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public static boolean isNexusPHP(String url){
        if (url.contains("m-team") || url.contains("hdcmct") || url.contains("hdsky") ||
            url.contains("hdhome") || url.contains("keepfrds") || url.contains("hdtime") ||
            url.contains("totheglory") || url.contains("open") || url.contains("hdchina") ||
            url.contains("chdbits") || url.contains("ourbits") || url.contains("btschool")){
            return true;
        } else return false;
    }
    private void getPasskey() {
        String source;
        Pattern passkeylink;
        if (url.contains("totheglory.im")){
            driver.get("https://totheglory.im/my.php");
            String[] s = driver.getPageSource().split("Passkey");
            String[] t = s[1].split("\n");
            passkey = t[3].trim();
        } else {
            driver.get(url);
            source = driver.getPageSource();
            passkeylink = Pattern.compile("href=.*details.php\\?id=[0-9]*.*hit=1");
            Matcher sizeMatcher = passkeylink.matcher(source);
            if (sizeMatcher.find()) {
                driver.get("https://" + this.domain + "/" + sizeMatcher.group().substring(6));
            }
            if (driver.getPageSource().contains("passkey=")) {
                source = driver.getPageSource();
                passkey = source.substring(source.indexOf("passkey=") + 8, source.indexOf("passkey=") + 40);
            } else {
                System.out.println("Cannot acquire passkey");
            }
        }
    }

    private void getFreeIDs() {

        ArrayList<String> urls = new ArrayList<>();
        String originalString = null;
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        if (url.contains("totheglory.im")){
            driver.get(url);
            String s = driver.getPageSource();
            System.out.println("Searching torrent links from " + driver.getCurrentUrl() + "...");
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("torrent_table")));
            originalString = driver.findElementById("torrent_table").getAttribute("outerHTML");

            String searchString = originalString;
            Pattern datePattern = Pattern.compile("[fF]ree*.*torrent=\"[0-9]*");
            Matcher dateMatcher = datePattern.matcher(searchString);
            int beEndIndex = 0;

            double size = 0;
            if (this.max == -1 || this.max == 0) this.max = 65535;
            String id = "";
            String subString = "";
            while(dateMatcher.find()) {
                subString = dateMatcher.group();
                Pattern idPattern = Pattern.compile("torrent=\"[0-9]*");
                Matcher idMatcher = idPattern.matcher(subString);
                if (idMatcher.find()) {
                    id = idMatcher.group().substring(9);
                    if ("".equals(id)) {
                        System.out.println("Cannot find torrent id.");
                        System.exit(106);
                    }
                }

                Pattern sizeAndUnitPattern = Pattern.compile("align=\"center\">.*<br>[TGM]B");
                String s1 = searchString.substring(searchString.indexOf(id));
                Matcher sizeAndUnitMatcher = sizeAndUnitPattern.matcher(s1);
                if (sizeAndUnitMatcher.find()) {
                    String sizeAndUnitString = sizeAndUnitMatcher.group();
                    Pattern sizePattern = Pattern.compile("[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|[1-9]\\d*");
                    Matcher sizeMatcher = sizePattern.matcher(sizeAndUnitString);
                    if (sizeMatcher.find()) {
                        size = Double.valueOf(sizeMatcher.group());
                    }
                    if ("MB".equals(sizeAndUnitString.substring(sizeAndUnitString.length() - 2))) {
                        size /= 1024;
                    }
                    if ("TB".equals(sizeAndUnitString.substring(sizeAndUnitString.length() - 2))) {
                        size *= 1024;
                    }
                }
                if (this.urls.size() == 0 && !this.load) {
                    System.out.println("Skip " + "https://" + domain + "/dl/" + id + "...");
                    this.newUrls.add("https://" + domain + "/dl/" + id + "/" + this.passkey);
                } else if (!this.urls.contains("https://totheglory.im/dl/" + id + "/" + this.passkey)) {
                    if (size >= this.min && size <= this.max) {
                        if (this.download) System.out.println(this.cli.toUpperCase() + ": got torrent from " + url + ", id: " + id + ", size: " + new DecimalFormat("#0.00").format(size) + " GB");
                        this.urls.add("https://totheglory.im/dl/" + id + "/" + this.passkey);
                        this.newUrls.add("https://totheglory.im/dl/" + id + "/" + this.passkey);
                    }
                } else {
                    System.out.println(this.cli.toUpperCase() + ": already added this torrent, id: " + id + "... Skip");
                }
                int subIndex = searchString.indexOf(subString);
                int subLength = subString.length();
                beEndIndex = subIndex + subLength + beEndIndex;
                searchString = originalString.substring(beEndIndex);
                dateMatcher = datePattern.matcher(searchString);
            }
        } else {
            driver.get(url);
            String s = driver.getPageSource();
            System.out.println("Searching torrent links from " + driver.getCurrentUrl() + "...");
            try {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("torrents")));
                originalString = driver.findElementByClassName("torrents").getAttribute("outerHTML");
            } catch (Exception e) {
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("torrent_list")));
                originalString = driver.findElementByClassName("torrent_list").getAttribute("outerHTML");
            }// originalString means the string of torrents list
            String searchString = originalString;
            Pattern datePattern = Pattern.compile("id=[0-9]*.*[fF]ree");//search the string of free torrents
            Matcher dateMatcher = datePattern.matcher(searchString);
            int beEndIndex = 0;

            double size = 0;//record the size of torrent
            if (this.max == -1 || this.max == 0) this.max = 65535;
            String id = "";
            String subString = "";
            while(dateMatcher.find()) {

                subString = dateMatcher.group();

                Pattern idPattern = Pattern.compile("id=[0-9]*");
                Matcher idMatcher = idPattern.matcher(subString);
                if  (idMatcher.find()){
                    id  = idMatcher.group().substring(3);
                    if ("".equals(id)){
                        System.out.println("Cannot find torrent id.");
                        System.exit(106);
                    }
                }

                Pattern sizeAndUnitPattern = Pattern.compile("id=" + id + ".*[TGM][B]");
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
                    if ("TB".equals(sizeAndUnitString.substring(sizeAndUnitString.length() - 2))){
                        size *= 1024;
                    }
                }
                if (this.urls.size() == 0 && !this.load){
                    System.out.println("Skip " + "https://" + domain + "/download.php?id=" + id + "...");
                    this.newUrls.add("https://" + domain + "/download.php?id=" + id + "&passkey=" + this.passkey);
                } else if (!this.urls.contains("https://" + domain + "/download.php?id=" + id + "&passkey=" + this.passkey)) {
                    if (size >= this.min && size <= this.max) {
                        if (this.download) System.out.println(this.cli.toUpperCase() + ": got torrent from " + url + ", id: " + id + ", size: " + new DecimalFormat("#0.00").format(size)  + " GB");
                        this.urls.add("https://" + domain + "/download.php?id=" + id + "&passkey=" + this.passkey);
                        this.newUrls.add("https://" + domain + "/download.php?id=" + id + "&passkey=" + this.passkey);
                    }
                } else {
                    System.out.println(this.cli.toUpperCase() + ": already added this torrent, id: " + id + "... Skip");
                }
                int subIndex = searchString.indexOf(subString);
                int subLength = subString.length();
                beEndIndex = subIndex + subLength + beEndIndex;
                searchString = originalString.substring(beEndIndex);
                dateMatcher = datePattern.matcher(searchString);
            }
        }
        if (this.urls.size() == 0 && !this.load) {
            this.urls.addAll(this.newUrls);
            this.newUrls = new ArrayList<String>();
        }
    }

    @Override
    public void run() {
        this.getFreeIDs();
        if (this.download) {
            switch (this.cli.toLowerCase()) {
                default:
                case "de":
                    Deluge deluge = new Deluge(this.cliConfig[0], this.cliConfig[1], url, this.urls, this.down, this.up);
                    deluge.setUrls(this.newUrls);
                    deluge.addTorrents();
                    break;
                case "qb":
                    QBittorrent qBittorrent = new QBittorrent(this.cliConfig[0], this.cliConfig[1], url, this.urls, this.down, this.up);
                    qBittorrent.setUrls(this.newUrls);
                    qBittorrent.addTorrents();
                    break;
                case "tr":
                case "rt":
            }
            this.newUrls = new ArrayList<String>();
        }
    }
}
