package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.zydse.dto.NotificationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.model.User;
import top.zydse.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

/**
 * CreateBy: zydse
 * ClassName: NotificationController
 * Description:
 *
 * @Date: 2020/3/22
 */
@Controller
@Slf4j
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @RequiresPermissions("profile:retrieve")
    @RequestMapping("/notification/{id}")
    public String notification(HttpServletRequest request,
                               @PathVariable(name = "id") Long id) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        NotificationDTO notificationDTO = notificationService.read(id, user);
        return "redirect:/question/" + notificationDTO.getOuterId();
    }

    @RequiresAuthentication
    @RequestMapping("/notification/all")
    public String readAll(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        int result = notificationService.readAll(user);
        log.info("{} read {} notifications ", user.getName(), result);
        return "redirect:/profile/notification";
    }
}
