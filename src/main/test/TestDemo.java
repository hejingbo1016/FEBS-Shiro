import cc.mrbird.febs.common.utils.ConfigUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.SandboxSignkey;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Test.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TestDemo {

    private static final String mch_id = "1492460372";

    @Test
    public void test1() throws Exception {
        String wechatKey = ConfigUtil.getProperty("wechat.key");
        PayMchAPI.sandboxnewStart();
        SandboxSignkey sandboxSignkey = PayMchAPI.sandboxnewPayGetsignkey(mch_id, wechatKey);
        System.out.println(sandboxSignkey);
    }

}
