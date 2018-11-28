package entity.clients.qbittorrent.torrents;

import java.util.HashSet;

/**
 * Created by SpereShelde on 2018/11/21.
 */
public class QbittorrentTorrentsListResponse {

    private HashSet<QbittorrentTorrents> qbittorrentTorrents;

    public QbittorrentTorrentsListResponse(HashSet<QbittorrentTorrents> qbittorrentTorrents) {
        this.qbittorrentTorrents = qbittorrentTorrents;
    }

    public HashSet<QbittorrentTorrents> getQbittorrentTorrents() {
        return qbittorrentTorrents;
    }

    public void setQbittorrentTorrents(HashSet<QbittorrentTorrents> qbittorrentTorrents) {
        this.qbittorrentTorrents = qbittorrentTorrents;
    }
}
