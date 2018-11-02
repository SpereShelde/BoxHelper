package models.cli.tr;

import models.torrent.TRTorrent;
import tools.HttpHelper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by SpereShelde on 2018/11/1.
 */
public class TRChecker implements Runnable{

    private String webUI;
    private long currentSize = 0;
    private long space;
    private String action;
    private int num = 0;
    private int downloadingAmount = 0;

    public int getDownloadingAmount() {
        return downloadingAmount;
    }

    public void setDownloadingAmount(int downloadingAmount) {
        this.downloadingAmount = downloadingAmount;
    }

    public TRChecker(String webUI, long space, String action, int num) {
        this.webUI = webUI;
        this.space = space;
        this.action = action;
        this.num = num;
    }

    public TRChecker() {
        downloadingAmount = 0;
    }

    @Override
    public void run() {
        //1073741824
        ArrayList<TRTorrent> torrents = null;
        try {
            String sid = HttpHelper.loginToTR(webUI + "/transmission/rpc", "BoxHelper", "127.0.0.1");
            torrents = HttpHelper.getTorrentsFromTR(webUI + "/transmission/rpc", "BoxHelper", sid, "127.0.0.1", "\"id\",\"status\",\"totalSize\",\"addedDate\",\"rateDownload\",\"rateUpload\",\"uploadRatio\",\"doneDate\",\"activityDate\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (this.action){
            default:
            case "slow":
                torrents.sort(new Comparator<TRTorrent>() {
                    @Override
                    public int compare(TRTorrent trTorrent1, TRTorrent trTorrent2) {
                        if(trTorrent1.getRateUpload() < trTorrent2.getRateUpload()) return -1;
                        else if (trTorrent1.getRateUpload() == trTorrent2.getRateUpload()) return 0;
                        else return 1;
                    }
                });
                break;
            case "add":
                torrents.sort(new Comparator<TRTorrent>() {
                    @Override
                    public int compare(TRTorrent trTorrent1, TRTorrent trTorrent2) {
                        if(trTorrent1.getAddedDate() < trTorrent2.getAddedDate()) return -1;
                        else if (trTorrent1.getAddedDate() == trTorrent2.getAddedDate()) return 0;
                        else return 1;
                    }
                });
                break;
            case "complete":
                torrents.sort(new Comparator<TRTorrent>() {
                    @Override
                    public int compare(TRTorrent trTorrent1, TRTorrent trTorrent2) {
                        if(trTorrent1.getDoneDate() < trTorrent2.getDoneDate()) return -1;
                        else if (trTorrent1.getDoneDate() == trTorrent2.getDoneDate()) return 0;
                        else return 1;
                    }
                });
                break;
            case "active":
                torrents.sort(new Comparator<TRTorrent>() {
                    @Override
                    public int compare(TRTorrent trTorrent1, TRTorrent trTorrent2) {
                        if(trTorrent1.getActivityDate() < trTorrent2.getActivityDate()) return -1;
                        else if (trTorrent1.getActivityDate() == trTorrent2.getActivityDate()) return 0;
                        else return 1;
                    }
                });
                break;
            case "small":
                torrents.sort(new Comparator<TRTorrent>() {
                    @Override
                    public int compare(TRTorrent trTorrent1, TRTorrent trTorrent2) {
                        if(trTorrent1.getTotalSize() < trTorrent2.getTotalSize()) return -1;
                        else if (trTorrent1.getTotalSize() == trTorrent2.getTotalSize()) return 0;
                        else return 1;
                    }
                });
                break;
            case "ratio":
                torrents.sort(new Comparator<TRTorrent>() {
                    @Override
                    public int compare(TRTorrent trTorrent1, TRTorrent trTorrent2) {
                        if(trTorrent1.getUploadRatio() < trTorrent2.getUploadRatio()) return -1;
                        else if (trTorrent1.getUploadRatio() == trTorrent2.getUploadRatio()) return 0;
                        else return 1;
                    }
                });
                break;
            case "large":
                torrents.sort(new Comparator<TRTorrent>() {
                    @Override
                    public int compare(TRTorrent trTorrent1, TRTorrent trTorrent2) {
                        if(trTorrent1.getTotalSize() < trTorrent2.getTotalSize()) return 1;
                        else if (trTorrent1.getTotalSize() == trTorrent2.getTotalSize()) return 0;
                        else return -1;
                    }
                });
                break;
        }
        torrents.forEach(torrent -> {
            currentSize += torrent.getTotalSize();//B
            if (torrent.getStatus() == 4){
                downloadingAmount += 1;
            }
        });
        if (currentSize <= space){
            System.out.println("TR: used space is " + new DecimalFormat("#0.00").format(currentSize / (double)1073741824) + " GB, under space limit (" + new DecimalFormat("#0.00").format(space / (double)1073741824) + " GB).");
        }else {
            System.out.println("TR: used space is " + new DecimalFormat("#0.00").format(currentSize / (double)1073741824) + " GB, beyond space limit (" + new DecimalFormat("#0.00").format(space / (double)1073741824) + " GB).\nBegin delete TR torrents...");
            List<TRTorrent> torrentsToBeRemoved = new ArrayList<>();
            torrentsToBeRemoved = torrents.subList(0, this.num);
            String sid = HttpHelper.loginToTR(webUI + "/transmission/rpc", "BoxHelper", "127.0.0.1");
            StringBuilder stringBuilder = new StringBuilder();
            torrentsToBeRemoved.forEach(torrent -> {
                stringBuilder.append(torrent.getId() + ",");
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            try {
                if (HttpHelper.removeTorrentFromTR(webUI + "/transmission/rpc", "BoxHelper", sid, "127.0.0.1", stringBuilder.toString()))
                    System.out.println("TR: successfully deleted torrents.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
