package models.pt;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SpereShelde on 2018/10/17.
 */
public class PTChecker {

    public static Pt dispatch(String url, String sizeSpeed, HtmlUnitDriver driver, String cli, String[] config, boolean load){
        String[] ss =sizeSpeed.split("/");
        Pt pt = new Pt();
        if (NexusPHP.isNexusPHP(url)) {
            pt = new NexusPHP(url, cli, Double.parseDouble(ss[0]), Double.parseDouble(ss[1]), Double.parseDouble(ss[2]), Double.parseDouble(ss[3]), (HtmlUnitDriver) driver, config, load);
        }// Check the type
        return pt;
    }

}
