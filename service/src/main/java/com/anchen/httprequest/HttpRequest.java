package com.anchen.httprequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author 安宸
 * @create 2022/2/5 11:23
 */
public class HttpRequest {
    public static String sendGet(String url, String param){
        URLConnection connection;
        BufferedReader bufReader;
        StringBuilder result = new StringBuilder();
        try {
            String line;
            connection = new URL(url+"?"+param).openConnection();
            connection.setRequestProperty("Accept-Charset","UTF-8");
            bufReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            while ((line = bufReader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    public static String sendGet(String url){
        URLConnection connection;
        BufferedReader bufReader;
        StringBuilder result = new StringBuilder();
        try {
            String line;
            connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset","UTF-8");
            bufReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            while ((line = bufReader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    public static JSONObject getBody(BufferedReader reader){
        StringBuilder data = new StringBuilder();
        String line;
        try {
            while (null != (line = reader.readLine())) {
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(data.toString());
    }
}
