import models.MTeam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import static java.lang.Thread.sleep;

/**
 * Created by wzf on 2018/6/6.
 */
public class Main {

    public static void main(String[] args) {

        int cpuThreads = Runtime.getRuntime().availableProcessors();
        MTeam mTeam = new MTeam();
        if (mTeam.getConfig()) {
            int count  = 1;
            while (true){
                System.out.println("\nSpider " + count + " begin at " + time());
                ExecutorService executorService = Executors.newFixedThreadPool(cpuThreads);
                String[] pages = mTeam.getConfigures().get("urls").toString().split(",");//!
                for (String page: pages) {
                    mTeam.setCurrentUrl(page.trim());
                    executorService.execute(mTeam);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                executorService.shutdown();
                try {
                    sleep(1000*60*Integer.valueOf(mTeam.getConfigures().get("cycle").toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Cannot get configure...");
        }
    }
}
