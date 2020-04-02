package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
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
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("准备退出");
        request.getSession().removeAttribute("user");
        log.info("移除user");
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        System.out.println(request.getSession());
        request.getSession().invalidate();
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResultDTO login(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password,
                        @RequestParam(name = "isRemember", defaultValue = "0") Integer isRemember,
                        HttpServletResponse response,
                        HttpServletRequest request) {
        log.info("username : {} password : {}",username, password);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            System.out.println(savedRequest != null);
            String url = savedRequest == null ? "/" : savedRequest.getRequestUrl();
            log.info("user request from :: " + url);
            User user = (User) subject.getPrincipal();
            log.info("login-ed user : " + user.getToken());
            if (isRemember == 1) {
                Cookie cookie = new Cookie("token", user.getToken());
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            request.getSession().setAttribute("user",user);
            return ResultDTO.successOf(url);
        } catch (UnknownAccountException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.USERNAME_INCORRECT);
        } catch (IncorrectCredentialsException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.PASSWORD_INCORRECT);
        } catch (AuthenticationException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
        }
    }

    @ResponseBody
    @PostMapping(value = "/register")
    public ResultDTO register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        VerificationDTO verificationCode = (VerificationDTO) request.getSession().getAttribute("verificationCode");
        ResultDTO resultDTO = userService.register(registerDTO, verificationCode);
        if (resultDTO.getCode() != 200)
            return resultDTO;
        UsernamePasswordToken token = new UsernamePasswordToken(registerDTO.getUsername(), registerDTO.getPassword());
        SecurityUtils.getSubject().login(token);
        log.info("request url from {}", WebUtils.getSavedRequest(request).getRequestUrl());
        return ResultDTO.successOf(WebUtils.getSavedRequest(request).getRequestUrl());
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
        ResultDTO<VerificationDTO> resultDTO = userService.sendSms(phoneNumber, timestamp);
        if (resultDTO.getCode() == 200) {
            request.getSession().setAttribute("verificationCode", resultDTO.getData());
            resultDTO.setData(null);
        }
        return resultDTO;
    }

}
