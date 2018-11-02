package models.torrent;

/**
 * Created by SpereShelde on 2018/11/1.
 */
public class TRTorrent {

    private int status, id;
    private long activityDate, addedDate, doneDate, rateDownload, rateUpload, totalSize;
    private double uploadRatio;

    public TRTorrent(int id, int status, long activityDate, long addedDate, long doneDate, long rateDownload, long rateUpload, long totalSize, double uploadRatio) {
        this.status = status;
        this.id = id;
        this.activityDate = activityDate;
        this.addedDate = addedDate;
        this.doneDate = doneDate;
        this.rateDownload = rateDownload;
        this.rateUpload = rateUpload;
        this.totalSize = totalSize;
        this.uploadRatio = uploadRatio;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(long activityDate) {
        this.activityDate = activityDate;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    public long getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(long doneDate) {
        this.doneDate = doneDate;
    }

    public long getRateDownload() {
        return rateDownload;
    }

    public void setRateDownload(long rateDownload) {
        this.rateDownload = rateDownload;
    }

    public long getRateUpload() {
        return rateUpload;
    }

    public void setRateUpload(long rateUpload) {
        this.rateUpload = rateUpload;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public double getUploadRatio() {
        return uploadRatio;
    }

    public void setUploadRatio(double uploadRatio) {
        this.uploadRatio = uploadRatio;
    }
}
