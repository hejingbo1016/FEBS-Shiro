package cc.mrbird.febs.wechat.utils;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;

import java.io.InputStream;

@Data
public class WeiChatPayConfig implements WXPayConfig {

    /** 微信公众号ID */
    private String appID;

    private String key;

    /** 商户号 */
    private String mchID;

    /** HTTP(S) 连接超时时间，单位毫秒 */
    private int httpConnectTimeoutMs = 8000;

    /** HTTP(S) 读数据超时时间，单位毫秒 */
    private int httpReadTimeoutMs = 10000;


    @Override
    public InputStream getCertStream() {
        return null;
    }

}
