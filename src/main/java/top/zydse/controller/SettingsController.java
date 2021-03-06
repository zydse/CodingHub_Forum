package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.VerificationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.model.User;
import top.zydse.provider.SensitiveWordFilter;
import top.zydse.service.ProfileService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * CreateBy: zydse
 * ClassName: SettingsController
 * Description:
 *
 * @Date: 2020/4/19
 */
@Controller
@RequiresAuthentication
@RequestMapping("/settings")
@Slf4j
public class SettingsController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private SensitiveWordFilter wordFilter;

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
        if (dto == null || !phone.equals(dto.getPhoneNumber()) || !code.equals(dto.getCode())) {
            request.setAttribute("error", "错误的验证码");
            return "settings";
        }
        if (System.currentTimeMillis() - dto.getGmtCreate() > 1000 * 60 * 15) {
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
        if (!retype.equals(newPassword)) {
            request.setAttribute("error", CustomizeErrorCode.PASSWORD_RETYPE_INCORRECT.getMessage());
            return "settings";
        }
        User user = (User) request.getSession().getAttribute("user");
        if (!user.getPassword().equals(original)) {
            request.setAttribute("error", CustomizeErrorCode.ORIGINAL_PASSWORD_INCORRECT.getMessage());
            return "settings";
        }
        profileService.updatePassword(user.getId(), user.getSalt(), newPassword);
        SecurityUtils.getSubject().logout();
        return "redirect:/user/toLogin";
    }

    @PutMapping("/profile")
    public String profile(HttpServletRequest request,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "bio", required = false) String bio) {
        request.setAttribute("type", "profile");
        if (wordFilter.isContainSensitiveWord(name + "..." + bio)) {
            request.setAttribute("error", CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_DESCRIPTION.getMessage());
            return "settings";
        }
        User user = (User) request.getSession().getAttribute("user");
        if (bio != null) {
            user.setBio(bio);
        }
        boolean flag = name.equals(user.getName());
        user.setName(name);
        String password = user.getPassword();
        user.setPassword(null);
        profileService.updatePhoneOrInfo(user);
        if (!flag) {
            SecurityUtils.getSubject().logout();
            return "redirect:/user/toLogin";
        }
        user.setPassword(password);
        request.getSession().setAttribute("user", user);
        request.setAttribute("msg", "操作成功");
        return "settings";
    }
}
