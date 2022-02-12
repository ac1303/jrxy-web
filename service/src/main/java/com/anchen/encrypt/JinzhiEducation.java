package com.anchen.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * @author 安宸
 * @create 2022/2/4 12:21
 */
public class JinzhiEducation {
    /**
     * 生成随机字符串
     * @param length 传入随机字符串的长度
     */
    public static String getRandomString(int length){
        String chs = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        StringBuilder result = new StringBuilder();
        Random rand = new Random();
        for (;length>=0;length--){
            result.append(chs.charAt(rand.nextInt(chs.length())));
        }
        return result.toString();
    }

    /**
     * AES-128-CBC加密，加密结果Base64编码
     * @param data 随机64位字符串+密码
     * @param key  从页面获取
     * @param iv   iv对加密结果无影响，可以随机生成
     */
    public static String encryptAES(String data, String key, String iv) throws Exception{
        data = getRandomString(64)+data;
        // 获取加解密的算法工具类
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 通过给定的字节数组构建一个密钥
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        // 使用IV构造对象
        IvParameterSpec ivParameter = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        // 对工具类进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);
        // 用加密工具类对象对明文进行加密，防止出现乱码，所以采用Base64编码
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * AES-128-CBC解密，解密结果Base64解码
     * @param data
     * @param key
     * @param iv
     */
    public static String decryptAES(String data, String key, String iv) throws Exception{
        // 获取加解密的算法工具类
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 通过给定的字节数组构建一个密钥
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        // 使用IV构造对象
        IvParameterSpec ivParameter = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        // 对工具类进行初始化
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);
        // 用加密工具类对解密后的明文进行解码
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        String key = encryptAES("2019901333/fanshuhua..","kSXW5puqxPbMPgCp","kSXW5puqxPbMPabc");
        System.out.println(key);
        System.out.println(decryptAES(key,"kSXW5puqxPbMPgCp","kSXW5puqxPbMPgCp"));
    }
}
