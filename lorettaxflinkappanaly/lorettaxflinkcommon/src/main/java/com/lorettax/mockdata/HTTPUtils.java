package com.lorettax.mockdata;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by li on 2018/12/16.
 */
public class HTTPUtils {

    public static void httppost(String urlString,String param){
        URL url;

        try {
            url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(1000 * 5);
            conn.connect();
            conn.getOutputStream().write(param.getBytes("utf8"));
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            byte[] buffer = new byte[1024];
            StringBuffer sb = new StringBuffer();
            InputStream in = conn.getInputStream();
            int httpCode = conn.getResponseCode();
            System.out.println(in.available());
            while(in.read(buffer,0,1024) != -1) {
                sb.append(new String(buffer));
            }
            System.out.println("sb:" + sb.toString());
            in.close();
            System.out.println(httpCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
