package cc.mrbird.febs.wechat.utils;

import cc.mrbird.febs.common.utils.ConfigUtil;
import cc.mrbird.febs.common.utils.SpringContextUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import weixin.popular.api.TokenAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.FollowResult;
import weixin.popular.bean.user.User;
import weixin.popular.bean.user.UserInfoList;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WechatUtil {

    private static final String WEIXIN_TOKEN_KEY = "wechatToken";
    private static final String APP_ID = ConfigUtil.getProperty("wechat.appid");
    private static final String APP_SECRET = ConfigUtil.getProperty("wechat.appSecret");
    private static final String WEB_ACCESS_TOKEN = ConfigUtil.getProperty("wechat.webAccessToken");
    private static final String WEB_SHOUQUAN_URL = ConfigUtil.getProperty("wechat.shouquanUrl");
    private static final String AB = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static Random rnd = new Random();

    /**
     * 获取token 存入redis 7000过期
     *
     * @return
     */
    public static synchronized String getToken() {
        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate");
        Object o = redisTemplate.opsForValue().get(WEIXIN_TOKEN_KEY);
        if (o == null) {
            Token token = TokenAPI.token(APP_ID, APP_SECRET);
            String access_token = token.getAccess_token();
            redisTemplate.opsForValue().set(WEIXIN_TOKEN_KEY, access_token, 7000, TimeUnit.SECONDS);
            return access_token;
        } else {
            return o.toString();
        }
    }

    /**
     * 获取所有用户信息
     *
     * @param nextOpenid
     * @param userList
     */
    public static void getUserList(String nextOpenid, List<User> userList) {
        String token = getToken();
        FollowResult followResult = UserAPI.userGet(token, nextOpenid);

        // 是否还有用户
        String next_openid = followResult.getNext_openid();
        if (StrUtil.isNotBlank(next_openid)) {
            getUserList(next_openid, userList);
        }

        FollowResult.Data data = followResult.getData();
        if (data != null) {
            String[] openidArr = data.getOpenid();
            if (openidArr != null && openidArr.length > 0) {
                List<List<String>> openidGroupList = CollUtil.split(CollUtil.toList(openidArr), 1000);

                // 多线程获取
                openidGroupList.parallelStream().forEach(e -> {
                    UserInfoList userInfoList = UserAPI.userInfoBatchget(token, "zh_CN", e);
                    if (userInfoList != null && userInfoList.getUser_info_list() != null)
                        userList.addAll(userInfoList.getUser_info_list());
                });
            }
        }
    }

    /**
     * 根据openid 获取用户信息
     *
     * @param openidList
     * @return
     */
    public static List<User> getUserList(List<String> openidList) {

        if (CollUtil.isEmpty(openidList))
            return null;

        String token = getToken();
        List<List<String>> openidGroupList = CollUtil.split(openidList, 1000);

        List<User> userList = CollUtil.newArrayList();

        // 多线程获取
        openidGroupList.parallelStream().forEach(e -> {
            UserInfoList userInfoList = UserAPI.userInfoBatchget(token, "zh_CN", e);
            if (userInfoList != null && userInfoList.getUser_info_list() != null)
                userList.addAll(userInfoList.getUser_info_list());
        });

        return userList;
    }


    /**
     * 根据openid 获取用户信息
     *
     * @param openid
     * @return
     */
    public static User getUser(String openid) {
        if (StringUtils.isEmpty(openid)) {
            return null;
        }
        String token = getToken();
        User user = UserAPI.userInfo(token, openid);

        return user;
    }

    /**
     * 获取所有openid
     *
     * @param nextOpenid
     * @param openidList
     */
    public static void getOpenidList(String nextOpenid, List<String> openidList) {
        String token = getToken();
        FollowResult followResult = UserAPI.userGet(token, nextOpenid);

        // 是否还有用户
        String next_openid = followResult.getNext_openid();
        if (StrUtil.isNotBlank(next_openid)) {
            getOpenidList(next_openid, openidList);
        }

        FollowResult.Data data = followResult.getData();
        if (data != null) {
            String[] openidArr = data.getOpenid();
            if (openidArr != null && openidArr.length > 0) {
                openidList.addAll(CollUtil.toList(openidArr));
            }
        }
    }

    public static String createNonceStr() {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.replaceAll("\\-", "").toUpperCase();
    }

    /**
     * 针对微信支付生成商户订单号，为了避免微信商户订单号重复（下单单位支付），
     *
     * @return
     */
    public static String generateOrderSN() {
        StringBuffer orderSNBuffer = new StringBuffer();
        orderSNBuffer.append(System.currentTimeMillis());
        orderSNBuffer.append(getRandomString(7));
        return orderSNBuffer.toString();
    }

    /**
     * 获取随机字符串
     *
     * @param len
     * @return
     */
    public static String getRandomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /**
     * 获取网页授权信息
     *
     * @param code
     * @return
     */
    public static User getWebAccessToken(String code) {

        String url = WEB_SHOUQUAN_URL + WEB_ACCESS_TOKEN + "&appid=" + APP_ID + "&secret=" + APP_SECRET + "&code=" + code;
        String json = HttpUtil.get(url);
        User vo = JSON.parseObject(json, User.class);

        if (StringUtils.isEmpty(vo.getErrcode())) {
            return null;
        }
        return vo;
    }
}