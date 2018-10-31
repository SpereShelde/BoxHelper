package models.torrent.deTorrent;

/**
 * Created by SpereShelde on 2018/10/18.
 */
public class DETorrent{

    private String name, hash, state;
    private long total_wanted, time_added, upload_payload_rate;
    private double ratio;

    public DETorrent(String name, String hash, String state, long total_wanted, long time_added, long upload_payload_rate, double ratio) {
        this.state = state;
        this.name = name;
        this.hash = hash;
        this.total_wanted = total_wanted;
        this.time_added = time_added;
        this.upload_payload_rate = upload_payload_rate;
        this.ratio = ratio;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTotal_wanted() {
        return total_wanted;
    }

    public void setTotal_wanted(long total_wanted) {
        this.total_wanted = total_wanted;
    }

    public long getTime_added() {
        return time_added;
    }

    public void setTime_added(long time_added) {
        this.time_added = time_added;
    }

    public long getUpload_payload_rate() {
        return upload_payload_rate;
    }

    public void setUpload_payload_rate(long upload_payload_rate) {
        this.upload_payload_rate = upload_payload_rate;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

}
