package entity.sites.torrents;

import java.util.Date;

/**
 * Created by SpereShelde on 2018/10/26.
 */
public class RawTorrent implements Comparable {

    private String domain, name, source, uploader, link;
    private double size = 0;
    private Date timeOfLive, timeOfFree = new Date(946656000);
    private int id, seeder, leecher, complete = 0;
    private boolean sticky, free, diy, cnSubtitle, restrict, hr = false;
    private float uploadLimit, downloadLimit;
    private Unit unit;
    private String hash;

    @Override
    public int compareTo(Object o) {
        RawTorrent raw = (RawTorrent) o;
        if (this.timeOfLive.after(raw.timeOfLive)) return 1;
        else return -1;
    }

    @Override
    public boolean equals(Object obj) {
        RawTorrent raw = (RawTorrent) obj;
        return this.timeOfLive.getTime() == raw.timeOfLive.getTime();
    }

    public enum Unit{
        KB, MB, GB, TB, PB
    }

    public RawTorrent() {

    }

    public String getLink() {
        return link;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getUploadLimit() {
        return uploadLimit;
    }

    public void setUploadLimit(float uploadLimit) {
        this.uploadLimit = uploadLimit;
    }

    public float getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(float downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Date getTimeOfLive() {
        return timeOfLive;
    }

    public void setTimeOfLive(Date timeOfLive) {
        this.timeOfLive = timeOfLive;
    }

    public Date getTimeOfFree() {
        return timeOfFree;
    }

    public void setTimeOfFree(Date timeOfFree) {
        this.timeOfFree = timeOfFree;
    }

    public int getSeeder() {
        return seeder;
    }

    public void setSeeder(int seeder) {
        this.seeder = seeder;
    }

    public int getLeecher() {
        return leecher;
    }

    public void setLeecher(int leecher) {
        this.leecher = leecher;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isDiy() {
        return diy;
    }

    public void setDiy(boolean diy) {
        this.diy = diy;
    }

    public boolean isCnSubtitle() {
        return cnSubtitle;
    }

    public void setCnSubtitle(boolean cnSubtitle) {
        this.cnSubtitle = cnSubtitle;
    }

    public boolean isRestrict() {
        return restrict;
    }

    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }

    public boolean isHr() {
        return hr;
    }

    public void setHr(boolean hr) {
        this.hr = hr;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "RawTorrent{" +
                "\nsource='" + source + '\'' +
                ", \ndomain='" + domain + '\'' +
                ", \nname='" + name + '\'' +
                ", \nid=" + id +
                ", \nsize=" + size +
                ", \nunit=" + unit +
                ", \ntimeOfLive=" + timeOfLive +
                ", \ntimeOfFree=" + timeOfFree +
                ", \nuploader='" + uploader + '\'' +
                ", \nseeder=" + seeder +
                ", \nleecher=" + leecher +
                ", \ncomplete=" + complete +
                ", \nisSticky=" + sticky +
                ", \nisFree=" + free +
                ", \nisDiy=" + diy +
                ", \nisCnSubtitle=" + cnSubtitle +
                ", \nisRestrict=" + restrict +
                ", \nisHr=" + hr +
                "\n}";
    }
}
