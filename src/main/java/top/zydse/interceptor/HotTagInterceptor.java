package top.zydse.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.zydse.cache.HotTagCache;
import top.zydse.dto.HotTagDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: HotTagInterceptor
 * Description:
 *
 * @Date: 2020/4/17
 */
@Component
@Slf4j
public class HotTagInterceptor implements HandlerInterceptor {
    @Autowired
    private HotTagCache hotTagCache;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<HotTagDTO> tags = hotTagCache.getHots();
        request.setAttribute("hotTags", tags);
        return true;
    }
}
