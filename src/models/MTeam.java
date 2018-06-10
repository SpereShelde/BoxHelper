package models;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;


/**
 * Created by SpereShelde on 2018/5/31.
 */
public class MTeam{

    private String username, password, passkey;
    private ArrayList<Cookie> cookies = new ArrayList<>();

    public MTeam(String username, String password) {
        this.username = username;
        this.password = password;
        getCookieAndPasskey();
    }

    public MTeam() {
    }

    public ArrayList<Cookie> getCookies() {
        return cookies;
    }

    public String getPasskey() {
        return passkey;
    }

    private void getCookieAndPasskey() {

        System.out.println("Acquiring cookie and passkey of M-Team...");
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
        driver.manage().deleteAllCookies();
        driver.setJavascriptEnabled(false);
        driver.get("https://tp.m-team.cc/login.php");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(new By.ByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[1]/td[2]/input")));
        driver.findElementByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[1]/td[2]/input").sendKeys(this.username);
        driver.findElementByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[2]/td[2]/input").sendKeys(this.password);
        driver.findElementByXPath("//*[@id=\"portal-login\"]/div[1]/div/form/table/tbody/tr[3]/td/input").click();
        driver.get("https://tp.m-team.cc/index.php");
        if (!"https://tp.m-team.cc/index.php".equals(driver.getCurrentUrl())) {
            System.out.println("Username or Password error!");
            System.exit(100);
        }
        String cookieA = driver.manage().getCookies().toString();
        driver.get("https://tp.m-team.cc/details.php?id=1000&hit=1");
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#outer > table:nth-child(6) > tbody > tr:nth-child(7) > td.rowfollow > b:nth-child(1) > a:nth-child(1)")));
        String link = driver.findElementByCssSelector("#outer > table:nth-child(6) > tbody > tr:nth-child(7) > td.rowfollow > b:nth-child(1) > a:nth-child(1)").getAttribute("outerHTML");
        String[] temp = link.split("=");
        String passkeyA = temp[temp.length - 1].substring(0, 32);
        if (!"".equals(cookieA) && !"".equals(passkeyA)) {
            String[] fragments = cookieA.split("=|; ");
            this.cookies.add(new Cookie(fragments[0].substring(1, fragments[0].length()), fragments[1], fragments[7].substring(0, fragments[7].length() - 1), fragments[5], new Date(new Date().getTime() + 60*24*60*60*1000)));
            this.passkey = passkeyA;
            System.out.println("Done.\n");
        } else {
            System.out.println("Unable to acquire cookie and passkey...");
            System.exit(101);
        }
        driver.close();
    }

}
