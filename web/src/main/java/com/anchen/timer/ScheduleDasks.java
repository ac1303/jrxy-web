package com.anchen.timer;


import cn.hutool.core.date.DateUtil;
import java.util.concurrent.*;
/**
 * @author 安宸
 * @create 2022/2/5 15:23
 */
final class ScheduleDasks {
    private int corePoolSize;
    private final ScheduledExecutorService scheduled;
    ScheduleDasks(){
        this.corePoolSize=3;
        scheduled = Executors.newScheduledThreadPool(corePoolSize);
    }
    ScheduleDasks(int x){
        this.corePoolSize=x;
        scheduled = Executors.newScheduledThreadPool(corePoolSize);
    }
    public ScheduledFuture<?> beginBySchedule(int i){
        System.out.println(DateUtil.now());
//间隔单位毫秒：TimeUnit.MILLISECONDS
//间隔单位秒：TimeUnit.SECONDS
//间隔单位分钟：TimeUnit.MINUTES
//间隔单位小时：TimeUnit.HOURS
//间隔单位天：TimeUnit.DAYS
        return scheduled.schedule(() -> {
                System.out.println(DateUtil.now() + " " + Thread.currentThread().getName() + " " + i);
//                try {
//                    // 模拟执行任务2秒
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }, 10, TimeUnit.SECONDS);
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

public static void main(String[] args) throws InterruptedException {
    ScheduleDasks timer=new ScheduleDasks();
    timer.beginBySchedule(1);
    Thread.sleep(2000);
    timer.beginBySchedule(2);
    Thread.sleep(1000);
    timer.beginBySchedule(3);
    Thread.sleep(500);
    timer.beginBySchedule(4);
    Thread.sleep(5000);
    timer.beginBySchedule(5);

}
}
