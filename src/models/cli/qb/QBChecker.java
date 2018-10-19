package models.cli.qb;

import models.torrent.QBTorrent;
import tools.ConvertJson;
import tools.HttpHelper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SpereShelde on 2018/7/7.
 */
public class QBChecker implements Runnable {

    private String sessionID;
    private String webUI;
    private long currentSize = 0;
    private long space;
    private String action;
    private int num = 0;

    public QBChecker(String webUI, String sessionID, long space, String action, int num) {
        this.sessionID = sessionID;
        this.webUI = webUI;
        this.space = space;
        this.action = action;
        this.num = num;
    }

    @Override
    public void run() {
        ArrayList<QBTorrent> QBTorrents = null;
        try {
            String act = "";
            switch (this.action){
                default:
                case "slow": act = "upspeed"; break;
                case "add": act = "added_on"; break;
                case "complete": act = "completion_on"; break;
                case "active": act = "last_activity"; break;
                case "small":
                case "large": act = "size"; break;
                case "ratio": act = "ratio"; break;
            }
            QBTorrents = ConvertJson.convertQBTorrents(HttpHelper.doGet(webUI + "/query/torrents?filter=completed&category=BoxHelper&sort=" + act, "BoxHelper", sessionID, "127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        QBTorrents.forEach(QBTorrent -> {
            currentSize += QBTorrent.getSize();
        });
        if (currentSize <= space){
            System.out.println("QB: space used is " + new DecimalFormat("#0.00").format(currentSize / (double)1073741824) + " GB, under space limit.");
        }else {
            System.out.println("QB: space used is " + new DecimalFormat("#0.00").format(currentSize / (double)1073741824) + " GB, beyond space limit.\nBegin delete QB torrents...");
            List<QBTorrent> torrentsToBeRemoved;
            switch (this.action){
                default:
                case "slow":
                case "add":
                case "complete":
                case "active":
                case "small":
                case "ratio":
                    if (this.num <= QBTorrents.size()) {
                        torrentsToBeRemoved = QBTorrents.subList(0, this.num);
                    }else {
                        torrentsToBeRemoved = QBTorrents;
                    }
                    break;
                case "large":
                    if (this.num <= QBTorrents.size()) {
                        torrentsToBeRemoved = QBTorrents.subList(QBTorrents.size() - this.num - 1, QBTorrents.size());
                    }else {
                        torrentsToBeRemoved = QBTorrents;
                    }
                    break;
            }
            Map<String, String> contents = new HashMap();
            StringBuilder hashs = new StringBuilder();
            torrentsToBeRemoved.forEach(QBTorrent -> {
                hashs.append(QBTorrent.getHash() + "|");
                System.out.println("QB: deleting torrent +" + QBTorrent.getName() + ", size: " + new DecimalFormat("#0.00").format(QBTorrent.getSize() / (double)1073741824) + "GB...");
            });
            hashs.deleteCharAt(hashs.length() - 1);
            contents.put("hashes", hashs.toString());
            try {
                HttpHelper.doPostNormalForm(webUI + "/command/deletePerm", "Fiddler", sessionID, "127.0.0.1", contents);
                System.out.println("QB: successfully deleted torrent.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
