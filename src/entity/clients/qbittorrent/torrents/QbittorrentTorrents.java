package entity.clients.qbittorrent.torrents;

import entity.clients.Client;
import entity.clients.Torrents;

/**
 * Created by SpereShelde on 2018/11/20.
 */
public class QbittorrentTorrents extends Torrents {

    /**
     * added_on : 1542627885
     * amount_left : 0
     * auto_tmm : false
     * category : boxHelper
     * completed : 115294982948
     * completion_on : 1542644800
     * dl_limit : 0
     * dlspeed : 0
     * downloaded : 115310675712
     * downloaded_session : 27758607201
     * eta : 8640000
     * f_l_piece_prio : false
     * force_start : false
     * hash : a092551db1ecf51e4d70867d464dbe45162ad1b9
     * last_activity : 1542785556
     * magnet_uri : magnet:?xt=urn:btih:a123
     * max_ratio : -1
     * max_seeding_time : -1
     * name : [爱国者].Patriot.2018.Complete.WEB-DL.4K.H264.AAC-CMCTV
     * num_complete : 31
     * num_incomplete : 1
     * num_leechs : 0
     * num_seeds : 0
     * priority : 0
     * progress : 1
     * ratio : 0.2636946013215988
     * ratio_limit : -2
     * save_path : /home/opop/qbittorrent/download/
     * seeding_time_limit : -2
     * seen_complete : 1542645551
     * seq_dl : false
     * size : 115294982948
     * state : stalledUP
     * super_seeding : false
     * tags :
     * time_active : 153906
     * total_size : 115294982948
     * tracker : http://tracker.hdcmct.org/announce.php?passkey=123
     * up_limit : 0
     * uploaded : 30406802660
     * uploaded_session : 26437768778
     * upspeed : 0
     */

//    private long added_on;
//    private String category;
//    private long completion_on;
//    private long dl_limit;
//    private long dlspeed;
//    private String hash;
//    private long last_activity;
//    private String name;
//    private int num_complete;
//    private int num_incomplete;
//    private int num_leechs;
//    private int num_seeds;
//    private double ratio;
//    private String save_path;
//    private String state;
//    private long total_size;
//    private String tracker;
//    private long up_limit;
//    private long upspeed;

//    public QbittorrentTorrents(long added_on, String category, long completion_on, long dl_limit, long dlspeed, String hash, long last_activity, String name, int num_complete, int num_incomplete, int num_leechs, int num_seeds, double ratio, String save_path, String state, long total_size, String tracker, long up_limit, long upspeed) {
//        this.added_on = added_on;
//        this.category = category;
//        this.completion_on = completion_on;
//        this.dl_limit = dl_limit;
//        this.dlspeed = dlspeed;
//        this.hash = hash;
//        this.last_activity = last_activity;
//        this.name = name;
//        this.num_complete = num_complete;
//        this.num_incomplete = num_incomplete;
//        this.num_leechs = num_leechs;
//        this.num_seeds = num_seeds;
//        this.ratio = ratio;
//        this.save_path = save_path;
//        this.state = state;
//        this.total_size = total_size;
//        this.tracker = tracker;
//        this.up_limit = up_limit;
//        this.upspeed = upspeed;
//        this.clients = Client.Clients.QBITTORRENT;
//    }

//    public long getAdded_on() {
//        return added_on;
//    }
//
//    public void setAdded_on(long added_on) {
//        this.added_on = added_on;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public long getCompletion_on() {
//        return completion_on;
//    }
//
//    public void setCompletion_on(long completion_on) {
//        this.completion_on = completion_on;
//    }
//
//    public long getDl_limit() {
//        return dl_limit;
//    }
//
//    public void setDl_limit(long dl_limit) {
//        this.dl_limit = dl_limit;
//    }
//
//    public long getDlspeed() {
//        return dlspeed;
//    }
//
//    public void setDlspeed(long dlspeed) {
//        this.dlspeed = dlspeed;
//    }
//
//    public String getHash() {
//        return hash;
//    }
//
//    public void setHash(String hash) {
//        this.hash = hash;
//    }
//
//    public long getLast_activity() {
//        return last_activity;
//    }
//
//    public void setLast_activity(long last_activity) {
//        this.last_activity = last_activity;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getNum_complete() {
//        return num_complete;
//    }
//
//    public void setNum_complete(int num_complete) {
//        this.num_complete = num_complete;
//    }
//
//    public int getNum_incomplete() {
//        return num_incomplete;
//    }
//
//    public void setNum_incomplete(int num_incomplete) {
//        this.num_incomplete = num_incomplete;
//    }
//
//    public int getNum_leechs() {
//        return num_leechs;
//    }
//
//    public void setNum_leechs(int num_leechs) {
//        this.num_leechs = num_leechs;
//    }
//
//    public int getNum_seeds() {
//        return num_seeds;
//    }
//
//    public void setNum_seeds(int num_seeds) {
//        this.num_seeds = num_seeds;
//    }
//
//    public double getRatio() {
//        return ratio;
//    }
//
//    public void setRatio(double ratio) {
//        this.ratio = ratio;
//    }
//
//    public String getSave_path() {
//        return save_path;
//    }
//
//    public void setSave_path(String save_path) {
//        this.save_path = save_path;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public long getTotal_size() {
//        return total_size;
//    }
//
//    public void setTotal_size(long total_size) {
//        this.total_size = total_size;
//    }
//
//    public String getTracker() {
//        return tracker;
//    }
//
//    public void setTracker(String tracker) {
//        this.tracker = tracker;
//    }
//
//    public long getUp_limit() {
//        return up_limit;
//    }
//
//    public void setUp_limit(long up_limit) {
//        this.up_limit = up_limit;
//    }
//
//    public long getUpspeed() {
//        return upspeed;
//    }
//
//    public void setUpspeed(long upspeed) {
//        this.upspeed = upspeed;
//    }
}
