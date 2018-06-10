import com.gargoylesoftware.htmlunit.BrowserVersion;
import models.Spider;
import models.Type1;
import models.MTeam;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import static java.lang.Thread.sleep;

/**
 * Created by SpereShelde on 2018/6/6.
 */
public class BoxHelper {

    private static Map configures = new HashMap();

    private static void getConfigures() {// Get configures from file.

        if (new File("boxHelper.conf").exists()){
            try {
                Object[] config = Files.readAllLines(Paths.get("boxHelper.conf")).toArray();//Read file by lines.
                for (Object o: config) {
                    String s = o.toString().trim();
                    if (!("".equals(s) || s.charAt(0)=='#' || !s.contains("<=>") || s.trim().lastIndexOf("<=>") == s.length() - 3)){
                        String[] line = s.split("<=>");
                        if ("".equals(line[1])) {
                            configures.put(line[0].trim(), " ");//Add configures to Map.
                        } else {
                            configures.put(line[0].trim(), line[1].trim());//Add configures to Map.
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Unable to read file boxHelper.conf");
            }
        }
    }

    public static void main(String[] args) {

        getConfigures();
        int cpuThreads = Runtime.getRuntime().availableProcessors();
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
        driver.manage().deleteAllCookies();
        MTeam mTeam = new MTeam();
        Type1 chdbits = new Type1();
        if (!(configures.get("m-team_id") == null || configures.get("m-team_key") == null || "".equals(configures.get("m-team_id").toString()) || "".equals(configures.get("m-team_key").toString()))) {
            mTeam = new MTeam(configures.get("m-team_id").toString(), configures.get("m-team_key").toString());
        }
        if (!(configures.get("chdbits_id") == null || configures.get("chdbits_key") == null || "".equals(configures.get("chdbits_id").toString()) || "".equals(configures.get("chdbits_key").toString()))) {
            chdbits = new Type1("chdbits.co", configures.get("chdbits_id").toString(), configures.get("chdbits_key").toString());
        }
        int count  = 1;
        String[] Pages = configures.get("urls").toString().split(",");
        Spider spider1 = new Spider("tp.m-team.cc", mTeam.getPasskey(), configures.get("downloadPath").toString(), Double.parseDouble(configures.get("min").toString()), Double.parseDouble(configures.get("max").toString()), driver);
        Spider spider2 = new Spider("chdbits.co", chdbits.getPasskey(), configures.get("downloadPath").toString(), Double.parseDouble(configures.get("min").toString()), Double.parseDouble(configures.get("max").toString()), driver);
        driver.get("https://tp.m-team.cc/login.php");
        for (Cookie cookie : mTeam.getCookies()) {
            driver.manage().addCookie(cookie);
        }
        driver.get("https://chdbits.co/login.php");
        for (Cookie cookie : chdbits.getCookies()) {
            driver.manage().addCookie(cookie);
        }
        while (true){
            ExecutorService executorService = Executors.newFixedThreadPool(cpuThreads);
            System.out.println("\nBoxHelper " + count + " begin at " + time());
            for (String page: Pages) {
                if (page.contains("tp.m-team.cc")){
                    spider1.setUrl(page);
                    executorService.execute(spider1);
                }else if (page.contains("chdbits.co")){
                    spider2.setUrl(page);
                    executorService.execute(spider2);
                }
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
            try {
                sleep((long) (1000*60*Double.valueOf(configures.get("cycle").toString())));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
    }
}
