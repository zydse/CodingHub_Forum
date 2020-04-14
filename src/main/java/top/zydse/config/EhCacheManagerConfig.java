package top.zydse.config;

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CreateBy: zydse
 * ClassName: EhCacheManagerConfig
 * Description:
 *
 * @Date: 2020/3/31
 */
//@Configuration
public class EhCacheManagerConfig {
    //设置为共享模式
//    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }
}
