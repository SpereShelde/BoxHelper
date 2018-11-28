package entity.clients;

/**
 * Created by SpereShelde on 2018/11/21.
 */
public class Torrents {

    protected Client.Clients clients;
    private long added_on;
    private String category;
    private long completion_on;
    private long dl_limit;
    private long dlspeed;
    private String hash;
    private long last_activity;
    private String name;
    private int num_complete;
    private int num_incomplete;
    private int num_leechs;
    private int num_seeds;
    private double ratio;
    private String save_path;
    private String state;
    private long total_size;
    private String tracker;
    private long up_limit;
    private long upspeed;

    public Torrents() {
    }

    public Client.Clients getClients() {
        return clients;
    }

    public void setClients(Client.Clients clients) {
        this.clients = clients;
    }

    public Torrents(String hash) {
        this.hash = hash;
    }

    public Torrents(long added_on, long last_activity, long total_size, long upspeed) {
        this.added_on = added_on;
        this.last_activity = last_activity;
        this.total_size = total_size;
        this.upspeed = upspeed;
    }

    public long getAdded_on() {
        return added_on;
    }

    public void setAdded_on(long added_on) {
        this.added_on = added_on;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCompletion_on() {
        return completion_on;
    }

    public void setCompletion_on(long completion_on) {
        this.completion_on = completion_on;
    }

    public long getDl_limit() {
        return dl_limit;
    }

    public void setDl_limit(long dl_limit) {
        this.dl_limit = dl_limit;
    }

    public long getDlspeed() {
        return dlspeed;
    }

    public void setDlspeed(long dlspeed) {
        this.dlspeed = dlspeed;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getLast_activity() {
        return last_activity;
    }

    public void setLast_activity(long last_activity) {
        this.last_activity = last_activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum_complete() {
        return num_complete;
    }

    public void setNum_complete(int num_complete) {
        this.num_complete = num_complete;
    }

    public int getNum_incomplete() {
        return num_incomplete;
    }

    public void setNum_incomplete(int num_incomplete) {
        this.num_incomplete = num_incomplete;
    }

    public int getNum_leechs() {
        return num_leechs;
    }

    public void setNum_leechs(int num_leechs) {
        this.num_leechs = num_leechs;
    }

    public int getNum_seeds() {
        return num_seeds;
    }

    public void setNum_seeds(int num_seeds) {
        this.num_seeds = num_seeds;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public String getSave_path() {
        return save_path;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getTotal_size() {
        return total_size;
    }

    public void setTotal_size(long total_size) {
        this.total_size = total_size;
    }

    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    public long getUp_limit() {
        return up_limit;
    }

    public void setUp_limit(long up_limit) {
        this.up_limit = up_limit;
    }

    public long getUpspeed() {
        return upspeed;
    }

    public void setUpspeed(long upspeed) {
        this.upspeed = upspeed;
    }
}
