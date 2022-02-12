package com.anchen.function;

import com.alibaba.fastjson.JSONObject;
import com.anchen.encrypt.JinzhiEducation;
import com.anchen.httprequest.HttpRequest;
import com.anchen.mysql.OperationDatabase;

/**
 * @author 安宸
 * @create 2022/2/10 22:13
 */
public class VerifyStudentIDAndPassword {
    public static JSONObject getIDAndPassword(String id, String password) {
        JSONObject resultJson = new JSONObject();
        if (OperationDatabase.isExist(id)) {
            resultJson.put("code",300);
            resultJson.put("msg","账号已存在，请联系我修改数据，QQ：878375551");
            return resultJson;
        }
        String url = "http://39.101.***.**:5000/login/"+id+"&"+password+"&QyBkSG";
        String result = HttpRequest.sendGet(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code")==200){
            String sign=id+"/"+password;
            try {
                sign=JinzhiEducation.encryptAES(sign,"kSXW5puqx","kSXW5puq");
                resultJson.put("code",200);
                resultJson.put("sign",sign);
            } catch (Exception e) {
                resultJson.put("code",500);
                resultJson.put("msg","服务器未知错误");
            }
        }else if (jsonObject.getInteger("code")==400){
            return jsonObject;
        }else {
            resultJson.put("code",500);
            resultJson.put("msg","服务器未知错误");
        }
        return resultJson;
    }

    public static JSONObject getIDAndPassword(String token) {
        JSONObject resultJson = new JSONObject();
        String data= "";
        String id= "";
        String password= "";
        try {
            data = JinzhiEducation.decryptAES(token,"kSXW5puqx","kSXW5puqx");
            data=data.substring(65);
            if(data.contains("/")){
                id=data.substring(0,data.indexOf("/"));
                password=data.substring(data.indexOf("/")+1);
                resultJson.put("code",200);
                resultJson.put("id",id);
                resultJson.put("password",password);
                return resultJson;
            }else {
                resultJson.put("code",500);
                resultJson.put("msg","sign验证错误");
                return resultJson;
            }
        } catch (Exception e) {
            resultJson.put("code",500);
            resultJson.put("msg","sign验证错误");
            return resultJson;
        }
    }
}
