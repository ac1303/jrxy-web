package com.anchen.timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author 安宸
 * @create 2022/2/7 13:57
 */
public class starUp  implements javax.servlet.ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        创建线程池，每天执行一次
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                读取数据库，查询数据库中的数据，每天查询一次
                System.out.println("每天执行一次");
            }
//间隔单位毫秒：TimeUnit.MILLISECONDS
//间隔单位秒：TimeUnit.SECONDS
//间隔单位分钟：TimeUnit.MINUTES
//间隔单位小时：TimeUnit.HOURS
//间隔单位天：TimeUnit.DAYS
        },0,1,java.util.concurrent.TimeUnit.DAYS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
