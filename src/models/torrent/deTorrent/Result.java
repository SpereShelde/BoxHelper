package models.torrent.deTorrent;

import java.util.Map;

/**
 * Created by SpereShelde on 2018/10/18.
 */
public class Result {

    private Stats stats;
    private boolean connected;
    private Map<String,Torrents> torrents;
    private Filters filters;

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Map<String, Torrents> getTorrents() {
        return torrents;
    }

    public void setTorrents(Map<String, Torrents> torrents) {
        this.torrents = torrents;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }
}
