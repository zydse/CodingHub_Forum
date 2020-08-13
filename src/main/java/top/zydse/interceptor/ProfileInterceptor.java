package top.zydse.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.zydse.dto.UserProfileDTO;
import top.zydse.model.User;
import top.zydse.service.ProfileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CreateBy: zydse
 * ClassName: UserProfileInterceptor
 * Description:
 *
 * @Date: 2020/4/18
 */
@Component
public class ProfileInterceptor implements HandlerInterceptor {
    @Autowired
    private ProfileService profileService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null)
            return true;
        UserProfileDTO dto = profileService.findUserById(user.getId());
        request.setAttribute("userProfile", dto);
        return true;
    }
}
