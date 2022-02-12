package com.anchen.function;

import com.alibaba.fastjson.JSONObject;
import com.anchen.timer.Captcha;

/**
 * @author 安宸
 * @create 2022/2/8 14:14
 */
public class ValidateCaptcha {
    public static boolean validate(String captchaId, String code) {
        JSONObject jsonObject = new JSONObject();
        if (Captcha.check(captchaId, code)) {
            return true;
        }else {
            return false;
        }
    }

}
