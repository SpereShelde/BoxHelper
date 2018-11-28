package controller;

import entity.clients.Client;
import entity.clients.Torrents;
import entity.sites.torrents.RawTorrent;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by SpereShelde on 2018/11/19.
 */
public class ClientsController implements Runnable{

    private HashSet<Client> clients;
    private RawTorrent torrent;
    private Torrents torrentToDelete;
    private String torrentToWatch; //hash
    private Long totalSpeedToWatch;
    private Long totalDown, totalUp, averageUp, averageDown, diskUsed, diskLimit;
    private Client suitableClient;
    private Properties properties;
    private ArrayList<String> addedTorrents;

    public ArrayList<String> getAddedTorrents() {
        return addedTorrents;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setTorrent(RawTorrent torrent) {
        this.torrent = torrent;
    }

    public ClientsController(HashSet<Client> clients) {

        this.clients = clients;
        this.torrentToDelete = new Torrents();
        this.torrentToWatch = "";
        this.totalSpeedToWatch = 0L;
        this.totalDown = 0L;
        this.totalUp = 0L;
        this.averageUp = 0L;
        averageDown = 0L;
        diskUsed = 0L;
        diskLimit = 0L;
        this.addedTorrents = new ArrayList<>();
    }

    private void selectTorrentToDelete(){
        HashSet<Torrents> completedTorrents = new HashSet<>();
        for (Client c : clients) {
            try {
                c.acquireTorrents(Client.TorrentState.COMPLETED);
            } catch (IOException e) {
                e.printStackTrace();
            }
            completedTorrents.addAll(c.getCompletedTorrents());
        }
        this.torrentToDelete = new Torrents(1577808000, 1577808000, 0, 1048576000);
        switch (properties.getProperty("delete_rule")){
            default:
            case "act":
                completedTorrents.forEach(torrent -> {
                    if (torrent.getLast_activity() < this.torrentToDelete.getLast_activity()) this.torrentToDelete = torrent;
                });
                break;
            case "add":
                completedTorrents.forEach(torrent -> {
                    if (torrent.getAdded_on() < this.torrentToDelete.getAdded_on()) this.torrentToDelete = torrent;
                });
                break;
            case "slow":
                completedTorrents.forEach(torrent -> {
                    if (torrent.getUpspeed() < this.torrentToDelete.getUpspeed()) this.torrentToDelete = torrent;
                });
                break;
            case "large":
                completedTorrents.forEach(torrent -> {
                    if (torrent.getTotal_size() > this.torrentToDelete.getTotal_size()) this.torrentToDelete = torrent;
                });
                break;
        }
    }

    private void deleteTorrent(){
        for (Client c : clients) {
            if (c.getClass().toString().toLowerCase().contains(this.torrentToDelete.getClients().toString().toLowerCase())){
                try {
                    c.removeTorrent(this.torrentToDelete.getHash());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {

        int downAmount = 100;
        for (Client c : clients) {
            try {
                c.acquireGlobalInfo();
                this.totalUp += c.getUploadSpeed();
                this.totalDown += c.getDownloadSpeed();
                this.averageUp += c.getAverageUp();
                this.averageDown += c.getAverageDown();
                this.diskUsed += c.getTorrentSize();
                if (c.getDownloadAmount() < downAmount) {
                    downAmount = c.getDownloadAmount();
                    this.suitableClient = c;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Total download speed is \u001b[33m" +
                new DecimalFormat("#0.00").format(this.totalDown / (double) 1048576) + " MB/s\u001b[0m (Avg. " +
                new DecimalFormat("#0.00").format(this.averageDown / (double) 1048576) + " MB/s) .");
        System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Total upload speed is \u001b[33m" +
                new DecimalFormat("#0.00").format(this.totalUp / (double) 1048576) + " MB/s\u001b[0m (Avg. " +
                new DecimalFormat("#0.00").format(this.averageUp / (double) 1048576) + " MB/s) .");
        if (properties.getProperty("sound_out").equals("true") && this.torrentToWatch != null){
            if (this.totalSpeedToWatch > (this.totalUp + this.totalDown) * 1.2) {
                System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m The newly added torrent lowered the total speed. Remove it.");
                this.torrentToDelete = new Torrents(this.torrentToWatch);
                System.out.println("test: ttw" + this.torrentToWatch);
                deleteTorrent();
                this.torrentToWatch = null;
                this.totalSpeedToWatch = 0L;
            }
        }
        if (this.diskUsed / 1073741824 < Float.parseFloat(properties.getProperty("disk_limit"))) {
            System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Total size of all torrents in BoxHelper is \u001b[33m" + new DecimalFormat("#0.00").format(this.diskUsed / 1073741824) +
                    " GB\u001b[0m, which is within the limit of disk usage (" + Integer.parseInt(properties.getProperty("disk_limit"))
                    + " GB).");
            if ("true".equals(properties.getProperty("super_mode"))) {
                System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m \u001b[33mSuper mode\u001b[0m is \u001b[33mON\u001b[0m.");
                if (Double.parseDouble(properties.getProperty("expect_total_speed")) * 1048576 > this.totalUp) {
                    System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Upload speed is not statisfied. Try to add new torrent.");
                    try {
                        if (this.torrent != null) {
                            if (this.suitableClient.addTorrent(this.torrent.getLink(), this.torrent.getName(), this.torrent.getHash(), this.torrent.getDownloadLimit(), this.torrent.getUploadLimit())) this.torrentToWatch = this.torrent.getHash();

                            if (this.torrentToWatch != null && !this.torrentToWatch.equals("")) this.addedTorrents.add(this.torrent.getDomain() + "-" + this.torrent.getId());
                            this.totalSpeedToWatch = this.totalUp + this.totalDown;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Upload speed reached expectation.");
                    if (properties.getProperty("pause_seeding_torrent").equals("true")) {
                        System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Pause seeding torrents and stop adding new torrents.");
                        for (Client c : clients) {
                            HashSet activeTorrents = new HashSet();
                            try {
                                activeTorrents.addAll(c.acquireTorrents(Client.TorrentState.ACTIVE));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            StringBuilder stringBuilder = new StringBuilder();
                            activeTorrents.forEach(torrent -> {
                                if (((Torrents) torrent).getUpspeed() < Float.parseFloat(properties.getProperty("expect_torrent_up"))) stringBuilder.append(((Torrents) torrent).getHash() + "|");
                            });
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            try {
                                c.pauseTorrents(stringBuilder.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Stop adding new torrents.");
                    }
                }
            } else {
                System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m \u001b[33mSuper mode\u001b[0m is \u001b[33mOFF\u001b[0m. Try to add new torrent immediately.");
                Object[] clis = this.clients.toArray();
                this.suitableClient = (Client) clis[(int) (System.currentTimeMillis() % clis.length)];
                try {
                    if (this.torrent != null){
                        this.torrentToWatch = this.torrent.getHash();
                        this.suitableClient.addTorrent(this.torrent.getLink(), this.torrent.getName(), this.torrent.getHash(), this.torrent.getDownloadLimit(), this.torrent.getUploadLimit());

                        if (this.torrentToWatch != null && !this.torrentToWatch.equals("")) this.addedTorrents.add(this.torrent.getDomain() + "-" + this.torrent.getId());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (this.diskUsed / 1073741824 - Float.parseFloat(properties.getProperty("disk_limit")) > 100){
            System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Total size of allTorrents in BoxHelper is " + new DecimalFormat("#0.00").format(this.diskUsed) +
                    " GB, which is out of the limit of disk usage a lot. (" + Integer.parseInt(properties.getProperty("disk_limit"))
                    + " GB). Abort adding torrent and delete torrent immediately.");
            selectTorrentToDelete();
            deleteTorrent();
        } else {
            System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Total size of allTorrents in BoxHelper is " + new DecimalFormat("#0.00").format(this.diskUsed) +
                    " GB, which reached the limit of disk usage (" + Integer.parseInt(properties.getProperty("disk_limit"))
                    + " GB). Abort adding torrent and seek for chance to delete torrents.");
            if (3 * this.totalUp < Double.parseDouble(properties.getProperty("expect_total_speed"))) {
                System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Current speed is relatively low. Try to delete torrent.");
                selectTorrentToDelete();
                deleteTorrent();
            } else {
                System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Current speed is relatively high. Wait another chance to delete torrent.");
            }
        }
        this.totalUp = 0L;
        this.totalDown = 0L;
        this.averageUp = 0L;
        this.diskUsed = 0L;

        for (Client c : clients) {
            c.reset();
        }
    }
}
