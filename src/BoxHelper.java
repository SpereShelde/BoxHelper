import controller.ClientsController;
import controller.SitesController;
import entity.clients.Client;
import entity.clients.qbittorrent.Qbittorrent;
import entity.config.Config;
import entity.sites.torrents.RawTorrent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.TreeSet;

import static java.lang.Thread.sleep;

/**
 * Created by SpereShelde on 2018/10/30.
 */
public class BoxHelper {

    private HashSet<RawTorrent> torrents = new HashSet<>();
    private HashSet<String> torrentsInfo = new HashSet<>(); // domain-id, for quick check;
    private int count = 1;

    public static void main(String[] args) {

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        BoxHelper boxHelper = new BoxHelper();
        Properties configProperties = Config.loadConfig();
        Properties pageProperties = Config.loadPages();
        SitesController sitesController = new SitesController(pageProperties, new RawTorrent());
        HashSet<Client> clients = new HashSet<>();
        Qbittorrent qbittorrent = null;
        if ("true".equals(configProperties.getProperty("enable_qBittorrent"))) {
            qbittorrent = new Qbittorrent(Integer.valueOf(configProperties.getProperty("qBittorrent_Web_UI_port")), configProperties.getProperty("qBittorrent_Web_UI_username"), configProperties.getProperty("qBittorrent_Web_UI_password"));
            try {
                qbittorrent.login();
                if (qbittorrent.acquireApiVersion()) clients.add(qbittorrent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ClientsController clientsController = new ClientsController(clients);
        RawTorrent torrent = new RawTorrent();
        while (true) {
            if (boxHelper.count % Integer.parseInt(configProperties.getProperty("turn")) == 1) {
                configProperties = Config.loadConfig();
                clientsController.setProperties(configProperties);
                sitesController.setProperties(pageProperties);
            }
            System.out.println();
            System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m BoxHelper: the " + boxHelper.count + " run.");
//            System.out.println("BoxHelper: acquired total " + boxHelper.torrents.size() + " torrents.");
            sitesController.run();
            clientsController.setTorrent(sitesController.getTorrent());
            clientsController.run();
            boxHelper.count++;
            sitesController.setAddedTorrents(clientsController.getAddedTorrents());
            try {
                long fluctuation = (long)Integer.parseInt(configProperties.getProperty("fluctuation")) * (int)(1000 * Math.random());
                sleep(fluctuation + (long)Integer.parseInt(configProperties.getProperty("cycle")) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sitesController.setAllTorrents(new HashSet<RawTorrent>());
            sitesController.setQualifiedTorrents(new TreeSet<RawTorrent>());
        }

    }
}
//        System.out.println("\u001b[31;1m [Error]  \u001b[34m    BoxHelper:\u001b[0m Cannot acquire passkey...");
//        System.out.println("\u001b[31;1m [Error]  \u001b[36m  qBittorrent:\u001b[0m Cannot acquire passkey...");
//        System.out.println("\u001b[31;1m [Error]  \u001b[32m       Deluge:\u001b[0m Cannot acquire passkey...");
//        System.out.println("\u001b[31;1m [Error]  \u001b[35m Transmission:\u001b[0m Cannot acquire passkey...");
//        System.out.println("\u001b[33;1m [Warning]\u001b[34m    BoxHelper:\u001b[0m Cannot acquire date...");
//        System.out.println("\u001b[33;1m [Warning]\u001b[36m  qBittorrent:\u001b[0m Cannot acquire date...");
//        System.out.println("\u001b[33;1m [Warning]\u001b[32m       Deluge:\u001b[0m Cannot acquire date...");
//        System.out.println("\u001b[33;1m [Warning]\u001b[35m Transmission:\u001b[0m Cannot acquire date...");
//        System.out.println("\u001b[37;1m [Info]   \u001b[34m    BoxHelper:\u001b[0m Cannot acquire date...");
//        System.out.println("\u001b[37;1m [Info]   \u001b[36m  qBittorrent:\u001b[0m Cannot acquire date...");
//        System.out.println("\u001b[37;1m [Info]   \u001b[32m       Deluge:\u001b[0m Cannot acquire date...");
//        System.out.println("\u001b[37;1m [Info]   \u001b[35m Transmission:\u001b[0m Cannot acquire date...");