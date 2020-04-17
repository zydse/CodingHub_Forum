package top.zydse.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.zydse.mapper.UserMapper;
import top.zydse.model.User;
import top.zydse.model.UserExample;
import top.zydse.service.impl.NotificationServiceImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: SessionInterceptor
 * Description:
 *
 * @Date: 2020/3/10
 */
@Component
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private NotificationServiceImpl notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            long count = notificationService.unreadCount(user.getId());
            request.setAttribute("unreadCount", count);
        }
        return true;
    }
}
