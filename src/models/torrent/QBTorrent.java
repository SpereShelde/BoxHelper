package models.torrent;

/**
 * Created by SpereShelde on 2018/7/2.
 */
public class QBTorrent {

    private String name, hash;
    private long added_on, completion_on, last_activity, size;
    private int dl_limit, up_limit, num_incomplete;
    private double ratio;

    public QBTorrent(String name, String hash, long added_on, long completion_on, long last_activity, long size, int dl_limit, int up_limit, int num_incomplete, double ratio) {
        this.name = name;
        this.hash = hash;
        this.added_on = added_on;
        this.completion_on = completion_on;
        this.last_activity = last_activity;
        this.size = size;
        this.dl_limit = dl_limit;
        this.up_limit = up_limit;
        this.num_incomplete = num_incomplete;
        this.ratio = ratio;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }

    public long getAdded_on() {
        return added_on;
    }

    public long getCompletion_on() {
        return completion_on;
    }

    public long getLast_activity() {
        return last_activity;
    }

    public long getSize() {
        return size;
    }

    public int getDl_limit() {
        return dl_limit;
    }

    public int getUp_limit() {
        return up_limit;
    }

    public int getNum_incomplete() {
        return num_incomplete;
    }

    public double getRatio() {
        return ratio;
    }
}
