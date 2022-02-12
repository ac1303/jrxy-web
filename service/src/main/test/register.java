import com.alibaba.fastjson.JSON;
import com.anchen.function.Register;

/**
 * @author 安宸
 * @create 2022/2/11 18:07
 */
public class register {
    public static void main(String[] args) {
        Register.GenerateStudentModel(JSON.parseObject("{\"address_Region\":\"东城区\",\"address_Province\":\"北京市\",\"pushkey\":\"PDU4477T9s84NUvrYQASjr87acp6R6Aaak9prPE5\",\"code\":\"408236\",\"address\":\"北京市/北京市/东城区\",\"latitude\":\"39.934827\",\"sign\":\"\",\"address_City\":\"北京市\",\"longitude\":\"116.422401\",\"radio\":\"1\"}"));
    }
}
