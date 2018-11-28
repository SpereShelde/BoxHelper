package controller;

import entity.clients.Torrents;
import entity.config.Config;
import entity.sites.Snexus;
import entity.sites.torrents.RawTorrent;

import java.util.*;

/**
 * Created by SpereShelde on 2018/10/28.
 */
public class SitesController implements Runnable {

    private Properties properties;
    private RawTorrent torrent;
    private HashSet<RawTorrent> allTorrents;
    private TreeSet<RawTorrent> qualifiedTorrents;
    private HashSet<String> allTorrentsInfo;
    private ArrayList<String> addedTorrents;

    public void setAllTorrents(HashSet<RawTorrent> allTorrents) {
        this.allTorrents = allTorrents;
    }

    public void setQualifiedTorrents(TreeSet<RawTorrent> qualifiedTorrents) {
        this.qualifiedTorrents = qualifiedTorrents;
    }

    public SitesController(Properties properties, RawTorrent torrent) {
        this.properties = properties;
        this.torrent = torrent;
        this.allTorrents = new HashSet<>();
        this.qualifiedTorrents = new TreeSet<>();
        this.allTorrentsInfo = new HashSet<>();
        this.addedTorrents = new ArrayList<>();
    }

    public void setAddedTorrents(ArrayList<String> addedTorrents) {
        this.addedTorrents = addedTorrents;
    }

