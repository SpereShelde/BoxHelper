package models;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SpereShelde on 2018/6/9.
 *
 * For example: chdbits;
 */
public class Type1{

    private String website, username, password, passkey;
    private ArrayList<Cookie> cookies = new ArrayList<>();

    public Type1(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
        getCookieAndPasskey();
    }

    public Type1() {
    }

    public ArrayList<Cookie> getCookies() {
        return cookies;
    }

    public String getPasskey() {
        return passkey;
    }

    private void getCookieAndPasskey() {

        System.out.println("Acquiring cookie and passkey of " + website + "...");
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
        driver.manage().deleteAllCookies();
        driver.setJavascriptEnabled(false);
        driver.get("https://" + this.website + "/login.php");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(new By.ByXPath("//*[@id=\"nav_block\"]/form[2]/table/tbody/tr[1]/td[2]/input")));
        driver.findElementByXPath("//*[@id=\"nav_block\"]/form[2]/table/tbody/tr[1]/td[2]/input").sendKeys(this.username);
        driver.findElementByXPath("//*[@id=\"nav_block\"]/form[2]/table/tbody/tr[2]/td[2]/input").sendKeys(this.password);
        driver.findElementByXPath("//*[@id=\"nav_block\"]/form[2]/table/tbody/tr[7]/td/input[1]").click();
        driver.get("https://" + this.website + "/index.php");
        if (!("https://" + this.website + "/index.php").equals(driver.getCurrentUrl())) {
            System.out.println("Username or Password error!");
            System.exit(100);
        }
        String cookieA = driver.manage().getCookies().toString();
        driver.get("https://" + this.website + "/details.php?id=1000&hit=1");
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#outer > table.details > tbody > tr:nth-child(5) > td.rowfollow > a")));
        String link = driver.findElementByCssSelector("#outer > table.details > tbody > tr:nth-child(5) > td.rowfollow > a").getAttribute("outerHTML");
        String[] temp = link.split("=");
        String passkeyA = temp[3].substring(0, 32);
        if (!"".equals(cookieA) && !"".equals(passkeyA)) {
            String[] fragments = cookieA.split("=|; ");
            driver.setJavascriptEnabled(false);
            driver.get("https://" + this.website + "/login.php");
            String time="2038-1-19 11:14:07";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long timestamp = date.getTime();
            this.cookies.add(new Cookie(fragments[0].substring(1, fragments[0].length()), fragments[1], ".chdbits.co", "/", new Date(new Date().getTime() + 365*24*60*60*1000), false, true));
            this.cookies.add(new Cookie(fragments[7].substring(fragments[7].indexOf(",") + 2), fragments[8], "chdbits.co", "/", new Date(timestamp)));
            this.cookies.add(new Cookie(fragments[14].substring(fragments[14].indexOf(",") + 2), fragments[15], "chdbits.co", "/", new Date(timestamp)));
            this.cookies.add(new Cookie(fragments[21].substring(fragments[21].indexOf(",") + 2), fragments[22], "chdbits.co", "/", new Date(timestamp)));
            this.cookies.add(new Cookie(fragments[28].substring(fragments[18].indexOf(",") + 2), fragments[29], "chdbits.co", "/", new Date(timestamp)));
            this.cookies.add(new Cookie(fragments[35].substring(fragments[35].indexOf(",") + 2), fragments[36], "chdbits.co", "/", new Date(timestamp)));
            this.passkey = passkeyA;
            System.out.println("Done.\n");
        } else {
            System.out.println("Unable to acquire cookie and passkey...");
            System.exit(101);
        }
        driver.close();
    }

}
