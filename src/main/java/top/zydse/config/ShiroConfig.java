package top.zydse.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import top.zydse.shiro.UserRealm;

import javax.servlet.DispatcherType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CreateBy: zydse
 * ClassName: ShiroConfig
 * Description:
 *
 * @Date: 2020/3/31
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * 注册一个EhCache的缓存管理器
     *
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }

    /**
     * 开启Shiro注解
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 创建一个ShiroDialect，支持Thymeleaf
     */
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 创建realm
     */
    @Bean
    public UserRealm getUserRealm(EhCacheManager ehCacheManager) {
        UserRealm userRealm = new UserRealm();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        userRealm.setCredentialsMatcher(matcher);
        userRealm.setCacheManager(ehCacheManager);
        return userRealm;
    }

    /**
     * 注册一个权限过滤器，并将权限过滤器绑定一个安全管理器
     *
     * @param manager
     * @return
     */
    @Bean(name = "myFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getSecurityManager") DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setLoginUrl("/user/toLogin");
        factoryBean.setUnauthorizedUrl("/401");
        factoryBean.setSecurityManager(manager);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/user/logout", "logout");
        map.put("/**", "anon");
        factoryBean.setFilterChainDefinitionMap(map);
        return factoryBean;
    }

    /**
     * 似乎开启aop支持后需要这样一步来让权限过滤器起作用
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("myFilter");
        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.ERROR,
                DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.INCLUDE);
        return filterRegistrationBean;
    }

    /**
     * 注册一个rememberMeCookie
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(60 * 60 * 24 * 30);
        return simpleCookie;
    }

    /**
     * 注册一个rememberMeManager
     *
     * @param simpleCookie
     * @return
     */
    @Bean
    public RememberMeManager getRememberMeManager(SimpleCookie simpleCookie) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
        rememberMeManager.setCipherKey(cipherKey);
        rememberMeManager.setCookie(simpleCookie);
        return rememberMeManager;
    }

    @Bean
    public DefaultWebSessionManager webSessionManager() {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setSessionIdUrlRewritingEnabled(false);
        SimpleCookie sessionIdCookie = new SimpleCookie();
        sessionIdCookie.setName("sessionID");
        webSessionManager.setSessionIdCookie(sessionIdCookie);
        webSessionManager.setSessionIdCookieEnabled(true);
        return webSessionManager;
    }

    /**
     * 注册核心安全管理器
     *
     * @param realm
     * @param ehCacheManager
     * @param rememberMeManager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getSecurityManager(
            @Qualifier("getUserRealm") Realm realm,
            EhCacheManager ehCacheManager,
            DefaultWebSessionManager webSessionManager,
            RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        manager.setCacheManager(ehCacheManager);
        manager.setRememberMeManager(rememberMeManager);
        manager.setSessionManager(webSessionManager);
        return manager;
    }
}
