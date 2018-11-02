package tools;

import com.google.gson.*;
import models.torrent.QBTorrent;
import models.torrent.TRTorrent;
import models.torrent.deTorrent.DEResponse;
import models.torrent.deTorrent.DETorrent;
import models.torrent.deTorrent.Torrents;
import org.openqa.selenium.Cookie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static java.nio.file.Paths.get;

/**
 * Created by SpereShelde on 2018/6/11.
 */
public class ConvertJson {

    public static Map convertConfigure(String jsonFileName) throws IOException {

        ArrayList<String> deConfig = new ArrayList<>();
        ArrayList<String> qbConfig = new ArrayList<>();
        ArrayList<String> trConfig = new ArrayList<>();
        ArrayList<String> rtConfig = new ArrayList<>();
        Map configures = new HashMap();
        JsonParser jsonParser = new JsonParser();
        String content = null;

        if (new File(jsonFileName).exists()){
            content = new String(Files.readAllBytes(get(jsonFileName)));
        }

        JsonObject object = (JsonObject) jsonParser.parse(content);

        if (object.has("de_config")) {
            JsonArray de = object.get("de_config").getAsJsonArray();
            for (JsonElement a: de) {
                if (a != null || !"".equals(a.getAsString())) deConfig.add(a.getAsString());
            }
            configures.put("deConfig", deConfig.toArray());
        }

        if (object.has("qb_config")) {
            JsonArray de = object.get("qb_config").getAsJsonArray();
            for (JsonElement a: de) {
                if (a != null || !"".equals(a.getAsString())) qbConfig.add(a.getAsString());
                configures.put("qbConfig", qbConfig.toArray());
            }
        }

        if (object.has("tr_config")) {
            JsonArray de = object.get("tr_config").getAsJsonArray();
            for (JsonElement a: de) {
                if (a != null || !"".equals(a.getAsString())) trConfig.add(a.getAsString());
            }
            String webui = trConfig.get(0).replace("://", "://" + trConfig.get(1) + "@");
            if (webui.contains("transmission")) trConfig.set(0, webui.substring(0, webui.indexOf("transmission") - 1));
            configures.put("trConfig", trConfig.toArray());
        }

        if (object.has("rt_config")) {
            JsonArray de = object.get("rt_config").getAsJsonArray();
            for (JsonElement a: de) {
                if (a != null || !"".equals(a.getAsString())) rtConfig.add(a.getAsString());
            }
            configures.put("rtConfig", rtConfig.toArray());
        }

        JsonArray arrayURL = object.get("url_size_speed_cli").getAsJsonArray();
        ArrayList<String> urls = new ArrayList<>();
        Map<String, String> urlSizeSpeedCli = new HashMap<>();
        for (JsonElement a: arrayURL) {
            JsonArray url = a.getAsJsonArray();
            double min = url.get(1).getAsDouble();
            double max = url.get(2).getAsDouble();
            String cli = url.get(3).getAsString();
            double down = url.get(4).getAsDouble();
            double up = url.get(5).getAsDouble();
            boolean load = url.get(6).getAsBoolean();
            if (min < 0) min = -1;
            if (max < 0) max = -1;
            if (down < 0) down = -1;
            if (up < 0) up = -1;
            if (!"".equals(url.get(0).getAsString())) {
                urlSizeSpeedCli.put(url.get(0).getAsString(), cli + "/" +min + "/" + max + "/" + down + "/" + up + "/" + load);
            }
        }
        configures.put("url_size_speed_cli", urlSizeSpeedCli);
        configures.put("downloading_amount", object.get("total_downloading_amount").getAsInt());
        configures.put("cycle", object.get("cycle").getAsInt());
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

    public static Map convertResponse(String response){
        Map res = new HashMap();
        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(response);

        if (object.has("id")) {
            res.put("id", object.get("id").getAsString()) ;
        }
        if (object.has("result")) {
            res.put("result", object.get("result").getAsString()) ;
        }
        if (object.has("error")) {
            if (object.get("error") == JsonNull.INSTANCE){
                res.put("error", "null");
            } else res.put("error", "notNull");
        }
        if (object.has("arguments")) {
            JsonObject arguments = null;
            JsonObject torrentAdded = null;
            if (object.has("arguments")) arguments = object.get("arguments").getAsJsonObject();
            if (arguments != null && arguments.has("torrent-added")) torrentAdded = arguments.get("torrent-added").getAsJsonObject();
            if (torrentAdded != null && torrentAdded.has("id")) res.put("argumentsTorrentAddID", torrentAdded.get("id").getAsInt()) ;
        }

        return res;
    }

    public static ArrayList<QBTorrent> convertQBTorrents(String content){
        ArrayList<QBTorrent> QBTorrents = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        String name, hash, state;
        long added_on, completion_on, last_activity, size;
        int dl_limit, up_limit, num_incomplete;
        double ratio;

        JsonArray jsonArray = (JsonArray) jsonParser.parse(content);
        for (JsonElement array: jsonArray) {
            JsonObject object = array.getAsJsonObject();
            added_on = object.get("added_on").getAsLong();
            completion_on = object.get("completion_on").getAsLong();
            dl_limit = object.get("dl_limit").getAsInt();
            hash = object.get("hash").getAsString();
            last_activity = object.get("last_activity").getAsLong();
            name = object.get("name").getAsString();
            num_incomplete = object.get("num_incomplete").getAsInt();
            ratio = object.get("ratio").getAsDouble();
            size = object.get("size").getAsLong();
            up_limit = object.get("up_limit").getAsInt();
            state = object.get("state").getAsString();
            QBTorrents.add(new QBTorrent(name, hash, state, added_on, completion_on, last_activity, size, dl_limit, up_limit, num_incomplete, ratio));
        }
        return QBTorrents;
    }

    public static ArrayList<DETorrent> convertDETorrents(String content){

        Gson gson = new Gson();
        DEResponse response = gson.fromJson(content, DEResponse.class);

        Map<String, Torrents> torrents = response.getResult().getTorrents(); //对动态的key，来创建map，间接从中取出实体类futrue。
        ArrayList<DETorrent> deTorrents = new ArrayList<>();
        for (Map.Entry<String, Torrents> pair:torrents.entrySet()){//遍历取出键值对，调用getkey()，getvalue()取出key和value。
            DETorrent deTorrent = new DETorrent(pair.getValue().getName(), pair.getKey(), pair.getValue().getState(), pair.getValue().getTotal_wanted(), pair.getValue().getTime_added(), pair.getValue().getUpload_payload_rate(), pair.getValue().getRatio());
            deTorrents.add(deTorrent);
        }
        return deTorrents;
    }

    public static ArrayList<TRTorrent> convertTRTorrents(String content) {
        ArrayList<TRTorrent> trTorrents = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        int status, id;
        long activityDate, addedDate, doneDate, rateDownload, rateUpload, totalSize;
        double uploadRatio;

        JsonObject jsonObject = (JsonObject) jsonParser.parse(content);
        JsonObject argument = jsonObject.getAsJsonObject("arguments");
        JsonArray torrents = argument.getAsJsonArray("torrents");
        for (JsonElement torrent: torrents) {
            JsonObject object = torrent.getAsJsonObject();
            activityDate = object.get("activityDate").getAsLong();
            addedDate = object.get("addedDate").getAsLong();
            doneDate = object.get("doneDate").getAsLong();
            rateDownload = object.get("rateDownload").getAsLong();
            rateUpload = object.get("rateUpload").getAsLong();
            totalSize = object.get("totalSize").getAsLong();
            uploadRatio = object.get("uploadRatio").getAsDouble();
            status = object.get("status").getAsInt();
            id = object.get("id").getAsInt();
            trTorrents.add(new TRTorrent(id, status, activityDate, addedDate, doneDate, rateDownload, rateUpload, totalSize, uploadRatio));
        }
        return trTorrents;
    }
}


