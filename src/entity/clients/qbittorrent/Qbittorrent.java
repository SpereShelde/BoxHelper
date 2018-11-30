package entity.clients.qbittorrent;

import com.google.gson.*;
import entity.clients.Client;
import entity.clients.Torrents;
import entity.clients.qbittorrent.info.QbittorrentGlobalInfo;
import entity.clients.qbittorrent.torrents.QbittorrentTorrents;
import entity.clients.qbittorrent.torrents.QbittorrentTorrentsListResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;

import static java.lang.Thread.sleep;

/**
 * Created by SpereShelde on 2018/11/19.
 */
public class Qbittorrent extends Client {

    private String username, password, sessionID;

    public Qbittorrent(String username, String password) {
        this.setPort(2017);
        if (username == null || "".equals(username)) {
            this.username = "admin";
        } else this.username = username;
        if (password == null || "".equals(password)) {
            this.password = "admin";
        } else this.password = password;
        this.sessionID = "";
        this.setCount(1);
        this.setAverageUp((long)0);
        this.setTorrentSize((long)0);
    }

    public Qbittorrent(int port, String username, String password) {
        if (port <= 0) {
            this.setPort(2017);
        } else this.setPort(2017);
        if (username == null || "".equals(username)) {
            this.username = "admin";
        } else this.username = username;
        if (password == null || "".equals(password)) {
            this.password = "admin";
        } else this.password = password;
        this.sessionID = "";
        this.setCount(1);
        this.setAverageUp((long)0);
        this.setTorrentSize((long)0);
    }

