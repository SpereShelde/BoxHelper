package entity.clients.qbittorrent.info;

/**
 * Created by SpereShelde on 2018/11/21.
 */
public class QbittorrentGlobalInfo {


    /**
     * connection_status : connected
     * dht_nodes : 0
     * dl_info_data : 426095354758
     * dl_info_speed : 0
     * dl_rate_limit : 0
     * up_info_data : 370572497490
     * up_info_speed : 12665
     * up_rate_limit : 0
     */

    private String connection_status;
    private long dl_info_speed;
    private long dl_rate_limit;
    private long up_info_speed;
    private long up_rate_limit;

    public String getConnection_status() {
        return connection_status;
    }

    public void setConnection_status(String connection_status) {
        this.connection_status = connection_status;
    }

    public long getDl_info_speed() {
        return dl_info_speed;
    }

    public void setDl_info_speed(long dl_info_speed) {
        this.dl_info_speed = dl_info_speed;
    }

    public long getDl_rate_limit() {
        return dl_rate_limit;
    }

    public void setDl_rate_limit(long dl_rate_limit) {
        this.dl_rate_limit = dl_rate_limit;
    }

    public long getUp_info_speed() {
        return up_info_speed;
    }

    public void setUp_info_speed(long up_info_speed) {
        this.up_info_speed = up_info_speed;
    }

    public long getUp_rate_limit() {
        return up_rate_limit;
    }

    public void setUp_rate_limit(long up_rate_limit) {
        this.up_rate_limit = up_rate_limit;
    }
}
