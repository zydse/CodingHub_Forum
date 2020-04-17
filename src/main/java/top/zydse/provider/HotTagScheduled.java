package top.zydse.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.zydse.cache.HotTagCache;
import top.zydse.dto.HotTagDTO;
import top.zydse.mapper.CommonExtensionMapper;

import java.util.Date;
import java.util.List;

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
    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    private CommonExtensionMapper extensionMapper;

    @Scheduled(fixedRate = 1000 * 30)
    public void updateHotTag(){
        List<HotTagDTO> hotTagDTOList = extensionMapper.getTagPriority();
        hotTagCache.setHots(hotTagDTOList);
        log.info("now you see me at {}",new Date());
        log.info("update hotTagDTO list is : {}", hotTagDTOList);
    }
}
