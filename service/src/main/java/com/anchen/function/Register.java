package com.anchen.function;

import com.alibaba.fastjson.JSONObject;
import com.anchen.model.Jrxy;
import com.anchen.mysql.OperationDatabase;

/**
 * @author 安宸
 * @create 2022/2/11 17:56
 * {"address_Region":"东城区",
 * "address_Province":"北京市",
 * "pushkey":"PDU4477T9s84NUvrYQASjr87acp6R6Aaak9prPE5",
 * "code":"265529",
 * "address":"北京市/北京市/东城区",
 * "latitude":"39.934827",
 * "sign":"vJXEaN6hqg/eZztqQJgYjY4KhlZTEDwo0u7ZMKZZJs2KxeVadqfr6vdfGz+DRRULDHj8UIABwu1OdSSTqM+wf/yopDq1ReHH+qpne5u4n7+h7NFhrgv17l5EV8WuSKZK",
 * "address_City":"北京市",
 * "longitude":"116.422401",
 * "radio":"1"}
 */
public class Register {
    public static JSONObject GenerateStudentModel(JSONObject json){
        JSONObject jsonObject = new JSONObject();
        try{
            if(!ValidateCaptcha.validate(json.getString("pushkey"),json.getString("code"))){
                jsonObject.put("code",0);
                jsonObject.put("msg","验证码错误");
                return jsonObject;
            }
            String sign=json.getString("sign");
            if (sign.equals("")||sign==null){
                jsonObject.put("code",-1);
                jsonObject.put("msg","注册失败，sign为空");
                return jsonObject;
            }
            JSONObject IDAndPsw=VerifyStudentIDAndPassword.getIDAndPassword(sign);
            if(IDAndPsw.getIntValue("code")!=200){
                jsonObject.put("code",-1);
                jsonObject.put("msg","注册失败，sign错误");
                return jsonObject;
            }
            Jrxy jrxy=new Jrxy();
            jrxy.setStudentId(Long.parseLong(IDAndPsw.getString("id")));
            jrxy.setPsw(IDAndPsw.getString("password"));
            jrxy.setAddressProvince(json.getString("address_Province"));
            jrxy.setAddressCity(json.getString("address_City"));
            jrxy.setAddressRegion(json.getString("address_Region"));
            jrxy.setAddress(json.getString("address"));
            jrxy.setLon(Double.parseDouble(json.getString("longitude")));
            jrxy.setLat(Double.parseDouble(json.getString("latitude")));
            jrxy.setPushChannel(json.getString("channel"));
//            判断是哪个推送渠道
            if ("PushDeer".equals(json.getString("channel"))){
                jrxy.setPushDeerKey(json.getString("pushkey"));
            }else if ("ServerChan".equals(json.getString("channel"))){
                jrxy.setServerChanKey(json.getString("pushkey"));
            }else {
                jsonObject.put("code",-3);
                jsonObject.put("msg","注册失败，参数错误");
                return jsonObject;
            }
//            写入数据库
            if(OperationDatabase.insert(jrxy)){
                jsonObject.put("code",1);
                jsonObject.put("msg","注册成功");
            }else {
                jsonObject.put("code",0);
                jsonObject.put("msg","注册失败,无法保存到数据库");
            }
            return jsonObject;
        }catch (Exception e){
            jsonObject.put("code",-2);
            jsonObject.put("msg","注册失败,数据解析失败");
            return jsonObject;
        }
    }
}
