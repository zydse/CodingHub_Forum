package top.zydse.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
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
import top.zydse.provider.SensitiveWordFilter;
import top.zydse.service.UserService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Set;

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
    @Autowired
    private DefaultKaptcha captchaProducer;
    @Autowired
    private SensitiveWordFilter wordFilter;

    @ResponseBody
    @PostMapping("/login")
    public ResultDTO login(@RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "rememberMe") Integer rememberMe,
                           @RequestParam(name = "captchaCode") String captchaCode,
                           HttpServletRequest request) {
        String sessionCaptcha = (String) request.getSession().getAttribute("captchaCode");
        if (sessionCaptcha == null || !sessionCaptcha.equals(captchaCode)) {
            return ResultDTO.errorOf(CustomizeErrorCode.CAPTCHA_CODE_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe == 1);
        try {
            subject.login(token);
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            String url = savedRequest == null ? "/" : savedRequest.getRequestUrl();
            User user = (User) subject.getPrincipal();
            request.getSession().setAttribute("user", user);
            request.getSession().removeAttribute("captchaCode");
            return ResultDTO.successOf(url);
        } catch (UnknownAccountException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.USERNAME_INCORRECT);
        } catch (IncorrectCredentialsException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.PASSWORD_INCORRECT);
        } catch (AuthenticationException e) {
            return ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping("/defaultCaptcha")
    public void code(@RequestParam(value = "d", required = false) Long timestamp,
                     HttpServletResponse response,
                     HttpServletRequest request) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            request.getSession().setAttribute("captchaCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @ResponseBody
    @PostMapping(value = "/register")
    public ResultDTO register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        if (wordFilter.isContainSensitiveWord(registerDTO.getUsername())) {
            return ResultDTO.errorOf(CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_USERNAME);
        }
        VerificationDTO verificationCode = (VerificationDTO) request.getSession().getAttribute("verificationCode");
        ResultDTO resultDTO = userService.register(registerDTO, verificationCode);
        if (resultDTO.getCode() != 200)
            return resultDTO;
        UsernamePasswordToken token = new UsernamePasswordToken(registerDTO.getUsername(), registerDTO.getPassword());
        SecurityUtils.getSubject().login(token);
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        String url = savedRequest == null ? "/" : savedRequest.getRequestUrl();
        request.getSession().setAttribute("user", resultDTO.getData());
        request.getSession().removeAttribute("verificationCode");
        return ResultDTO.successOf(url);
    }

    @ResponseBody
    @RequestMapping(path = "/register/verifyUsername")
    public ResultDTO verifyUsername(@RequestParam("username") String username,
                                    @RequestParam("timestamp") Long timestamp) {
        if (wordFilter.isContainSensitiveWord(username)) {
            return ResultDTO.errorOf(CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_USERNAME);
        }
        User user = userService.selectByName(username);
        if (user != null) {
            return ResultDTO.errorOf(CustomizeErrorCode.DUPLICATE_USERNAME);
        }
        return ResultDTO.successOf();
    }

    @ResponseBody
    @RequestMapping(path = "/register/verifyPhone")
    public ResultDTO verifyPhone(@RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("timestamp") Long timestamp) {
        User user = userService.selectByPhone(phoneNumber);
        if (user != null) {
            return ResultDTO.errorOf(CustomizeErrorCode.DUPLICATE_PHONE_NUMBER);
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

    @ResponseBody
    @RequestMapping("/reset")
    public ResultDTO resetPassword(HttpServletRequest request, @RequestBody RegisterDTO registerDTO) {
        VerificationDTO dto = (VerificationDTO) request.getSession().getAttribute("verificationCode");
        if (dto == null || !registerDTO.getPhoneNumber().equals(dto.getPhoneNumber())
                || !registerDTO.getVerifyCode().equals(dto.getCode())) {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_ERROR);
        }
        if (System.currentTimeMillis() - dto.getGmtCreate() > 1000 * 60 * 15) {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_INACTIVE);
        }
        userService.resetPassword(registerDTO.getPassword(), registerDTO.getPhoneNumber());
        request.getSession().removeAttribute("verificationCode");
        return ResultDTO.successOf();
    }

}
