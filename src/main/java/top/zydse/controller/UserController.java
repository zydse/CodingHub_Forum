package top.zydse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.PageDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.mapper.UserMapper;
import top.zydse.model.User;
import top.zydse.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: UserController
 * Description:
 *
 * @Date: 2020/3/3
 */
@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    User user = userMapper.findByToken(cookie.getValue());
                    if (user != null) {
                        System.out.println("from userController:   " + user);
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        PageDTO pagination = questionService.findAll(page, size);
        model.addAttribute("pagination", pagination);
        return "index";
    }

}
