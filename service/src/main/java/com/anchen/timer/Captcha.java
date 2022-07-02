package com.anchen.timer;


import cn.hutool.core.date.DateUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
/**
 * @author 范书华
 * @create 2022/2/7 15:37
 */
final public class Captcha {
    private final static Map<String,String> CAPTCHE_LIST=new HashMap<String, String>();
    private final static Map<String,ScheduledFuture> CAPTCHE_AUTHORITY= new HashMap<>();
    private final static ScheduledExecutorService CAPTCHE_SCHEDULED= Executors.newScheduledThreadPool(5);

    public static void setId(String id,String value){
        CAPTCHE_LIST.put(id,value);
        checkAndAdd(id,value);
        System.out.println(DateUtil.now()+"添加ID： "+id+" "+CAPTCHE_LIST);
    }
    public static String getValue(String id){
        return CAPTCHE_LIST.get(id);
    }
    public static ScheduledFuture<?> addTimerTask(String id){
//间隔单位毫秒：TimeUnit.MILLISECONDS
//间隔单位秒：TimeUnit.SECONDS
//间隔单位分钟：TimeUnit.MINUTES
//间隔单位小时：TimeUnit.HOURS
//间隔单位天：TimeUnit.DAYS
        return CAPTCHE_SCHEDULED.schedule(() -> {
            CAPTCHE_LIST.remove(id);
            System.out.println(DateUtil.now()+"超时删除ID： "+id+" "+CAPTCHE_LIST);
        }, 120, TimeUnit.SECONDS);
    }
//    检查验证码是否正确
    public static boolean check(String id,String value){
        if(CAPTCHE_LIST.containsKey(id)&&CAPTCHE_LIST.get(id).equals(value)){
//            移除验证码，并且取消定时任务，同时移除句柄
            CAPTCHE_LIST.remove(id);
            CAPTCHE_AUTHORITY.get(id).cancel(true);
            CAPTCHE_AUTHORITY.remove(id);
            return true;
        }
        return false;
    }
//    应用场景，检查是否存在上一个验证码的定时任务句柄，如果存在则清除定时任务和句柄，然后添加新的
    public static void checkAndAdd(String id,String value){
        if(CAPTCHE_AUTHORITY.containsKey(id)){
//            取消线程
            CAPTCHE_AUTHORITY.get(id).cancel(true);
        }
        CAPTCHE_AUTHORITY.put(id,addTimerTask(id));
        CAPTCHE_LIST.put(id,value);
    }
}
