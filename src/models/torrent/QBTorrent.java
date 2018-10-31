package models.torrent;

/**
 * Created by SpereShelde on 2018/7/2.
 */
public class QBTorrent {

    private String name, hash, state;
    private long added_on, completion_on, last_activity, size;
    private int dl_limit, up_limit, num_incomplete;
    private double ratio;

    public QBTorrent(String name, String hash, String state, long added_on, long completion_on, long last_activity, long size, int dl_limit, int up_limit, int num_incomplete, double ratio) {
        this.name = name;
        this.state = state;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getAdded_on() {
        return added_on;
    }

    public void setAdded_on(long added_on) {
        this.added_on = added_on;
    }

    public long getCompletion_on() {
        return completion_on;
    }

    public void setCompletion_on(long completion_on) {
        this.completion_on = completion_on;
    }

    public long getLast_activity() {
        return last_activity;
    }

    public void setLast_activity(long last_activity) {
        this.last_activity = last_activity;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDl_limit() {
        return dl_limit;
    }

    public void setDl_limit(int dl_limit) {
        this.dl_limit = dl_limit;
    }

    public int getUp_limit() {
        return up_limit;
    }

    public void setUp_limit(int up_limit) {
        this.up_limit = up_limit;
    }

    public int getNum_incomplete() {
        return num_incomplete;
    }

    public void setNum_incomplete(int num_incomplete) {
        this.num_incomplete = num_incomplete;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
