package models.cli.de;

import tools.HttpHelper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SpereShelde on 2018/10/17.
 */
public class Deluge {

    private String paswd;
    private String webUI;
    private ArrayList<String> urls = new ArrayList<>();
    private double upload, download;

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public Deluge(String webUI, String passwd, String site, ArrayList<String> urls, double download, double upload) {
        this.paswd = passwd;
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
            HttpHelper.addTorrentsToDE(webUI + "/json", "BoxHelper", this.paswd, "127.0.0.1", this.urls, download, upload); //Login DE
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

