package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.*;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.model.User;
import top.zydse.service.*;
import top.zydse.shiro.UserRealm;

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
    @Autowired
    private UserService userService;
    @Autowired
    private UserRealm userRealm;

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/publish")
    public String publish(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "8") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        log.info("model get user : {}", model.getAttribute("user"));
        model.addAttribute("section", "publish");
        model.addAttribute("sectionName", "我的提问");
        PaginationDTO<QuestionDTO> pagination = questionService.findAll(user.getId(), page, size);
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/notification")
    public String replies(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "8") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("section", "notification");
        model.addAttribute("sectionName", "我的通知");
        PaginationDTO<NotificationDTO> pagination = notificationService.list(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/view")
    public String view(Model model,
                       HttpServletRequest request,
                       @RequestParam(name = "page", defaultValue = "1") Integer page,
                       @RequestParam(name = "size", defaultValue = "8") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("section", "view");
        model.addAttribute("sectionName", "浏览历史");
        PaginationDTO<ViewHistoryDTO> pagination = profileService.viewHistory(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresPermissions("profile:retrieve")
    @GetMapping("/thumb")
    public String thumb(Model model,
                        HttpServletRequest request,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "8") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("section", "thumb");
        model.addAttribute("sectionName", "我的赞");
        PaginationDTO<ThumbHistoryDTO> pagination = profileService.thumbHistory(page, size, user.getId());
        model.addAttribute("pagination", pagination);
        return "profile";
    }

    @RequiresAuthentication
    @GetMapping("/info")
    public String userInfo(HttpServletRequest request) {
        request.setAttribute("section", "info");
        request.setAttribute("sectionName", "我的资料");
        return "profile";
    }

    @RequiresAuthentication
    @GetMapping("/collection")
    public String getCollection(HttpServletRequest request,
                                @RequestParam(name = "page", defaultValue = "1") Integer page,
                                @RequestParam(name = "size", defaultValue = "8") Integer size){
        User user = (User) request.getSession().getAttribute("user");
        PaginationDTO<CollectionDTO> pagination = profileService.collection(user.getId(), page, size);
        request.setAttribute("section", "collection");
        request.setAttribute("sectionName", "我的收藏");
        request.setAttribute("pagination", pagination);
        return "profile";
    }

    @GetMapping("/user/{userId}")
    public String userProfile(@PathVariable("userId") Long id,
                              @RequestParam(value = "s", required = false) String section,
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "size", defaultValue = "8") Integer size,
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
            PaginationDTO<QuestionDTO> pagination = questionService.findAll(id, page, size);
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

    @RequiresPermissions("user:delete")
    @DeleteMapping("/user/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId){
        userService.deleteById(userId);
        return "redirect:/";
    }
}
