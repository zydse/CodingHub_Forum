package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.RegisterDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.VerificationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.model.User;
import top.zydse.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public String login(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        if(refer.contains("/user/login") || refer.contains("/user/register"))
            refer = "/";
        request.getSession().setAttribute("referUrl", refer);
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultDTO login(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "isRemember", defaultValue = "0") Integer isRemember,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        String referUrl = (String) request.getSession().getAttribute("referUrl");
        User user = userService.checkUser(username, password);
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
        }
        if (isRemember != 1) {
            request.getSession().removeAttribute("referUrl");
            request.getSession().setAttribute("user", user);
            return ResultDTO.successOf(referUrl);
        }
        Cookie token = new Cookie("token", user.getToken());
        token.setPath("/");
        response.addCookie(token);
        request.getSession().removeAttribute("referUrl");
        return ResultDTO.successOf(referUrl);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        if(refer.contains("/user/login") || refer.contains("/user/register"))
            refer = "/";
        request.getSession().setAttribute("referUrl", refer);
        return "register";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDTO register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        VerificationDTO verificationCode = (VerificationDTO) request.getSession().getAttribute("verificationCode");
        ResultDTO resultDTO = userService.save(registerDTO, verificationCode);
        if(resultDTO.getCode() != 200)
            return resultDTO;
        User user = userService.checkUser(registerDTO.getUsername(), registerDTO.getPassword());
        String referUrl = (String) request.getSession().getAttribute("referUrl");
        request.getSession().setAttribute("user", user);
        return ResultDTO.successOf(referUrl);
    }

    @ResponseBody
    @RequestMapping(path = "/register/verifyUsername")
    public ResultDTO verifyUsername(@RequestParam("username") String username,
                                    @RequestParam("timestamp") Long timestamp) {
        User user = userService.selectByName(username);
        if (user != null) {
            return ResultDTO.errorOf(CustomizeErrorCode.DUPLICATE_USERNAME);
        }
        return ResultDTO.successOf();
    }

    @ResponseBody
    @RequestMapping(path = "/register/verifyCode")
    public ResultDTO verifyCode(@RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("timestamp") Long timestamp,
                                HttpServletRequest request) {
        ResultDTO resultDTO = userService.sendSms(phoneNumber, timestamp);
        if (resultDTO.getCode() == 200) {
            request.getSession().setAttribute("verificationCode", resultDTO.getData());
            resultDTO.setData(null);
        }
        return resultDTO;
    }

}
