package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.*;
import top.zydse.model.User;
import top.zydse.service.NotificationService;
import top.zydse.service.ProfileService;
import top.zydse.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

/**
 * CreateBy: zydse
 * ClassName: Profile
 * Description:
 *
 * @Date: 2020/3/9
 */
@Slf4j
@Controller
@RequiresAuthentication
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProfileService profileService;

    @GetMapping("/publish")
    public String publish(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "未登录");
            return "redirect:/";
        }
        model.addAttribute("section", "publish");
        model.addAttribute("sectionName", "我的提问");
        PaginationDTO<QuestionDTO> pagination = questionService.findAll(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @GetMapping("/notification")
    public String replies(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "未登录");
            return "redirect:/";
        }
        model.addAttribute("section", "notification");
        model.addAttribute("sectionName", "我的通知");
        PaginationDTO<NotificationDTO> pagination = notificationService.list(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @GetMapping("/view")
    public String view(Model model,
                       HttpServletRequest request,
                       @RequestParam(name = "page", defaultValue = "1") Integer page,
                       @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "未登录");
            return "redirect:/";
        }
        model.addAttribute("section", "view");
        model.addAttribute("sectionName", "浏览历史");
        PaginationDTO<ViewHistoryDTO> pagination = profileService.viewHistory(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @GetMapping("/thumb")
    public String thumb(Model model,
                        HttpServletRequest request,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "未登录");
            return "redirect:/";
        }
        model.addAttribute("section", "thumb");
        model.addAttribute("sectionName", "我的赞");
        PaginationDTO<ThumbHistoryDTO> pagination = profileService.thumbHistory(page, size, user.getId());
        log.info("pagination length:" + pagination.getPageCount());
        model.addAttribute("pagination", pagination);
        return "profile";
    }
}
