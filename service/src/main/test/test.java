
import cn.hutool.core.date.DateUtil;
import com.anchen.model.Jrxy;
import com.anchen.mysql.OperationDatabase;

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
        return scheduled.schedule(() -> {
            System.out.println(DateUtil.now() + " " + Thread.currentThread().getName() + " " + i);
            Jrxy jrxy=new Jrxy();
            jrxy.setStudentId(i);
            jrxy.setPsw("123456");
            jrxy.setAddressProvince("河南省");
            jrxy.setAddressCity("郑州市");
            jrxy.setAddressRegion("金水区");
            jrxy.setAddress("111");
            jrxy.setLon(12.3333);
            jrxy.setLat(11.3333);
            jrxy.setPushChannel("111");
            jrxy.setServerChanKey("123");
            jrxy.setPushDeerKey("123");
            jrxy.setErrorCount(0);

            System.out.println(OperationDatabase.insert(jrxy));
        }, 0, TimeUnit.SECONDS);
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public static void main(String[] args) throws InterruptedException {
        ScheduleDasks timer=new ScheduleDasks();
        timer.beginBySchedule(1);
        timer.beginBySchedule(2);
        timer.beginBySchedule(3);
        timer.beginBySchedule(4);
//        timer.beginBySchedule(5);
//        Thread.sleep(5000);
//        timer.beginBySchedule(6);
//        Thread.sleep(1000);
//        timer.beginBySchedule(7);
    }
}
