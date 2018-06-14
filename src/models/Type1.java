package models;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.Random;

/**
 * Created by SpereShelde on 2018/6/9.
 *
 * For example: chdbits;
 */
public class Type1{

    private String website, passkey;
    private HtmlUnitDriver driver;

    public Type1(String website, HtmlUnitDriver driver) {
        this.website = website;
        this.driver = driver;
        getCookieAndPasskey();
    }

    public Type1() {
    }

    public String getPasskey() {
        return passkey;
    }

    private void getCookieAndPasskey() {

        System.out.println("Trying to acquire passkey of " + website + "...");
        boolean done = false;
        Random random = new Random(System.currentTimeMillis());
        int pageNumber = random.nextInt(1000) + 500;
        String passkeyA = "";
        int count = 1;
        while (!done && count <= 60){
            driver.get("https://" + this.website + "/details.php?id=" + pageNumber + "&hit=1");
//            System.out.println(driver.getCurrentUrl());
            if (driver.getPageSource().contains("passkey=")){
                done = true;
                System.out.println("Got");
                String source = driver.getPageSource();
                String[] temp = source.split("passkey=");
                passkeyA = temp[1].substring(0, 32);
            } else {
                if (count <= 20){
                    pageNumber = random.nextInt(1000) + 100;
                } else if (count <= 40){
                    pageNumber = random.nextInt(10000) + 1000;
                }else  {
                    pageNumber = random.nextInt(30000) + 10000;
                }
            }
            count++;
        }
        if (count == 11){
            System.out.println("Cannot get passkey...");
            System.exit(102);
        }

        if (!"".equals(passkeyA)) {
            this.passkey = passkeyA;
            System.out.println("Done.\n");
        } else {
            System.out.println("Unable to acquire passkey...");
            System.exit(101);
        }
    }

}
