package top.zydse.provider;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.zydse.cache.HotQuestionCache;
import top.zydse.cache.HotTagCache;
import top.zydse.dto.HotTagDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.mapper.CommonExtensionMapper;
import top.zydse.mapper.QuestionMapper;
import top.zydse.model.Question;
import top.zydse.model.QuestionExample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * CreateBy: zydse
 * ClassName: HotTagScheduled
 * Description:
 *
 * @Date: 2020/4/6
 */
@Component
@Slf4j
public class ScheduledTask {
    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    private HotQuestionCache hotQuestionCache;
    @Autowired
    private CommonExtensionMapper extensionMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Scheduled(fixedRate = 1000 * 30)
    public void updateHotTag() {
        List<HotTagDTO> hotTagDTOList = extensionMapper.getTagPriority();
        hotTagCache.setHots(hotTagDTOList);
    }

    @Scheduled(fixedRate = 1000 * 30)
    public void updateLatestQuestion() {
        QuestionExample questionExample = new QuestionExample();
        long start = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
        questionExample.createCriteria().andGmtLastCommentGreaterThan(start);
        questionExample.setOrderByClause("comment_count desc, gmt_last_comment desc");
        List<Question> questionList = questionMapper.
                selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(0, 8));
        hotQuestionCache.setQuestions(questionList);
    }
}
