package models.torrent.deTorrent;

/**
 * Created by SpereShelde on 2018/10/18.
 */
public class Torrents {

    private Long total_wanted;
    private Long time_added;
    private Long upload_payload_rate;
    private Double ratio;
    private String name;

    public Long getTotal_wanted() {
        return total_wanted;
    }

    public void setTotal_wanted(Long total_wanted) {
        this.total_wanted = total_wanted;
    }

    public Long getTime_added() {
        return time_added;
    }

    public void setTime_added(Long time_added) {
        this.time_added = time_added;
    }

    public Long getUpload_payload_rate() {
        return upload_payload_rate;
    }

    public void setUpload_payload_rate(Long upload_payload_rate) {
        this.upload_payload_rate = upload_payload_rate;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "total_wanted:"+getTotal_wanted()
                +",time_added:"+getTime_added()
                +",upload_payload_rate:"+getUpload_payload_rate().toString()
                +",ratio:"+getRatio()
                +",name:"+getName();
    }
}
