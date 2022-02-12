package com.anchen.function;

import com.alibaba.fastjson.JSONObject;
import com.anchen.httprequest.HttpRequest;

/**
 * @author 安宸
 * @create 2022/2/5 11:58
 */
public class GetLonAndLat {
    public static JSONObject getLonAndLat(String address) {
        String url = "https://api.map.baidu.com/geocoding/v3/";
        String param = "address=" + address + "&output=json&ak=uUMHLLKRVWpszrspq4Yt1wT";
        String result = HttpRequest.sendGet(url, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        JSONObject res1=res.getJSONObject("result");
        JSONObject jsonObject2=res1.getJSONObject("location");
        jsonObject2.put("lng", String.format("%.6f", jsonObject2.get("lng")));
        jsonObject2.put("lat", String.format("%.6f", jsonObject2.get("lat")));
        return jsonObject2;
    }
}