    public RawTorrent getTorrent() {
        return torrent;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public HashSet<RawTorrent> acquireTorrents(){
        HashSet<RawTorrent> freeChange = new HashSet<>();
        int pageAmount = Integer.parseInt(properties.getProperty("page_amount").trim());
        for (int i = 1; i <= pageAmount; i++) {
            String page = properties.getProperty("url_" + i).trim();
            HashMap<String, String > cookies = new HashMap();
            String[] rawCookie = properties.getProperty("cookies_" + i).trim().split(";");
            for (String cookie: rawCookie) {
                if (!"".equals(cookie)) {
                    cookies.put(cookie.substring(0, cookie.indexOf("=")), cookie.substring(cookie.indexOf("=") + 1));
                }
            }
            if(Snexus.isSnexus(page)) {
                Snexus snexus = new Snexus(page, cookies, Float.valueOf(properties.getProperty("download_limit_" + i)), Float.valueOf(properties.getProperty("upload_limit_" + i)));
                final int num = i;
                snexus.acquireTorrents().forEach(rawTorrent -> {
                    if (isQualified(num, rawTorrent)) {
                        rawTorrent.setHash(snexus.acquireHash(rawTorrent.getId()));
                        this.qualifiedTorrents.add(rawTorrent);
                    }
                    if (!allTorrentsInfo.contains(rawTorrent.getDomain() + "-" + rawTorrent.getId())){
                        allTorrents.add(rawTorrent);
                        allTorrentsInfo.add(rawTorrent.getDomain() + "-" + rawTorrent.getId());
                    } else {
                        allTorrents.removeIf(rawT -> rawT.getDomain() == rawTorrent.getDomain() && rawT.getId() == rawTorrent.getId());
                        allTorrents.add(rawTorrent);
                        if (!rawTorrent.isFree()){
                            freeChange.add(rawTorrent);
                        }
                    }
                });
            } else {

            }
        }

        System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Acquired " + allTorrents.size() + " torrents. ");
        Iterator<RawTorrent> iterator = qualifiedTorrents.descendingIterator();
        this.torrent = null;
        for (int i = 0; i < qualifiedTorrents.size(); i++){
            RawTorrent rawTorrent = iterator.next();
            if (!this.addedTorrents.contains(rawTorrent.getDomain() + "-" + rawTorrent.getId())) {
                this.torrent=rawTorrent;
                break;
            }
        }
        if (this.torrent != null) System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m The most qualified torrent is \u001b[33m" + torrent.getName() + "\u001b[0m in \u001b[33m" + torrent.getDomain() + "\u001b[0m");
        else System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m \u001b[33mNo qualified torrent.");
        return freeChange;
    }

    private boolean isQualified(int pageNum, RawTorrent rawTorrent) {
        boolean hit = false;
        String[] rules = properties.getProperty("rule_" + pageNum).trim().split(";");
        for (String rule: rules) {
            if (hit == true) return true;
            if (rule.contains("+")) {//series rules
                boolean subHit = true;
                String[] subRules = rule.trim().split("\\+");
                for (String sub: subRules) {
                    if (sub.contains("=")) {
                        String kv[] = sub.replaceAll("\\s", "").split("=");
                        switch (kv[0]){
                            default:
                            case "s": if (rawTorrent.getSeeder() > Integer.parseInt(kv[1])) subHit=false; break;
                            case "l": if (rawTorrent.getLeecher() < Integer.parseInt(kv[1])) subHit=false; break;
                            case "c": if (rawTorrent.getComplete() > Integer.parseInt(kv[1])) subHit=false; break;
                            case "u": if (!rawTorrent.getUploader().equals(kv[1])) subHit=false; break;
                            case "t": if (rawTorrent.getTimeOfFree().before(new Date(System.currentTimeMillis() + new Long(Integer.parseInt(kv[1]))*3600000))) subHit=false; break;
                            case "k": if (!rawTorrent.getSource().contains(kv[1])) subHit=false; break;
                        }
                    } else if (sub.contains("[")){
                        String size = sub.replaceAll("\\s", "").replaceAll("<>", "");
                        double lower = Double.parseDouble(size.substring(1, size.indexOf(",")));
                        double upper = Double.parseDouble(size.substring(size.indexOf(",") + 1, size.length() - 1));
                        if (upper < 0) upper = Double.MAX_VALUE;
                        if (lower > rawTorrent.getSize() || rawTorrent.getSize() > upper) subHit=false;
                    } else {
                        switch (sub.trim()){
                            default:
                            case "f": if (!rawTorrent.isFree()) subHit=false; break;
                            case "b": if (!rawTorrent.isSticky()) subHit=false; break;
                            case "d": if (!rawTorrent.isDiy()) subHit=false; break;
                            case "m": if (!rawTorrent.isCnSubtitle()) subHit=false; break;
                            case "r": if (!rawTorrent.isRestrict()) subHit=false; break;
                            case "h": if (!rawTorrent.isHr()) subHit=false; break;
                        }
                    }
                }
                hit = subHit;
            }
            else if (rule.contains("=")) {
                String kv[] = rule.replaceAll("\\s", "").split("=");
                switch (kv[0]){
                    default:
                    case "s": if (rawTorrent.getSeeder() <= Integer.parseInt(kv[1])) hit=true; break;
                    case "l": if (rawTorrent.getLeecher() >= Integer.parseInt(kv[1])) hit=true; break;
                    case "c": if (rawTorrent.getComplete() <= Integer.parseInt(kv[1])) hit=true; break;
                    case "u": if (rawTorrent.getUploader().equals(kv[1])) hit=true; break;
                    case "t": if (rawTorrent.getTimeOfFree().after(new Date(System.currentTimeMillis() + new Long(Integer.parseInt(kv[1]))*3600000))) hit=true; break;
                    case "k": if (rawTorrent.getSource().contains(kv[1])) hit=true; break;
                }
            } else if (rule.contains("[")){
                String size = rule.replaceAll("\\s", "").replaceAll("<>", "");
                double lower = Double.parseDouble(size.substring(1, size.indexOf(",")));
                double upper = Double.parseDouble(size.substring(size.indexOf(",") + 1, size.length() - 1));
                if (upper < 0) upper = Double.MAX_VALUE;
                if (lower < rawTorrent.getSize() && rawTorrent.getSize() < upper) hit=true;
            } else {
                switch (rule.trim()){
                    default:
                    case "f": if (rawTorrent.isFree()) hit=true; break;
                    case "b": if (rawTorrent.isSticky()) hit=true; break;
                    case "d": if (rawTorrent.isDiy()) hit=true; break;
                    case "m": if (rawTorrent.isCnSubtitle()) hit=true; break;
                    case "r": if (rawTorrent.isRestrict()) hit=true; break;
                    case "h": if (rawTorrent.isHr()) hit=true; break;
                }
            }
        }
        return hit;
    }

    @Override
    public void run() {
        acquireTorrents();
    }
}
