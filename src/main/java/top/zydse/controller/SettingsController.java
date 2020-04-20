package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.VerificationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.model.User;
import top.zydse.service.ProfileService;

import javax.servlet.http.HttpServletRequest;

/**
 * CreateBy: zydse
 * ClassName: SettingsController
 * Description:
 *
 * @Date: 2020/4/19
 */
@Controller
@RequestMapping("/settings")
@Slf4j
public class SettingsController {

    @Autowired
    private ProfileService profileService;

    @RequiresAuthentication
    @GetMapping("/{type}")
    public String profile(@PathVariable("type") String type,
                          Model model) {
        model.addAttribute("type", type);
        return "settings";
    }

    @PutMapping("/phone")
    public String phone(HttpServletRequest request,
                        @RequestParam(value = "phone") String phone,
                        @RequestParam(value = "code") String code) {
        VerificationDTO dto = (VerificationDTO) request.getSession().getAttribute("verificationCode");
        request.setAttribute("type", "phone");
        if (dto == null || !phone.equals(dto.getPhoneNumber()) || !code.equals(dto.getCode())){
            request.setAttribute("error", "错误的验证码");
            return "settings";
        }
        if(System.currentTimeMillis() - dto.getGmtCreate() > 1000 * 60 *15){
            request.setAttribute("error", CustomizeErrorCode.VERIFICATION_CODE_INACTIVE.getMessage());
            return "settings";
        }
        User user = (User) request.getSession().getAttribute("user");
        user.setPhoneNumber(phone);
        profileService.updatePhoneOrInfo(user);
        request.getSession().setAttribute("user", user);
        request.removeAttribute("verificationCode");
        request.setAttribute("msg", "操作成功");
        return "settings";
    }

    @PutMapping("/password")
    public String password(HttpServletRequest request,
                           @RequestParam(value = "original") String original,
                           @RequestParam(value = "retype") String retype,
                           @RequestParam(value = "new") String newPassword) {
        request.setAttribute("type", "password");
        if(!retype.equals(newPassword)){
            request.setAttribute("error", CustomizeErrorCode.PASSWORD_RETYPE_INCORRECT.getMessage());
            return "settings";
        }
        User user = (User) request.getSession().getAttribute("user");
        if(!user.getPassword().equals(original)){
            request.setAttribute("error", CustomizeErrorCode.ORIGINAL_PASSWORD_INCORRECT.getMessage());
            return "settings";
        }
        profileService.updatePassword(user.getId(), user.getSalt(), newPassword);
        user.setPassword(newPassword);
        SecurityUtils.getSubject().logout();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
        token.setRememberMe(false);
        log.info("new password:{}", newPassword);
        SecurityUtils.getSubject().login(token);
        request.getSession().setAttribute("user", user);
        request.setAttribute("msg", "操作成功，如果使用了<记住我>登录，修改密码后下次需要重新登录");
        return "settings";
    }

    @PutMapping("/profile")
    public String profile(HttpServletRequest request,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "bio", required = false) String bio) {
        request.setAttribute("type", "profile");
        User user = (User) request.getSession().getAttribute("user");
        if (bio != null){
            user.setBio(bio);
        }
        boolean flag = name.equals(user.getName());
        user.setName(name);
        String password = user.getPassword();
        user.setPassword(null);
        profileService.updatePhoneOrInfo(user);
        if (!flag) {
            SecurityUtils.getSubject().logout();
            UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), password);
            SecurityUtils.getSubject().login(token);
        }
        user.setPassword(password);
        request.getSession().setAttribute("user", user);
        request.setAttribute("msg", "操作成功");
        return "settings";
    }
}
