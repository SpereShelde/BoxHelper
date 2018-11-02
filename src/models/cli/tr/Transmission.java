package models.cli.tr;

import tools.HttpHelper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wzf on 2018/11/1.
 */
public class Transmission {

    private String webUI;
    private ArrayList<String> urls = new ArrayList<>();
    private double upload, download;

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public Transmission(String webUI, String site, ArrayList<String> urls, double download, double upload) {
        if (webUI.lastIndexOf("/") == webUI.length() - 1) {
            this.webUI = webUI.substring(0, webUI.length() - 1);
        } else {
            this.webUI = webUI;
        }
        this.urls = urls;
        this.upload = upload;
        this.download = download;
    }
    public void addTorrents(){
        try {
            String sid = HttpHelper.loginToTR(webUI + "/transmission/rpc", "BoxHelper", "127.0.0.1");
            HttpHelper.addTorrentsToTR(webUI + "/transmission/rpc", "BoxHelper", sid, "127.0.0.1", this.urls, download, upload); //Login DE
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
