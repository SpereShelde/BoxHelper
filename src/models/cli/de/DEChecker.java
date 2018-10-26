package models.cli.de;

import models.torrent.deTorrent.DETorrent;
import models.torrent.QBTorrent;
import tools.ConvertJson;
import tools.HttpHelper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by SpereShelde on 2018/10/18.
 */
public class DEChecker implements Runnable{

    private String passwd;
    private String webUI;
    private long currentSize = 0;
    private long space;
    private String action;
    private int num = 0;

    public DEChecker(String webUI, String passwd, long space, String action, int num) {
        this.passwd = passwd;
        this.webUI = webUI;
        this.space = space;
        this.action = action;
        this.num = num;
    }

    @Override
    public void run() {
        //1073741824
        ArrayList<DETorrent> torrents = null;
        try {
            String sid = HttpHelper.loginToDE(webUI + "/json", "BoxHelper", this.passwd, "127.0.0.1");
            torrents = HttpHelper.getTorrentsFromDE(webUI + "/json", "BoxHelper", sid, "127.0.0.1", "\"name\",\"total_wanted\",\"upload_payload_rate\",\"ratio\",\"time_added\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (this.action){
            default:
            case "slow":
                torrents.sort(new Comparator<DETorrent>() {
                    @Override
                    public int compare(DETorrent deTorrent1, DETorrent deTorrent2) {
                        if(deTorrent1.getUpload_payload_rate() < deTorrent2.getUpload_payload_rate()) return -1;
                        else if (deTorrent1.getUpload_payload_rate() == deTorrent2.getUpload_payload_rate()) return 0;
                        else return 1;
                    }
                });
                break;
            case "add":
                torrents.sort(new Comparator<DETorrent>() {
                    @Override
                    public int compare(DETorrent deTorrent1, DETorrent deTorrent2) {
                        if(deTorrent1.getTime_added() < deTorrent2.getTime_added()) return -1;
                        else if (deTorrent1.getTime_added() == deTorrent2.getTime_added()) return 0;
                        else return 1;
                    }
                });
                break;
            case "small":
                torrents.sort(new Comparator<DETorrent>() {
                    @Override
                    public int compare(DETorrent deTorrent1, DETorrent deTorrent2) {
                        if(deTorrent1.getTotal_wanted() < deTorrent2.getTotal_wanted()) return -1;
                        else if (deTorrent1.getTotal_wanted() == deTorrent2.getTotal_wanted()) return 0;
                        else return 1;
                    }
                });
                break;
            case "ratio":
                torrents.sort(new Comparator<DETorrent>() {
                    @Override
                    public int compare(DETorrent deTorrent1, DETorrent deTorrent2) {
                        if(deTorrent1.getRatio() < deTorrent2.getRatio()) return -1;
                        else if (deTorrent1.getRatio() == deTorrent2.getRatio()) return 0;
                        else return 1;
                    }
                });
                break;
            case "large":
                torrents.sort(new Comparator<DETorrent>() {
                    @Override
                    public int compare(DETorrent deTorrent1, DETorrent deTorrent2) {
                        if(deTorrent1.getTotal_wanted() < deTorrent2.getTotal_wanted()) return 1;
                        else if (deTorrent1.getTotal_wanted() == deTorrent2.getTotal_wanted()) return 0;
                        else return -1;
                    }
                });
                break;
        }
        torrents.forEach(torrent -> {
            currentSize += torrent.getTotal_wanted();//B
        });
        if (currentSize <= space){
            System.out.println("DE: used space is " + new DecimalFormat("#0.00").format(currentSize / (double)1073741824) + " GB, under space limit (" + new DecimalFormat("#0.00").format(space / (double)1073741824) + " GB).");
        }else {
            System.out.println("DE: used space is " + new DecimalFormat("#0.00").format(currentSize / (double)1073741824) + " GB, beyond space limit (" + new DecimalFormat("#0.00").format(space / (double)1073741824) + " GB).\nBegin delete DE torrents...");
            List<DETorrent> torrentsToBeRemoved = new ArrayList<>();
            torrentsToBeRemoved = torrents.subList(0, this.num);
            Map<String, String> contents = new HashMap();
            torrentsToBeRemoved.forEach(torrent -> {
                try {
                    String sid = HttpHelper.loginToDE(webUI + "/json", "BoxHelper", this.passwd, "127.0.0.1");
                    if (HttpHelper.removeTorrentFromDE(webUI + "/json", "BoxHelper", sid, "127.0.0.1", torrent.getHash()))
                        System.out.println("DE: successfully deleted torrents.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
