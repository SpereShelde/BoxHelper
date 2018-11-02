package tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.torrent.TRTorrent;
import models.torrent.deTorrent.DETorrent;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by SpereShelde on 2018/7/3.
 */
public class HttpHelper {

    public static CookieStore cookieStore = null;


    public static String doGetToQB(String destination, String userAgent, String host) throws IOException {

        String responseString = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建httpget
        HttpGet httpget = new HttpGet(destination);
        httpget.setHeader("User-Agent", userAgent);
        httpget.setHeader("Host", host);

        // 执行get请求.
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 打印响应状态
            if (entity != null && response.getStatusLine().toString().contains("200")) {
                // 打印响应内容
                responseString = EntityUtils.toString(entity) ;
            }
        } finally {
            response.close();
            httpclient.close();
        }
        return responseString;
    }

    public static String doGetToQBWithAuth(String destination, String sid, String userAgent, String host) throws IOException {

        String responseString = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建httpget
        HttpGet httpget = new HttpGet(destination);
        httpget.setHeader("User-Agent", userAgent);
        httpget.setHeader("Cookie", "SID=" + sid);
        httpget.setHeader("Host", host);

        // 执行get请求.
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 打印响应状态
            if (entity != null && response.getStatusLine().toString().contains("200")) {
                // 打印响应内容
                responseString = EntityUtils.toString(entity) ;
            }
        } finally {
            response.close();
            httpclient.close();
        }
        return responseString;
    }

    public static String loginToQB(String destination, String userAgent, String username, String passwd, String host) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        //创建参数队列
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addTextBody("username", username);
        multipartEntityBuilder.addTextBody("password", passwd);
        httpPost.setEntity(multipartEntityBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = null;
        try {
            // 获取响应实体
            entity = response.getEntity();

        } finally {
            response.close();
            httpClient.close();
        }
        String setCookie = response.getFirstHeader("Set-Cookie").getValue();
        String sessionId = setCookie.substring("SID=".length(), setCookie.indexOf(";"));
        return sessionId;
    }

    public static boolean doPostToQB(String destination, String userAgent, String sid, String host, Map<String, String> contents) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        //创建参数队列
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Cookie", "SID=" + sid);
        httpPost.addHeader("Host", host);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        contents.forEach((k,v) -> multipartEntityBuilder.addTextBody(k, v));
        httpPost.setEntity(multipartEntityBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = null;
        try {
            // 获取响应实体
            entity = response.getEntity();

        } finally {
            response.close();
            httpClient.close();
        }
        return entity != null && response.getStatusLine().toString().contains("200");//Not precise
    }

    public static String loginToDE(String destination, String userAgent, String passwd, String host){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String login = "{\"method\":\"auth.login\",\"params\":[\"" + passwd + "\"],\"id\":1}";
        HttpPost httpPost = new HttpPost(destination);
        //创建参数队列
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");

        StringEntity stringEntity = new StringEntity(login, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String setCookie = response.getFirstHeader("Set-Cookie").getValue();
        String sessionId = setCookie.substring("_session_id=".length(), setCookie.indexOf(";"));
        return sessionId;
    }

    public static ArrayList<DETorrent> getTorrentsFromDE(String destination, String userAgent, String sid, String host, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Cookie", "_session_id=" + sid);
        String get = "{\"method\":\"web.update_ui\",\"params\":[[" + params + "],{}],\"id\":123}";
        StringEntity stringEntity = new StringEntity(get, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String res = "";
        try {
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
        }
        return ConvertJson.convertDETorrents(res);
    }

    public static void addTorrentsToDE(String destination, String userAgent, String sid, String host, ArrayList<String> urls, double download, double upload) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Cookie", "_session_id=" + sid);
        CloseableHttpResponse response = null;
        try {
            urls.forEach(url -> {
                String down = "{\"method\":\"web.download_torrent_from_url\",\"params\":[\"" + url + "\"],\"id\":1}";
                StringEntity stringEntity1 = new StringEntity(down, ContentType.APPLICATION_JSON);
                httpPost.setEntity(stringEntity1);
                try {
                    CloseableHttpResponse response1 = httpClient.execute(httpPost);

                    HttpEntity entity1 = response1.getEntity();
                    String res1 = EntityUtils.toString(entity1, "UTF-8");
                    Map resMap1 = ConvertJson.convertResponse(res1);
                    if (resMap1.get("error").equals("null")) {
                        String add = "{\"method\":\"web.add_torrents\",\"params\":[[{\"path\":\"" + resMap1.get("result") + "\",\"options\":{\"max_download_speed\":" + download * 1024 + ",\"max_upload_speed\":" + upload * 1024 + "}}]],\"id\":1}";
                        StringEntity stringEntity2 = new StringEntity(add, ContentType.APPLICATION_JSON);
                        httpPost.setEntity(stringEntity2);
                        CloseableHttpResponse response2 = httpClient.execute(httpPost);
                        HttpEntity entity2 = response2.getEntity();
                        String res2 = EntityUtils.toString(entity2, "UTF-8");
                        Map resMap2 = ConvertJson.convertResponse(res2);
                        if (resMap2.get("error").equals("null")) {
                            System.out.println("DE: successfully add torrent " + url.substring(0, url.length() - 41));
                        }
                        else System.out.println("DE: cannot add torrent");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            response.close();
            httpClient.close();
        } catch (Exception e) {
        }
    }

    public static boolean removeTorrentFromDE(String destination, String userAgent, String sid, String host, String hash) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Cookie", "_session_id=" + sid);
        String get = "{\"method\":\"core.remove_torrent\",\"params\":[\"" + hash + "\",true],\"id\":1}";
        StringEntity stringEntity = new StringEntity(get, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String res = "";
        try {
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
            Map resMap = ConvertJson.convertResponse(res);
            if (resMap.get("error").equals("null")) {
                System.out.println("DE: removing torrent " + hash);
                return true;
            }
            else {
                System.out.println("DE: cannot remove torrent " + hash);
                return false;
            }
        } finally {
            response.close();
            httpClient.close();
        }
    }

    public static String loginToTR(String destination, String userAgent, String host){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String login = "{\"method\":\"session-get\",\"arguments\":{},\"tag\":\"\"}";
        HttpPost httpPost = new HttpPost(destination);
        //创建参数队列
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");

        StringEntity stringEntity = new StringEntity(login, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cookie = response.getFirstHeader("x-transmission-session-id").getValue();
        return cookie;
    }

    public static ArrayList<TRTorrent> getTorrentsFromTR(String destination, String userAgent, String sid, String host, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("X-Transmission-Session-Id", sid);
        String get = "{\"method\":\"torrent-get\",\"arguments\":{\"fields\":[" + params + "]},\"tag\":\"\"}";
        StringEntity stringEntity = new StringEntity(get, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String res = "";
        try {
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");

        } catch (Exception e) {
        }
        return ConvertJson.convertTRTorrents(res);
    }

    public static void addTorrentsToTR(String destination, String userAgent, String sid, String host, ArrayList<String> urls, double download, double upload) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("X-Transmission-Session-Id", sid);
        if (download < 0) download = 1000;
        if (upload < 0) upload = 1000;
        int finalDownload = (int)download;
        int finalUpload = (int)upload;
        urls.forEach(url -> {
            String down = "{\"method\":\"torrent-add\",\"arguments\":{\"filename\":\"" + url + "\",\"paused\":false},\"tag\":\"\"}";
            StringEntity stringEntity = new StringEntity(down, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String res = EntityUtils.toString(entity, "UTF-8");
                Map resMap = ConvertJson.convertResponse(res);
                if ("success".equals(resMap.get("result"))) {
                    System.out.println("TR: successfully add torrent " + url.substring(0, url.length() - 41));
                    String set = "{\"method\":\"torrent-set\",\"arguments\":{\"downloadLimited\":true,\"downloadLimit\":" + finalDownload * 1024 + ",\"uploadLimited\":true,\"uploadLimit\":" + finalUpload * 1024 + ",\"ids\":" + resMap.get("argumentsTorrentAddID") + "},\"tag\":\"\"}";
                    stringEntity = new StringEntity(set, ContentType.APPLICATION_JSON);
                    httpPost.setEntity(stringEntity);
                    httpClient.execute(httpPost);
                } else System.out.println("TR: cannot add torrent");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public static boolean removeTorrentFromTR(String destination, String userAgent, String sid, String host, String ids) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(destination);
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("Host", host);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("X-Transmission-Session-Id", sid);
        String get = "{\"method\":\"torrent-remove\",\"arguments\":{\"ids\":[" + ids + "],\"delete-local-data\":true},\"tag\":\"\"}";
        StringEntity stringEntity = new StringEntity(get, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String res = "";
        try {
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
//            Map resMap = ConvertJson.convertResponse(res);
//            if (resMap.get("result").equals("success")) {
                System.out.println("TR: removing torrent " + ids);
//                return true;
//            }
//            else {
//                System.out.println("TR: cannot remove torrent " + ids);
//                return false;
//            }
        } finally {
            response.close();
            httpClient.close();
        }
        return true;
    }

}
