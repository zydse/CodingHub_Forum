package top.zydse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.zydse.interceptor.HotTagInterceptor;
import top.zydse.interceptor.SessionInterceptor;

import java.util.Arrays;

/**
 * CreateBy: zydse
 * ClassName: WebConfig
 * Description:
 *
 * @Date: 2020/3/10
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Autowired
    private HotTagInterceptor hotTagInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(Arrays.asList("/js/**", "/css/**", "/images/**",
                        "/fonts/**", "/favicon.ico", "/user/**", "/error", "/file/**"));
        registry.addInterceptor(hotTagInterceptor)
                .addPathPatterns("/", "/question/search", "/question/trend",
                        "/question/emptyComment", "/question/tags/**", "/question/allTags");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/toLogin").setViewName("login");
        registry.addViewController("/user/toRegister").setViewName("register");
        registry.addViewController("/401").setViewName("401");
    }
}
