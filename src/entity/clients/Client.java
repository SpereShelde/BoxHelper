package entity.clients;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by SpereShelde on 2018/11/19.
 */
public abstract class Client implements Runnable{

    private int port, count, downloadAmount;
    private float apiVersion;
    private Long downloadSpeed, uploadSpeed; //B/s
    private Long torrentSize; // B
    private Long averageUp, averageDown; // B/s
    protected HashSet allTorrents;
    protected HashSet downloadingTorrents;
    protected HashSet completedTorrents;
    public abstract boolean acquireApiVersion() throws IOException;
    public abstract void login() throws IOException;
    public abstract HashSet<Torrents> acquireTorrents(TorrentState state) throws IOException;
    public abstract void acquireGlobalInfo() throws IOException;
    public abstract boolean addTorrent(String link, String name, String hash, Long size, float downloadLimit, float uploadLimit) throws IOException;
    public abstract boolean removeTorrent(String link, String name) throws IOException;
    public abstract boolean removeTorrent(String hash) throws IOException;
    public abstract void pauseTorrents(String hashes) throws IOException;
    public abstract void startTorrents(String hashes) throws IOException;
    public abstract void startTorrent(String hash) throws IOException;
    public abstract void pauseTorrent(String hash) throws IOException;

    public void reset(){
        this.downloadAmount = 0;
        this.downloadSpeed = 0L;
        this.torrentSize = 0L;
    }

    public enum TorrentState {
        DOWNLOADING, COMPLETED, PAUSED, ALL, ACTIVE
    }

    public enum Clients {
        QBITTORRENT, DELUGE, TRANSMISSION
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDownloadAmount() {
        return downloadAmount;
    }

    public void setDownloadAmount(int downloadAmount) {
        this.downloadAmount = downloadAmount;
    }

    public float getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(float apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Long getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(Long downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public Long getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(Long uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    public Long getTorrentSize() {
        return torrentSize;
    }

    public void setTorrentSize(Long torrentSize) {
        this.torrentSize = torrentSize;
    }

    public Long getAverageUp() {
        return averageUp;
    }

    public void setAverageUp(Long averageUp) {
        this.averageUp = averageUp;
    }

    public Long getAverageDown() {
        return averageDown;
    }

    public void setAverageDown(Long averageDown) {
        this.averageDown = averageDown;
    }

    public HashSet getAllTorrents() {
        return allTorrents;
    }

    public void setAllTorrents(HashSet allTorrents) {
        this.allTorrents = allTorrents;
    }

    public HashSet getDownloadingTorrents() {
        return downloadingTorrents;
    }

    public void setDownloadingTorrents(HashSet downloadingTorrents) {
        this.downloadingTorrents = downloadingTorrents;
    }

    public HashSet getCompletedTorrents() {
        return completedTorrents;
    }

    public void setCompletedTorrents(HashSet completedTorrents) {
        this.completedTorrents = completedTorrents;
    }

    @Override
    public void run() {
        try {
            this.acquireGlobalInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
