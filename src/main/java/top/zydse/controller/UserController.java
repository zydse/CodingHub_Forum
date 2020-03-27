package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zydse.dto.AliResponseDTO;
import top.zydse.dto.RegisterDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.model.User;
import top.zydse.provider.AliMessageProvider;
import top.zydse.service.UserService;
import top.zydse.service.VerificationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.UUID;

/**
 * CreateBy: zydse
 * ClassName: UserController
 * Description:
 *
 * @Date: 2020/3/3
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String username, String password,HttpServletResponse response) {
        User user = userService.checkUser(username, password);
        if(user == null)
            return "redirect:/user/login";
        log.info("查有此人，设置cookie");
        Cookie token = new Cookie("token", user.getToken());
        token.setPath("/");
        response.addCookie(token);
        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "signup";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDTO register(@RequestBody RegisterDTO registerDTO) {
        return userService.save(registerDTO);
    }

    @ResponseBody
    @RequestMapping(path = "/register/verifyUsername")
    public ResultDTO verifyUsername(@Param("username") String username,
                                    @Param("timestamp") Long timestamp) {
        User user = userService.selectByName(username);
        if (user != null) {
            return ResultDTO.errorOf(CustomizeErrorCode.DUPLICATE_USERNAME);
        }
        return ResultDTO.successOf();
    }

    @ResponseBody
    @RequestMapping(path = "/register/verifyCode")
    public ResultDTO verifyCode(@Param("phoneNumber") String phoneNumber,
                                @Param("timestamp") Long timestamp) {
        return verificationService.sendSms(phoneNumber, timestamp);
    }

}
