package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.*;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.model.User;
import top.zydse.service.CommentService;
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
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentService commentService;

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/publish")
    public String publish(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        UserProfileDTO dto = profileService.findUserById(user.getId());
        model.addAttribute("section", "publish");
        model.addAttribute("sectionName", "我的提问");
        request.setAttribute("userProfile", dto);
        PaginationDTO<QuestionDTO> pagination = questionService.findAll(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/notification")
    public String replies(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        UserProfileDTO dto = profileService.findUserById(user.getId());
        model.addAttribute("section", "notification");
        model.addAttribute("sectionName", "我的通知");
        request.setAttribute("userProfile", dto);
        PaginationDTO<NotificationDTO> pagination = notificationService.list(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/view")
    public String view(Model model,
                       HttpServletRequest request,
                       @RequestParam(name = "page", defaultValue = "1") Integer page,
                       @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        UserProfileDTO dto = profileService.findUserById(user.getId());
        model.addAttribute("section", "view");
        model.addAttribute("sectionName", "浏览历史");
        request.setAttribute("userProfile", dto);
        PaginationDTO<ViewHistoryDTO> pagination = profileService.viewHistory(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/thumb")
    public String thumb(Model model,
                        HttpServletRequest request,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        UserProfileDTO dto = profileService.findUserById(user.getId());
        model.addAttribute("section", "thumb");
        model.addAttribute("sectionName", "我的赞");
        request.setAttribute("userProfile", dto);
        PaginationDTO<ThumbHistoryDTO> pagination = profileService.thumbHistory(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresAuthentication
    @GetMapping("/info")
    public String userInfo(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        UserProfileDTO dto = profileService.findUserById(user.getId());
        request.setAttribute("section", "info");
        request.setAttribute("sectionName", "我的资料");
        request.setAttribute("userProfile", dto);
        return "profile";
    }

    @GetMapping("/user/{userId}")
    public String userProfile(@PathVariable("userId") Long id,
                              @RequestParam(value = "s", required = false) String section,
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "size", defaultValue = "5") Integer size,
                              HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.getId().equals(id))
            return "redirect:/profile/info";
        UserProfileDTO dto = profileService.findUserById(id);
        request.setAttribute("userProfile", dto);
        if (section == null) {
            request.setAttribute("section", "info");
            request.setAttribute("sectionName", "用户资料");
        }
        else if (section.equals("publish")) {
            PaginationDTO<QuestionDTO> pagination = questionService.findAll(page, size, id);
            request.setAttribute("pagination", pagination);
            request.setAttribute("section", "publish");
            request.setAttribute("sectionName", "历史提问");
        }
        else if (section.equals("comment")) {
            PaginationDTO<CommentDTO> pagination = commentService.findAllByUser(id, page, size);
            request.setAttribute("pagination", pagination);
            request.setAttribute("section", "comment");
            request.setAttribute("sectionName", "历史回复");
        }
        else {
            throw new CustomizeException(CustomizeErrorCode.BAD_REQUEST);
        }
        request.setAttribute("userId", id);
        return "user";
    }
}
