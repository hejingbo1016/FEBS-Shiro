package cc.mrbird.febs.wechat.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.dto.ResponseDTO;
import cc.mrbird.febs.system.service.IWechatUserService;
import cc.mrbird.febs.wechat.dto.GetCodeDTO;
import cc.mrbird.febs.wechat.utils.WeiChatRequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wechatController")
public class WechatController extends BaseController {

    private final IWechatUserService wechatUserService;

    private final WeiChatRequestUtils weiChatRequestUtils;

    @RequestMapping(value = "/oauth")
    public ResponseDTO oauth() {

        return ResponseDTO.success(weiChatRequestUtils.getOauthUrl());
    }

    @RequestMapping(value = "/getCode")
    public ResponseDTO getCode(@RequestBody GetCodeDTO getCodeDTO) {
        log.info("code: " + getCodeDTO.getCode());
//        String openid = weiChatRequestUtils.getOpenid(getCodeDTO.getCode());
//        if (StringUtils.isEmpty(openid)){
//            return ResponseDTO.failture("获取openid失败，授权失败");
//        }
        return wechatUserService.registerUser(getCodeDTO);
    }

    @RequestMapping(value = "/verifyToken")
    public String verifyToken(String signature, String timestamp, String nonce, String echostr) {
        return echostr;
    }

    @ControllerEndpoint(operation = "根据openid，把未存入数据库中的用户存进去", exceptionMessage = "* 根据openid，把未存入数据库中的用户存进去失败")
    @RequestMapping(value = "/getUserByOpenid")
    public void getTest(String openid) {
        wechatUserService.getTest(openid);
    }



    @ControllerEndpoint(operation = "根据code，把未存入数据库中的用户存进去", exceptionMessage = "* 根据openid，把未存入数据库中的用户存进去失败")
    @RequestMapping(value = "/getTestByCode")
    public void getTestByCode(String code) {
        wechatUserService.getTestByCode(code);
    }

}



