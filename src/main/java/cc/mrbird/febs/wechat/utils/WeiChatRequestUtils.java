package cc.mrbird.febs.wechat.utils;

import cc.mrbird.febs.common.utils.RedisUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WeiChatRequestUtils {

    // 公众号appid
    private String APPID = "wx13f30efdeca98d8a";

    // 公众号密钥
    private String SECRET ="40972028a294e4701194fe2ed4e38f72";

    // 商户平台 设置的密钥key
    private String KEY;

    private String SPBILL_CREATE_IP = "120.24.81.225";

    private String APP_SERVER_HOST;

    // 商户号id
    private String MCHID;

    //公众号Token
    private String TOKEN = "test";

    //公众号EncodingAESKey
    private String ENCODINGAESKEY = "QT6vdPe3aqN7hEG9WXjE2JnDuYpL4aTruuHk3ZjHfeY";

    //
    private String OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STATE&connect_redirect=1#wechat_redirect";

    private String GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private String REDIRECT_URL = "http://knightmedia.ltd/login";

    private String WX_PAY_NOTIFY_URL = "/api/csOrder/notify";

    private String GET_ACCESS_TOKEN_URL= "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtils redisUtils;

    public String getOauthUrl(){
        String reUrl = String.format(OAUTH2_AUTHORIZE_URL, APPID, URLEncoder.encode(REDIRECT_URL),"snsapi_base",null);
        log.info(reUrl);
        return reUrl;
    }

    public String getOpenid(String code){
        String url = String.format(GET_OPENID_URL, APPID, SECRET, code);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url,null, Map.class);
        if (responseEntity.getStatusCode().value() != 200){
            log.info(responseEntity.getStatusCode().toString());
            return null;
        }
        Map map = responseEntity.getBody();
        log.info(map.toString());
        return (String) map.get("openid");
    }

    public Map<String, String> wxPay(String orderName, String orderNo, Double totalFee, String openid) throws Exception {
        WeiChatPayConfig weiChatPayConfig = new WeiChatPayConfig();
        weiChatPayConfig.setAppID(APPID);
        weiChatPayConfig.setKey(KEY);
        weiChatPayConfig.setMchID(MCHID);
        WXPay wxpay = new WXPay(weiChatPayConfig);
        Map<String, String> data = new HashMap<>();
        data.put("device_info", "WEB"); // 设备号 - 非必填
        data.put("fee_type", "CNY"); // 标价币种 - 非必填
        data.put("spbill_create_ip", SPBILL_CREATE_IP); // 终端IP-必填
        data.put("notify_url", APP_SERVER_HOST + WX_PAY_NOTIFY_URL); // 通知地址-必填
        data.put("trade_type", "JSAPI"); // 取值为：JSAPI - 交易类型 - 必填
        data.put("openid", openid); // 当trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。

        data.put("body", orderName); // 商品描述-必填
        data.put("out_trade_no", orderNo); // 商户订单号-必填
        totalFee = totalFee * 100;
        String fee = String.valueOf(totalFee);
        fee = fee.substring(0,fee.lastIndexOf("."));
        data.put("total_fee", fee); // 标价金额 - 必填 - 单位为分
        return wxpay.unifiedOrder(data);
    }

    public Map<String, String> sign(Map<String, String> map) throws Exception {
        Map<String, String> result = new HashMap<>(8);

        // 公帐号ID
        result.put("appId", APPID);
        // 时间戳
        result.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 随机串
        result.put("nonceStr", map.get("nonce_str"));
        // 数据包，统一下单接口返回的 prepay_id 参数值
        result.put("package", "prepay_id=" + map.get("prepay_id"));
        // 签名方式
        result.put("signType", "MD5");
        // 第一个参数为待签名的数据，第二个参数为获取API密钥
        String sign = WXPayUtil.generateSignature(result, KEY);
        result.put("paySign", sign);

        return result;
    }

    public boolean isPayResultNotifySignatureValid(Map<String, String> map) throws Exception {
        WeiChatPayConfig weiChatPayConfig = new WeiChatPayConfig();
        weiChatPayConfig.setKey(KEY);
        WXPay wxpay = new WXPay(weiChatPayConfig);
        return wxpay.isPayResultNotifySignatureValid(map);
    }

    public String getAccessToken(){
        String accessToken = null;
        if (redisUtils.isKeyExists("ACCESS_TOKEN")){
            accessToken = (String) redisUtils.get("ACCESS_TOKEN");
            return accessToken;
        }

        String getAccessTokenUrl = String.format(GET_ACCESS_TOKEN_URL,APPID,SECRET);
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(getAccessTokenUrl, Map.class);
        if (responseEntity.getStatusCode().value() != 200){
            log.error("获取access_token失败："+responseEntity.getStatusCode().toString());
            return accessToken;
        }
        Map map = responseEntity.getBody();
        accessToken = (String) map.get("access_token");
        Integer expires = (Integer) map.get("expires_in");
        log.info("accessToken: "+ accessToken);
        log.info("expires: "+ expires);
        redisUtils.set("ACCESS_TOKEN",accessToken,expires.longValue(), TimeUnit.SECONDS);
        return accessToken;
    }

}
