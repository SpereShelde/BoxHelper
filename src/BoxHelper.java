import com.gargoylesoftware.htmlunit.BrowserVersion;
import models.cli.de.DEChecker;
import models.pt.NexusPHP;
import models.cli.qb.QBChecker;
import models.pt.PTChecker;
import models.pt.Pt;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import tools.ConvertJson;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

/**
 * Created by SpereShelde on 2018/6/6.
 */
public class BoxHelper {

    private Map configures = new HashMap();
    private Map cookies = new HashMap();
    private Map drivers = new HashMap();
    private int urlNum = 1;

    private void getConfigures() {// Get configures from file.

        try {
            this.configures = ConvertJson.convertConfigure("config.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Path> jsonFiles = new ArrayList<>();

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("cookies"));
            for(Path path : stream){
                if (path.getFileName().toString().endsWith(".json")) {
                    jsonFiles.add(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Loading cookies ...");
        for (Path path: jsonFiles) {
            try {
                String domainName = path.getFileName().toString();
                cookies.put(domainName.substring(0, domainName.lastIndexOf(".")), ConvertJson.convertCookie(path.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> urlSizeSpeedCli = (Map<String, String>) this.configures.get("url_size_speed_cli");
        this.urlNum = urlSizeSpeedCli.size();
        urlSizeSpeedCli.forEach((url, sizeSpeedCli) -> {
            HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45, false);
            String domain = url.substring(url.indexOf("//") + 2, url.indexOf("/", url.indexOf("//") + 2));
            driver.get("https://" + domain);
            ArrayList<Cookie> cookiesT = (ArrayList) cookies.get(domain);
            cookiesT.forEach(cookie -> driver.manage().addCookie(cookie));
            drivers.put(url, driver);
        });

        System.out.println("Initialization done.");
    }

    public static void main(String[] args) {

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.OFF);
        BoxHelper boxHelper = new BoxHelper();
        boxHelper.getConfigures();
        int cpuThreads = Runtime.getRuntime().availableProcessors()<boxHelper.urlNum?Runtime.getRuntime().availableProcessors():boxHelper.urlNum;
        int count  = 1;

        ArrayList<Pt> pts = new ArrayList<>();
        Map<String, String> urlSizeSpeedCli = (Map<String, String>) boxHelper.configures.get("url_size_speed_cli");

        boxHelper.drivers.forEach((url, driver) -> {
            String ussc = urlSizeSpeedCli.get(url.toString());
            Object[] a = (Object[]) boxHelper.configures.get(ussc.substring(0,2) + "Config");
            String[] config = new String[]{a[0].toString(), a[1].toString(), a[2].toString()};
            pts.add(PTChecker.dispatch(url.toString(), ussc.substring(3, ussc.lastIndexOf("/")), (HtmlUnitDriver)driver, ussc.substring(0,2), config, ussc.substring(ussc.lastIndexOf("/") + 1).equals("true")));
        });

        while (true){
            ExecutorService executorService = Executors.newFixedThreadPool(cpuThreads);
            System.out.println("\nBoxHelper " + count + " runs at " + time());
            QBChecker qbChecker = new QBChecker();
            DEChecker deChecker = new DEChecker();
            if (boxHelper.configures.containsKey("deConfig")){
                System.out.println("Checking DE status...");
                Object[] de = (Object[]) boxHelper.configures.get("deConfig");
                String[] deConfig = new String[]{de[0].toString(), de[1].toString(), de[2].toString(), de[3].toString(), de[4].toString()};
                Long space;
                if ("-1".equals(deConfig[2])) space = new Long("102400") * 1073741824;
                else space = new Long(deConfig[2]) * 1073741824;
                deChecker = new DEChecker(deConfig[0], deConfig[1], space, deConfig[3], Integer.parseInt(deConfig[4]));
                deChecker.run();
            }
            if (boxHelper.configures.containsKey("qbConfig")){
                System.out.println("Checking QB status...");
                Object[] qb = (Object[]) boxHelper.configures.get("qbConfig");
                String[] qbConfig = new String[]{qb[0].toString(), qb[1].toString(), qb[2].toString(), qb[3].toString(), qb[4].toString()};
                Long space;
                if ("-1".equals(qbConfig[2])) space = new Long("102400") * 1073741824;
                else space = new Long(qbConfig[2]) * 1073741824;
                qbChecker = new QBChecker(qbConfig[0], qbConfig[1], space, qbConfig[3], Integer.parseInt(qbConfig[4]));
                qbChecker.run();
            }

            if (boxHelper.configures.containsKey("trConfig")){

            }
            if (boxHelper.configures.containsKey("rtConfig")){

            }
            if (qbChecker.getDownloadingAmount() + deChecker.getDownloadingAmount() <= (int)boxHelper.configures.get("downloading_amount")) {
                pts.forEach(pt -> {
                    if (pt instanceof NexusPHP) executorService.submit((NexusPHP) pt);
                });
            } else System.out.println("Total amount of downloading torrents over limit, stop adding torrents...");
            executorService.shutdown();
            deChecker.setDownloadingAmount(0);
            qbChecker.setDownloadingAmount(0);
            try {
                sleep((long) (1000*Double.valueOf(boxHelper.configures.get("cycle").toString())));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
    }
}
