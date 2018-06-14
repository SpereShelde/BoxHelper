package tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.Cookie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static java.nio.file.Paths.get;

/**
 * Created by wzf on 2018/6/11.
 */
public class ConvertJson {

    public static Map convertConfigure(String jsonFileName) throws IOException {

        ArrayList<String> urls = new ArrayList<>();
        Map configures = new HashMap();
        JsonParser jsonParser = new JsonParser();
        String content = null;

        if (new File(jsonFileName).exists()){
            content = new String(Files.readAllBytes(get(jsonFileName)));
        }

        JsonObject object = (JsonObject) jsonParser.parse(content);

        if (object.has("urls")) {
            JsonArray array = object.get("urls").getAsJsonArray();
            for (JsonElement a: array) {
                if (a != null || !"".equals(a.getAsString())) urls.add(a.getAsString());
            }
            configures.put("urls", urls);
        }
        if (object.has("min")) {
            configures.put("min", object.get("min").getAsDouble());
        }
        if (object.has("max")) {
            configures.put("max", object.get("max").getAsDouble());
        }
        if (object.has("path")) {
            configures.put("path", object.get("path").getAsString());
        }
        if (object.has("cycle")) {
            configures.put("cycle", object.get("cycle").getAsDouble());
        }
        return configures;
    }

    public static ArrayList<Cookie> convertCookie(String jsonFileName) throws IOException {

        String domain = "", name = "", value = "", path = "";
        Long expirationDate = Long.valueOf(0);
        boolean hostOnly = false, secure = false;
        ArrayList<Cookie> cookies = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        String content = "";

        if (new File(jsonFileName).exists()){
            content = new String(Files.readAllBytes(get(jsonFileName)));
        }

        JsonArray jsonArray = (JsonArray) jsonParser.parse(content);

        Long timestamp = null;
        for (JsonElement array: jsonArray) {
            JsonObject object = array.getAsJsonObject();
            if (object.has("domain")) {
                domain = object.get("domain").getAsString();
            }
            if (object.has("expirationDate")) {
                expirationDate = Math.floorDiv(object.get("expirationDate").getAsLong() * 1000, 1);
            }
            if (object.has("secure")) {
                hostOnly = object.get("secure").getAsBoolean();
            }
            if (object.has("httpOnly")) {
                secure = object.get("httpOnly").getAsBoolean();
            }
            if (object.has("name")) {
                name = object.get("name").getAsString();
            }
            if (object.has("path")) {
                path = object.get("path").getAsString();
            }
            if (object.has("value")) {
                value = object.get("value").getAsString();
            }
            cookies.add(new Cookie(name, value, domain, path, new Date(expirationDate)));
        }
        return cookies;
    }
}
