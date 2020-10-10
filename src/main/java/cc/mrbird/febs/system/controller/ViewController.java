package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.authentication.ShiroHelper;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.*;
import cc.mrbird.febs.system.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Controller("systemView")
@RequiredArgsConstructor
public class ViewController extends BaseController {

    private final IUserService userService;
    private final ShiroHelper shiroHelper;
    private final IUserDataPermissionService userDataPermissionService;
    private final IPhoneService phoneService;
    private final IMeetingService meetingService;
    private final IHotelService hotelService;
    private final IFileService fileService;
    private final IWechatUserService wechatUserService;
    private final IPaymentService paymentService;
    private final IPaymentDetailsService paymentDetailsService;
    private final IMeetingHotelService meetingHotelService;
    private final IOrderInvoiceService orderInvoiceService;


    @GetMapping("login")
    @ResponseBody
    public Object login(HttpServletRequest request) {
        if (FebsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(FebsUtil.view("login"));
            return mav;
        }
    }

    @GetMapping("unauthorized")
    public String unauthorized() {
        return FebsUtil.view("error/403");
    }


    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("index")
    public String index(Model model) {
        AuthorizationInfo authorizationInfo = shiroHelper.getCurrentUserAuthorizationInfo();
        User user = super.getCurrentUser();
        User currentUserDetail = userService.findByName(user.getUsername());
        currentUserDetail.setPassword("It's a secret");
        model.addAttribute("user", currentUserDetail);
        model.addAttribute("permissions", authorizationInfo.getStringPermissions());
        model.addAttribute("roles", authorizationInfo.getRoles());
        return "index";
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "layout")
    public String layout() {
        return FebsUtil.view("layout");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "password/update")
    public String passwordUpdate() {
        return FebsUtil.view("system/user/passwordUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/profile")
    public String userProfile() {
        return FebsUtil.view("system/user/userProfile");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/avatar")
    public String userAvatar() {
        return FebsUtil.view("system/user/avatar");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/profile/update")
    public String profileUpdate() {
        return FebsUtil.view("system/user/profileUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user")
    @RequiresPermissions("user:view")
    public String systemUser() {
        return FebsUtil.view("system/user/user");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/add")
    @RequiresPermissions("user:add")
    public String systemUserAdd() {
        return FebsUtil.view("system/user/userAdd");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/audit/{id}")
    @RequiresPermissions("user:audit")
    public String userAudit(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return FebsUtil.view("system/user/userAudit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/detail/{username}")
    @RequiresPermissions("user:view")
    public String systemUserDetail(@PathVariable String username, Model model) {
        resolveUserModel(username, model, true);
        return FebsUtil.view("system/user/userDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/update/{username}")
    @RequiresPermissions("user:update")
    public String systemUserUpdate(@PathVariable String username, Model model) {
        resolveUserModel(username, model, false);
        return FebsUtil.view("system/user/userUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/role")
    @RequiresPermissions("role:view")
    public String systemRole() {
        return FebsUtil.view("system/role/role");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/menu")
    @RequiresPermissions("menu:view")
    public String systemMenu() {
        return FebsUtil.view("system/menu/menu");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/dept")
    @RequiresPermissions("dept:view")
    public String systemDept() {
        return FebsUtil.view("system/dept/dept");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/phone")
    @RequiresPermissions("phone:view")
    public String systemPhone() {
        return FebsUtil.view("system/phone/phone");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/phone/add")
    @RequiresPermissions("phone:add")
    public String systemPhoneAdd() {
        return FebsUtil.view("system/phone/phoneAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/phone/update/{id}")
    @RequiresPermissions("phone:update")
    public String phoneUpdate(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Phone phone = phoneService.getById(id);
        model.addAttribute("phone", phone);
        return FebsUtil.view("system/phone/phoneUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meeting")
    @RequiresPermissions("meeting:view")
    public String systemMeeting() {
        return FebsUtil.view("system/meeting/meeting");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meeting/add")
    @RequiresPermissions("meeting:add")
    public String systemMeetingAdd() {
        return FebsUtil.view("system/meeting/meetingAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meeting/update/{id}")
    @RequiresPermissions("meeting:update")
    public String meetingUpdate(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Meeting meeting = meetingService.getById(id);
        File file = new File();
        file.setForeignId(id);
        List<File> files = fileService.findFiles(file);
        model.addAttribute("files", files);
        model.addAttribute("meeting", meeting);

        return FebsUtil.view("system/meeting/meetingUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meeting/selectHotel")
    // @RequiresPermissions("meeting:selectHotel")
    public String selectHotel(Model model) {
        return FebsUtil.view("system/meeting/selectHotel");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/hotel")
    @RequiresPermissions("hotel:view")
    public String systemHotel() {
        return FebsUtil.view("system/hotel/hotel");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/hotel/add")
    @RequiresPermissions("hotel:add")
    public String systemHotelAdd() {
        return FebsUtil.view("system/hotel/hotelAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/hotel/update/{id}")
    @RequiresPermissions("hotel:update")
    public String hotelUpdate(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getById(id);
        File file = new File();
        file.setForeignId(id);
        List<File> files = fileService.findFiles(file);
        model.addAttribute("hotel", hotel);
        model.addAttribute("files", files);
        return FebsUtil.view("system/hotel/hotelUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/hotel/room/{id}")
    @RequiresPermissions("hotel:room")
    public String hotelRoom(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getById(id);
        model.addAttribute("hotel", hotel);
        return FebsUtil.view("system/hotel/hotelRoom");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/weChatUser")
    @RequiresPermissions("weChatUser:view")
    public String systemWeChatUser() {
        return FebsUtil.view("system/wechatUser/weChatUser");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/weChatUser/add")
    @RequiresPermissions("weChatUser:add")
    public String systemWeChatUserAdd() {
        return FebsUtil.view("system/wechatUser/weChatUserAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/weChatUser/update/{id}")
    @RequiresPermissions("weChatUser:update")
    public String weChatUserUpdate(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        WechatUser weChatUser = wechatUserService.getById(id);
        model.addAttribute("weChatUser", weChatUser);
        return FebsUtil.view("system/wechatUser/weChatUserUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/weChatMetting/update/{id}")
    public String weChatMetting(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Meeting weChatMetting = meetingService.getById(id);
        model.addAttribute("weChatMetting", weChatMetting);
        return FebsUtil.view("system/wechat/weChatMetting");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meeting/audit/{id}")
    @RequiresPermissions("meeting:audit")
    public String meetingAudit(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Meeting meeting = meetingService.getById(id);
        model.addAttribute("meeting", meeting);
        return FebsUtil.view("system/meeting/meetingAudit");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/payment")
    @RequiresPermissions("payment:view")
    public String systemPayment() {
        return FebsUtil.view("system/payment/payment");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/payment/add")
    @RequiresPermissions("payment:add")
    public String systemPaymentAdd() {
        return FebsUtil.view("system/payment/paymentAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/payment/update/{id}")
    @RequiresPermissions("payment:update")
    public String paymentUpdate(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Payment payment = paymentService.selectByPaymentId(id);
        model.addAttribute("payment", payment);
        return FebsUtil.view("system/payment/paymentUpdate");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/payment/orderInvoice/{paymentCode}")
    @RequiresPermissions("payment:invoice")
    public String getInvoiceByCode(@NotBlank(message = "{required}") @PathVariable String paymentCode, Model model) {

        OrderInvoice orderInvoice = orderInvoiceService.getOrderInvoice(paymentCode);
        model.addAttribute("orderInvoice", orderInvoice);
        model.addAttribute("paymentCode", paymentCode);
        return FebsUtil.view("system/payment/orderInvoice");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/paymentDetails/list")
    @RequiresPermissions("payment:details")
    public String hotelRoom() {
        return FebsUtil.view("system/payment/paymentDetails");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meeting/meetingHotel")
    @RequiresPermissions("meetingHotel:view")
    public String meetingHotel() {
        return FebsUtil.view("system/meeting/meetingHotel");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meetingHotel/update/{id}")
    @RequiresPermissions("meetingHotel:update")
    public String meetingHotelUpdate(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        MeetingHotel meetingHotel = meetingHotelService.selectMeetingHotelById(id);
        model.addAttribute("meetingHotel", meetingHotel);
        return FebsUtil.view("system/meeting/meetingHotelUpdate");
    }


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/meetingHotel/add")
    @RequiresPermissions("meetingHotel:add")
    public String meetingHotelAdd() {
        return FebsUtil.view("system/meeting/meetingHotelAdd");
    }



    @GetMapping(FebsConstant.VIEW_PREFIX + "system/payment/audit/{id}")
    @RequiresPermissions("payment:audit")
    public String paymentAudit(@NotBlank(message = "{required}") @PathVariable Long id, Model model) {
        Payment payment = paymentService.selectByPaymentId(id);
        model.addAttribute("payment", payment);
        return FebsUtil.view("system/payment/paymentAudit");
    }


    @RequestMapping(FebsConstant.VIEW_PREFIX + "index")
    public String pageIndex() {
        return FebsUtil.view("index");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "404")
    public String error404() {
        return FebsUtil.view("error/404");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "403")
    public String error403() {
        return FebsUtil.view("error/403");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "500")
    public String error500() {
        return FebsUtil.view("error/500");
    }

    private void resolveUserModel(String username, Model model, Boolean transform) {
        User user = userService.findByName(username);
        String deptIds = userDataPermissionService.findByUserId(String.valueOf(user.getUserId()));
        user.setDeptIds(deptIds);
        model.addAttribute("user", user);
        if (transform) {
            String sex = user.getSex();
            if (User.SEX_MALE.equals(sex)) {
                user.setSex("男");
            } else if (User.SEX_FEMALE.equals(sex)) {
                user.setSex("女");
            } else {
                user.setSex("保密");
            }
        }
        if (user.getLastLoginTime() != null) {
            model.addAttribute("lastLoginTime", DateUtil.getDateFormat(user.getLastLoginTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        }
    }
}
