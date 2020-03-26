package top.zydse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.NotificationDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.model.User;
import top.zydse.service.NotificationService;
import top.zydse.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

/**
 * CreateBy: zydse
 * ClassName: Profile
 * Description:
 *
 * @Date: 2020/3/9
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "未登录");
            return "redirect:/";
        }
//        long unreadCount = notificationService.unreadCount(user.getId());
//        model.addAttribute("unreadCount", unreadCount);
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO<QuestionDTO> pagination = questionService.findAll(page, size, user.getId());
            model.addAttribute("pagination", pagination);
        }
        if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "我的回复");
            PaginationDTO<NotificationDTO> pagination = notificationService.list(page, size, user.getId());
            model.addAttribute("pagination", pagination);
        }
        return "profile";
    }
}
