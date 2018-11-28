package entity.sites;

import entity.sites.torrents.RawTorrent;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import org.openqa.selenium.Cookie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SpereShelde on 2018/10/25.
 */
public class Snexus extends PTSites {

    public Snexus(String page,  HashMap<String, String> cookies, float downLimit, float upLimit) {
        this.page = page;
        this.cookies = cookies;
        this.upLimit = upLimit;
        this.downLimit = downLimit;
//        String pageToGetPasskey = page;
        this.domain = page.substring(page.indexOf("//") + 2, page.indexOf("/", page.indexOf("//") + 2));
        acquirePasskey();
    }

    public static boolean isSnexus(String url) {
        return url.contains("m-team") || url.contains("hdcmct") || url.contains("hdsky") ||
                url.contains("hdhome") || url.contains("keepfrds") || url.contains("hdtime") ||
                url.contains("btschool") || url.contains("open") || url.contains("hdchina") ||
                url.contains("chdbits") || url.contains("ourbits");
    }

    private boolean acquirePasskey(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.get("https://" + getDomain());
        getCookies().forEach((k, v) -> driver.manage().addCookie(new Cookie(k, v)));
        setDriver(driver);
        driver.get("https://" + getDomain() + "/usercp.php");
        String source = driver.getPageSource();
        Pattern passkeyPattern = Pattern.compile("[0-9a-z]{32}");
        Matcher passkeyMatcher = passkeyPattern.matcher(source);
        if (passkeyMatcher.find()) {
            this.setPasskey(passkeyMatcher.group());
            return true;
        } else {
            System.out.println(" \\u001b[31m[ERROR]  \\u001b[32mBoxHelper\\u001b[0m: Cannot acquire passkey...\\u001b[0m");
            return false;
        }
    }

    public HashSet<RawTorrent> acquireTorrents(){
        HtmlUnitDriver driver = this.getDriver();
        HashSet<RawTorrent> torrents = new HashSet<>();
        driver.get(getPage());
        String pageSource = driver.getPageSource();
        torrents = decodePageSource(pageSource.substring(pageSource.lastIndexOf("colhead")));
        return torrents;
    }

    private HashSet<RawTorrent> decodePageSource(String pageSource){
        HashSet<RawTorrent> torrents = new HashSet<>();
        String[] lines = pageSource.split("\\n");
        StringBuilder torrent = new StringBuilder();
        for (int i = 0; i < lines.length - 2; i++) {
            torrent.append(lines[i].trim());
            if ("</tr>".equals(lines[i].trim()) && ((lines[i+1].trim().contains("<tr")) || (lines[i+2].trim().contains("div")))) {
                RawTorrent rawTorrent = generateRawTorrent(torrent.toString());
                if (rawTorrent != null) torrents.add(rawTorrent);
                torrent = new StringBuilder();
            }
        }
        return torrents;
    }

    private RawTorrent generateRawTorrent(String s) {
        RawTorrent rawTorrent = new RawTorrent();
        rawTorrent.setSource(s);
        rawTorrent.setDomain(this.domain);
        rawTorrent.setUploadLimit(this.upLimit);
        rawTorrent.setDownloadLimit(this.downLimit);
        if (s.contains("alt=\"Sticky\"") || s.contains("alt=\"sticky\"")) rawTorrent.setSticky(true);
        if (s.contains("alt=\"Free\"") || s.contains("alt=\"free\"")) rawTorrent.setFree(true);
        if (s.contains("禁转")) rawTorrent.setRestrict(true);
        if (s.contains("diy") || s.contains("DIY") || s.contains("DiY")) rawTorrent.setDiy(true);
        if (s.contains("中字") || s.contains("中文") || s.contains("双语") || s.contains("中英")) rawTorrent.setCnSubtitle(true);
        if (s.contains("H&R") || s.contains("h&r")) rawTorrent.setHr(true);

        //name & id
        String reg = "<a title=.*?</a>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            String caption = matcher.group();
            reg = "\".*?\"";
            pattern = Pattern.compile(reg);
            matcher = pattern.matcher(caption);
            if (matcher.find()) {
                String nameString = matcher.group().replaceAll("\"", "");
                if (nameString.length() >= 68) rawTorrent.setName(nameString.substring(0, 67));
                else rawTorrent.setName(nameString);
            }
            if (matcher.find()){
                reg = "id=\\d*";
                pattern = Pattern.compile(reg);
                matcher = pattern.matcher(s);
                matcher.find();
                rawTorrent.setId(Integer.parseInt(matcher.group().substring(3)));
            } else return null;
        } else return null;
        rawTorrent.setLink("https://" + this.getDomain() + "/download.php?id=" + rawTorrent.getId() + "&passkey=" + this.getPasskey());

