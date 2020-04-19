package top.zydse.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.zydse.cache.HotTagCache;
import top.zydse.dto.HotTagDTO;
import top.zydse.mapper.CommonExtensionMapper;
import top.zydse.mapper.QuestionMapper;
import top.zydse.model.QuestionExample;

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
    @Autowired
    private QuestionMapper questionMapper;

    @Scheduled(fixedRate = 1000 * 30)
    public void updateHotTag(){
        List<HotTagDTO> hotTagDTOList = extensionMapper.getTagPriority();
        hotTagCache.setHots(hotTagDTOList);
    }

    @Scheduled(fixedRate = 1000 * 30)
    public void updateLatestQuestion(){
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_modified");
        questionMapper.selectByExample(example);
//        hotTagCache.setHots(hotTagDTOList);
//        log.info("now you see me at {}",new Date());
//        log.info("update hotTagDTO list is : {}", hotTagDTOList);
    }
}
