package cc.mrbird.febs.wechat.utils;

import cc.mrbird.febs.common.utils.ConfigUtil;
import cc.mrbird.febs.common.utils.SpringContextUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.core.RedisTemplate;
import weixin.popular.api.PayMchAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.FollowResult;
import weixin.popular.bean.user.User;
import weixin.popular.bean.user.UserInfoList;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WechatUtil {

    private static final String WEIXIN_TOKEN_KEY = "wechatToken";
    private static final String APP_ID = ConfigUtil.getProperty("wechat.appid");
    private static final String APP_SECRET = ConfigUtil.getProperty("wechat.appSecret");

    /**
     * 获取token 存入redis 7000过期
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
     * 获取所有openid
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
}