    @Override
    public boolean acquireApiVersion() throws IOException {
        boolean supported = true;
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Checking API version ...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + this.getPort() + "/api/v2/app/webapiVersion");
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + this.getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        CloseableHttpResponse response = httpClient.execute(httpget);
        String responseString = "1";
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            responseString = EntityUtils.toString(entity) ;
            this.setApiVersion(Float.parseFloat(responseString));
            if (this.getApiVersion() >= 2) System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m API version is " + this.getApiVersion() + ". Continue...");
            else {
                System.out.println("\u001b[31;1m [Error]  \u001b[36m  qBittorrent:\u001b[0m API version is " + this.getApiVersion() + ". Please upgrade QB to 4.1 or above.");
                supported = false;
            }
        } else {
            supported = false;
            System.out.println("\u001b[31;1m [Error]  \u001b[36m  qBittorrent:\u001b[0m Cannot acquire API version.");
        }
        response.close();
        httpClient.close();
        return supported;
    }

    @Override
    public void login() throws IOException {
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Login ...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + this.getPort() + "/api/v2/auth/login?username=" + this.username + "&password=" + this.password);
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + this.getPort());
        CloseableHttpResponse response = httpClient.execute(httpget);
        String setCookie = response.getFirstHeader("Set-Cookie").getValue();
        String sid = setCookie.substring("SID=".length(), setCookie.indexOf(";"));
        if (!"".equals(sid)) this.sessionID = sid;
        else System.out.println("\u001b[31;1m [Error]  \u001b[36m  qBittorrent:\u001b[0m Cannot login. Please check config.");
        response.close();
        httpClient.close();
    }

    @Override
    public HashSet<Torrents> acquireTorrents(TorrentState state) throws IOException {

        String des = "";
        switch (state) {
            default:
            case ALL: des = "/api/v2/torrents/info?filter=all"; System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Acquiring all torrents ..."); break;
            case DOWNLOADING: des = "/api/v2/torrents/info?filter=downloading"; System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Acquiring downloading torrents ..."); break;
            case COMPLETED: des = "/api/v2/torrents/info?filter=completed"; System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Acquiring completed torrents ..."); break;
            case PAUSED: des = "/api/v2/torrents/info?filter=paused"; System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Acquiring paused torrents ..."); break;
            case ACTIVE: des = "/api/v2/torrents/info?filter=active"; System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Acquiring active torrents ..."); break;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + this.getPort() + des);
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + this.getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        CloseableHttpResponse response = httpClient.execute(httpget);
        String responseString = "";
        HttpEntity entity = response.getEntity();
        HashSet<QbittorrentTorrents> qbittorrentTorrents = null;
        if (entity != null) {
            responseString = EntityUtils.toString(entity) ;
            qbittorrentTorrents = decodeRawList(responseString);
            if (state.equals(TorrentState.ALL)) this.setAllTorrents(qbittorrentTorrents);
            if (state.equals(TorrentState.COMPLETED)) this.completedTorrents = qbittorrentTorrents;
            if (state.equals(TorrentState.DOWNLOADING)) {
                this.downloadingTorrents = qbittorrentTorrents;
                this.setDownloadAmount(qbittorrentTorrents.size());
            }
        } else System.out.println("\u001b[33;1m [Warning]\u001b[36m  qBittorrent:\u001b[0m Cannot acquire torrents.");
        response.close();
        httpClient.close();
        this.setCount(this.getCount() + 1);
        HashSet<Torrents> torrents = new HashSet<>();
        torrents.addAll(qbittorrentTorrents);
        return torrents;
    }

    private HashSet<QbittorrentTorrents> decodeRawList(String raw) {
//        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Decoding all torrents ...");
        Gson gson = new Gson();
        QbittorrentTorrentsListResponse qbittorrentTorrentsListResponse = gson.fromJson("{\"qbittorrentTorrents\":" + raw + "}", QbittorrentTorrentsListResponse.class);
        return qbittorrentTorrentsListResponse.getQbittorrentTorrents();
    }

    @Override
    public void acquireGlobalInfo() throws IOException {
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Acquiring global status ...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + this.getPort() + "/api/v2/transfer/info");
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + this.getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        CloseableHttpResponse response = httpClient.execute(httpget);
        String responseString = "";
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            responseString = EntityUtils.toString(entity) ;
            Gson gson = new Gson();
            QbittorrentGlobalInfo qbittorrentGlobalInfo = gson.fromJson(responseString, QbittorrentGlobalInfo.class);
            this.setDownloadSpeed(qbittorrentGlobalInfo.getDl_info_speed());
            this.setUploadSpeed(qbittorrentGlobalInfo.getUp_info_speed());
            if (this.getCount() ==1) {
                this.setAverageUp(this.getUploadSpeed());
                this.setAverageDown(this.getDownloadSpeed());
            }
            else {
                this.setAverageUp((this.getAverageUp() * (this.getCount() - 1) + this.getUploadSpeed()) / this.getCount());
                this.setAverageDown((this.getAverageDown() * (this.getCount() - 1) + this.getDownloadSpeed()) / this.getCount());
            }
            acquireTorrents(TorrentState.ALL);
            this.allTorrents.forEach(torrent -> {
                this.setTorrentSize(this.getTorrentSize() + ((QbittorrentTorrents) torrent).getTotal_size());
            });
            System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Current upload speed is \u001b[33m" +
                    new DecimalFormat("#0.00").format(this.getUploadSpeed() / (double)1048576) + " MB/s\u001b[0m (Avg. " +
                    new DecimalFormat("#0.00").format(this.getAverageUp() / (double)1048576) + " MB/s) .");
        } else System.out.println("\u001b[33;1m [Warning]\u001b[36m  qBittorrent:\u001b[0m Cannot acquire global status.");
        response.close();
        httpClient.close();

        checkIO();
    }

    private void checkIO() throws IOException {
        if ((this.getUploadSpeed() + this.getDownloadSpeed()) < (this.getAverageUp() + this.getDownloadSpeed()) / (double)10){
            System.out.println("\u001b[33;1m [Warning]\u001b[36m  qBittorrent:\u001b[0m Maybe having some I/O problem. Try to solve it.");
            StringBuilder stringBuilder = new StringBuilder();
            this.downloadingTorrents.forEach(torrent -> {
                stringBuilder.append(((QbittorrentTorrents) torrent).getHash() + "|");
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            pauseTorrent(stringBuilder.toString());
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startTorrents(stringBuilder.toString());
        }
    }

    @Override
    public boolean addTorrent(String link, String name, String hash, Long size, float downloadLimit, float uploadLimit) throws IOException {
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Adding torrent " + link.substring(0, link.indexOf("passkey") - 1) + " ...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:" + getPort() + "/api/v2/torrents/add");
        httpPost.addHeader("User-Agent", "BoxHelper");
        httpPost.addHeader("Host", "127.0.0.1:" + getPort());
        httpPost.addHeader("Cookie", "SID=" + this.sessionID);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addTextBody("urls", link);
        multipartEntityBuilder.addTextBody("dlLimit", (int)(downloadLimit * 1024 * 1024) + "");
        multipartEntityBuilder.addTextBody("upLimit", (int)(uploadLimit * 1024 * 1024) + "");
        httpPost.setEntity(multipartEntityBuilder.build());
        httpClient.execute(httpPost);
        httpClient.close();
        boolean added =recheck(hash, name, size, TorrentState.DOWNLOADING);
        if (added) {
            System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Successfully added torrent " + link.substring(0, link.indexOf("passkey") - 1) + " .");
            acquireTorrents(TorrentState.DOWNLOADING);
            for (Object torrent:this.downloadingTorrents) {
                if (((QbittorrentTorrents) torrent).getName().contains(name) || ((QbittorrentTorrents) torrent).getName().contains(name.replaceAll("\\s", "\\."))) {
                    hash = ((QbittorrentTorrents) torrent).getHash();
                    break;
                }
            }
        }
        else System.out.println("\u001b[33;1m [Warning]\u001b[36m  qBittorrent:\u001b[0m Cannot add torrent " + link.substring(0, link.indexOf("passkey") - 1) + " . Retry later...");
        return added;
    }

    private boolean recheck(String hash, String name, Long size, TorrentState torrentState) throws IOException {

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String des = "";
        switch (torrentState) {
            default:
            case ALL: des = "/api/v2/torrents/info?filter=all"; break;
            case DOWNLOADING: des = "/api/v2/torrents/info?filter=downloading"; break;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + this.getPort() + des);
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        CloseableHttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();
        boolean exist = false;
        if (entity != null) {
            String responseString = EntityUtils.toString(entity) ;
            if (hash != null && !"".equals(hash)) {
                if (responseString.contains(hash)) {
                    exist = true;
                }
            } else {
                for (Torrents torrent: decodeRawList(responseString)){
                    if (torrent.getName().replaceAll("[\\.\\s]", "").equals(name.replaceAll("[\\.\\s]", ""))){
                        exist = true;
                    }
                    if (((System.currentTimeMillis() / 1000 - torrent.getAdded_on()) < 180) && Math.abs(torrent.getTotal_size() - size) < 10000000){
                        exist = true;
                    }
                }
            }
        }
        response.close();
        httpClient.close();
        return exist;
    }

    @Override
    public boolean removeTorrent(String link, String name) throws IOException {
        if (link == null || "".equals(link) || name == null || "".equals(name)) return true;
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Removing torrent " + name + " ...");
        String hash = null;
        Long size = 0L;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        for (Object torrent: this.allTorrents){
            if (((QbittorrentTorrents) torrent).getName().contains(name) || ((QbittorrentTorrents) torrent).getName().contains(name.replaceAll("\\s", "\\."))){
                hash = ((QbittorrentTorrents) torrent).getHash();
                size = ((QbittorrentTorrents) torrent).getTotal_size();
                break;
            }
        }
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + getPort() + "/api/v2/torrents/delete?hashes=" + hash + "&deleteFiles=true");
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        httpClient.execute(httpget);
        boolean removed = recheck(hash, name, size, TorrentState.ALL);
        if (removed) System.out.println("\u001b[33;1m [Warning]\u001b[36m  qBittorrent:\u001b[0m " + name + " did not removed. Retry later...");
        else System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m " + name + " successfully removed.");
        httpClient.close();
        return removed;
    }

    @Override
    public boolean removeTorrent(String hash) throws IOException {
        if (hash == null || "".equals(hash)) return true;
        String name = null;
        Long size = 0L;
        for (Object torrent: this.allTorrents){
            if (((QbittorrentTorrents) torrent).getHash().equals(hash)){
                name = ((QbittorrentTorrents) torrent).getName();
                size = ((QbittorrentTorrents) torrent).getTotal_size();
                break;
            }
        }
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Removing torrent " + name +" ...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + getPort() + "/api/v2/torrents/delete?hashes=" + hash + "&deleteFiles=true");
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        httpClient.execute(httpget);
        boolean removed = recheck(hash, name, size, TorrentState.ALL);
        if (removed)  System.out.println("\u001b[33;1m [Warning]\u001b[36m  qBittorrent:\u001b[0m " + name + " did not removed. Retry later...");
        else System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m " + name + " successfully removed.");
        httpClient.close();
        return removed;
    }

    @Override
    public void pauseTorrents(String hashes) throws IOException {
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Pausing a bulk of torrents...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + getPort() + "/api/v2/torrents/pause?hashes=" + hashes);
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        httpClient.execute(httpget);
        httpClient.close();
    }

    @Override
    public void pauseTorrent(String hash) throws IOException {

    }

    @Override
    public void startTorrents(String hashes) throws IOException {
        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Starting a bulk of torrents...");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:" + getPort() + "/api/v2/torrents/resume?hashes=" + hashes);
        httpget.addHeader("User-Agent", "BoxHelper");
        httpget.addHeader("Host", "127.0.0.1:" + getPort());
        httpget.addHeader("Cookie", "SID=" + this.sessionID);
        httpClient.execute(httpget);
        httpClient.close();
    }

    @Override
    public void startTorrent(String hash) throws IOException {

    }


}
