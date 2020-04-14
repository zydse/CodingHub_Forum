package top.zydse.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * CreateBy: zydse
 * ClassName: HotTagScheduled
 * Description:
 *
 * @Date: 2020/4/6
 */
@Component
@Slf4j
public class HotTagScheduled {

    @Scheduled(fixedRate = 10000)
    public void hotTag(){
        log.info("now you see me at {}",new Date());
    }
}
