import cc.mrbird.febs.common.utils.ConfigUtil;
import cc.mrbird.febs.wechat.utils.WechatUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Test.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TestDemo {

    private static final String mch_id = "1492460372";

    @Test
    public void test1() throws Exception {
        String wechatKey = ConfigUtil.getProperty("wechat.key");
        String wechatAppId = ConfigUtil.getProperty("wechat.appid");
        PayMchAPI.sandboxnewEnd();
        SandboxSignkey sandboxSignkey = PayMchAPI.sandboxnewPayGetsignkey(mch_id, wechatKey);

        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(wechatAppId);
        unifiedorder.setMch_id(mch_id);
        unifiedorder.setBody("测试商品");
        unifiedorder.setDevice_info("web");
        unifiedorder.setNotify_url("localhost:8080/");
        unifiedorder.setOut_trade_no(WechatUtil.generateOrderSN());// "20200901125346"
        unifiedorder.setTotal_fee("1");
        unifiedorder.setTrade_type("NATIVE");
        unifiedorder.setSpbill_create_ip("61.140.181.10");
        unifiedorder.setProduct_id(WechatUtil.generateOrderSN());// "12235413214070356458058"
        unifiedorder.setNonce_str(WechatUtil.getRandomString(32));

        Detail detail = new Detail();
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>(1);
        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setPrice(1);
        goodsDetail.setQuantity(1);
        goodsDetail.setGoods_id(WechatUtil.generateOrderSN());
        goodsDetailList.add(goodsDetail);
        detail.setGoods_detail(goodsDetailList);

        UnifiedorderResult unifiedorderResult
                = PayMchAPI.payUnifiedorder(unifiedorder, wechatKey);

        System.out.println(sandboxSignkey);
        System.out.println(unifiedorderResult);

        int width = 300;
        int height = 300;
        //二维码的图片格式
        String format = "JPEG";
        Hashtable hints = new Hashtable();
        //内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(unifiedorderResult.getCode_url(),
                BarcodeFormat.QR_CODE, width, height, hints);
        // response.setContentType("image/JPEG");

        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\78184\\Desktop\\1.jpeg"));
        MatrixToImageWriter.writeToStream(bitMatrix, format, out);
    }

}
