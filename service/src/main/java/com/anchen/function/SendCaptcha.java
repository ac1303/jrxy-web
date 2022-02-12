package com.anchen.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anchen.httprequest.HttpRequest;
import com.anchen.timer.Captcha;

import java.util.Random;

//接收数据定义，-3参数错误，-2代表程序报错，-1代表推送失败，0代表密钥不存在，1代表推送成功
/**
 * @author 安宸
 * @create 2022/2/7 17:19
 */
public class SendCaptcha {
    public static String getRandomString(int length) {
        String chs = "0123456789";
        StringBuffer sb = new StringBuffer();
        Random rand = new Random();
        for (;length >0; length--) {
            sb.append(chs.charAt(rand.nextInt(chs.length())));
        }
        return sb.toString();
    }

    public static JSONObject pushdeer(String key,String code){
        JSONObject jsonObject = new JSONObject();
        String url = "https://api2.pushdeer.com/message/push?pushkey="+key+"&text=【今日校园自动打卡】您的验证码是："+code;
        String data = HttpRequest.sendGet(url);
//        如果有多个设备，不考虑其他设备是否推送成功，只要有一个设备推送成功，就认为推送成功
//        System.out.println(data);
        JSONArray result;
        try {
            result=JSONObject.parseObject(data)
                    .getJSONObject("content")
                    .getJSONArray("result");
        }catch (Exception e){
            jsonObject.put("code",-2);
            jsonObject.put("msg","程序报错,代码401");
            return jsonObject;
        }
        if (result.size()>0){
            try {
//                if("ok".equals(JSONObject.parseObject(result.getString(0))
//                        .getJSONArray("logs")
//                        .getJSONObject(0)
//                        .getString("result"))){
//                    Captcha.setId(key,code);
//                    jsonObject.put("code",1);
//                    jsonObject.put("msg","推送成功");
//                }else {
//                    jsonObject.put("code",-1);
//                    jsonObject.put("msg","推送失败");
//                }
                if ("ok".equals(JSONObject.parseObject(result.getString(0))
                        .getString("success"))){
                    Captcha.setId(key,code);
                    jsonObject.put("code",1);
                    jsonObject.put("msg","推送成功");
                }else {
                    jsonObject.put("code",-1);
                    jsonObject.put("msg","推送失败");
                }
            }catch (Exception e){
                jsonObject.put("code",-2);
                jsonObject.put("msg","程序报错，代码402");
            }
        }else {
            jsonObject.put("code",0);
            jsonObject.put("msg","密钥不存在");
        }
        return jsonObject;
    }
    public static JSONObject serverChan(String key,String code){
        JSONObject jsonObject = new JSONObject();
        try {
            String url = "https://sctapi.ftqq.com/"+key+".send?title=【今日校园自动打卡】验证码&desp=您的验证码是："+code;
            String data = HttpRequest.sendGet(url);
            JSONObject result;
            try {
                result =JSONObject.parseObject(data);
            }catch (Exception e){
                jsonObject.put("code",0);
                jsonObject.put("msg","密钥不存在，请检查推送渠道");
                return jsonObject;
            }
            if (result.getInteger("code")==0&&"SUCCESS".equals(result.getJSONObject("data").getString("error"))){
                Captcha.setId(key,code);
                jsonObject.put("code",1);
                jsonObject.put("msg","推送成功");
            }
            if (result.getInteger("code")==40001){
                jsonObject.put("code",-1);
                jsonObject.put("msg",result.getString("info"));
            }
        }catch (Exception e){
            jsonObject.put("code",-2);
            jsonObject.put("msg","程序报错,代码403");
        }

        return jsonObject;
    }
    public static JSONObject send(String key,String channel){
//        channel参数是PushDeer和ServerChan
        System.out.println("密钥是："+key+" 通道是："+channel);
        if ("PushDeer".equals(channel)){
            return pushdeer(key,getRandomString(6));
        }else if ("ServerChan".equals(channel)){
            return serverChan(key,getRandomString(6));
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-3);
            jsonObject.put("msg","参数错误");
            return jsonObject;
        }
    }

}