        //time of live and free
        reg = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(s);
        SimpleDateFormat simpleDateFormat  = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] dates = new String[5];
        int i = 0;
        while (matcher.find()) {
            dates[i] = matcher.group();
            i++;
        }
        switch (i){
            case 0:
                System.out.println("\u001b[33;1m [Warning]\u001b[34m    BoxHelper:\u001b[0m Cannot acquire date...");
                break;
            default:
            case 1:
                try {
                    rawTorrent.setTimeOfLive(simpleDateFormat.parse(dates[0]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                break;
            case 3:
                try {
                    rawTorrent.setTimeOfFree(simpleDateFormat.parse(dates[0]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    rawTorrent.setTimeOfLive(simpleDateFormat.parse(dates[2]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
        //free but no time recorded, like m-team
        //need to fix current time
        if (rawTorrent.isFree() && 0 == rawTorrent.getTimeOfFree().compareTo(new Date(946656000))){
            reg = "\\d{1,2}[Dd]\\d{1,2}[Hh]|\\d{1,2}[日天]\\d{1,2}[时時]";
            pattern = Pattern.compile(reg);
            matcher = pattern.matcher(s);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (matcher.find()) {
                String time = matcher.group();
                reg = "\\d+";
                pattern = Pattern.compile(reg);
                matcher = pattern.matcher(time);
                matcher.find();
                calendar.add(calendar.DATE, Integer.parseInt(matcher.group()));
                matcher.find();
                calendar.add(calendar.HOUR, Integer.parseInt(matcher.group()));
            } else {
                reg = "\\d{1,2}[Hh]\\d{1,2}[Mm]|\\d{1,2}[时時]\\d{1,2}[分]";
                pattern = Pattern.compile(reg);
                matcher = pattern.matcher(s);
                if (matcher.find()) {
                    String time = matcher.group();
                    reg = "\\d+";
                    pattern = Pattern.compile(reg);
                    matcher = pattern.matcher(time);
                    matcher.find();
                    calendar.add(calendar.HOUR, Integer.parseInt(matcher.group()));
                    matcher.find();
                    calendar.add(calendar.MINUTE, Integer.parseInt(matcher.group()));
                }
            }
            rawTorrent.setTimeOfFree(calendar.getTime());
        }

        //size
        String sizeString = "";
        if (s.contains(">KB<")) {
            sizeString = s.substring(s.indexOf(">KB<") - 20);
            rawTorrent.setUnit(RawTorrent.Unit.KB);
        }
        if (s.contains(">MB<")) {
            sizeString = s.substring(s.indexOf(">MB<") - 20);
            rawTorrent.setUnit(RawTorrent.Unit.MB);
        }
        if (s.contains(">GB<")) {
            sizeString = s.substring(s.indexOf(">GB<") - 20);
            rawTorrent.setUnit(RawTorrent.Unit.GB);
        }
        if (s.contains(">TB<")) {
            sizeString = s.substring(s.indexOf(">TB<") - 20);
            rawTorrent.setUnit(RawTorrent.Unit.TB);
        }
        if (s.contains(">PB<")) {
            sizeString = s.substring(s.indexOf(">PB<") - 20);
            rawTorrent.setUnit(RawTorrent.Unit.PB);
        }
        reg = "\\d*\\.\\d*";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(sizeString);
        if (matcher.find()) rawTorrent.setSize(Double.parseDouble(matcher.group()));

        //seeders & leechers & complete
        reg = ">\\d+<";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(sizeString);
        if (matcher.find()) rawTorrent.setSeeder(Integer.parseInt(matcher.group().replaceAll("[<>]", "")));
        if (matcher.find()) rawTorrent.setLeecher(Integer.parseInt(matcher.group().replaceAll("[<>]", "")));
        if (matcher.find()) rawTorrent.setComplete(Integer.parseInt(matcher.group().replaceAll("[<>]", "")));
        //uploader

        if (s.contains("userdetails")) {
            String uploaderString = sizeString.substring(sizeString.indexOf("userdetails"));
            reg = ">\\w+<";
            pattern = Pattern.compile(reg);
            matcher = pattern.matcher(uploaderString);
            if (matcher.find()){
                rawTorrent.setUploader(matcher.group().replaceAll("[<>]", ""));
            }
        } else {
            rawTorrent.setUploader("anonymity");
        }
        return rawTorrent;
    }

    public String acquireHash(int id){
        HtmlUnitDriver driver = this.getDriver();
        driver.get("https://" + this.domain + "/details.php?id=" + id);
        String pageSource = driver.getPageSource();
        String reg = "[a-zA-Z0-9]{32}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(pageSource.substring(pageSource.indexOf("Hash")));
        if (matcher.find()) return matcher.group();
        else return "";
    }
}
