package top.zydse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.dto.QuestionQueryDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.*;
import top.zydse.model.*;
import top.zydse.service.QuestionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CreateBy: zydse
 * ClassName: QuestionService
 * Description:
 *
 * @Date: 2020/3/9
 */
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ViewHistoryMapper viewHistoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CommonExtensionMapper extensionMapper;

    //有查询条件的方法
    public PaginationDTO<QuestionDTO> findAll(String search, Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)) {
            search = search.replace(" ", "|");
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        int totalCount = extensionMapper.countBySearch(search);
        paginationDTO.setPagination(totalCount, page, size);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        QuestionQueryDTO queryDTO = new QuestionQueryDTO();
        queryDTO.setSearch(search);
        queryDTO.setOffset(offset);
        queryDTO.setSize(size);
        List<Question> questionList = extensionMapper.selectBySearch(queryDTO);
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    //没有查询条件的查询方法
    public PaginationDTO<QuestionDTO> findAll(Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        int totalCount = (int) questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalCount, page, size);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Question> questionList = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    public PaginationDTO<QuestionDTO> findAll(int page, int size, Long id) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        int totalCount = (int) questionMapper.countByExample(example);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    @NotNull
    private PaginationDTO<QuestionDTO> getQuestionDTOPaginationDTO(PaginationDTO<QuestionDTO> paginationDTO, List<Question> questionList) {
        List<QuestionDTO> dtoList = new ArrayList<>();
        for (Question plainQuestion : questionList) {
            User user = userMapper.selectByPrimaryKey(plainQuestion.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(plainQuestion, questionDTO);
            questionDTO.setUser(user);
            dtoList.add(questionDTO);
        }
        paginationDTO.setPageData(dtoList);

        return paginationDTO;
    }

    @Transactional
    public QuestionDTO viewQuestion(Long questionId, User viewer, boolean viewed) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        User creator = userMapper.selectByPrimaryKey(question.getCreator());
        if (viewer != null && !(viewed = viewer.getId().equals(creator.getId()))) {
            //登录用户，浏览别人发帖，记录浏览历史
            ViewHistoryExample viewHistoryExample = new ViewHistoryExample();
            viewHistoryExample.createCriteria()
                    .andViewerEqualTo(viewer.getId())
                    .andQuestionIdEqualTo(questionId);
            List<ViewHistory> viewHistories = viewHistoryMapper.selectByExample(viewHistoryExample);
            if (viewHistories.size() == 0) {
                //第一次浏览
                ViewHistory history = new ViewHistory();
                history.setViewer(viewer.getId());
                history.setQuestionId(questionId);
                history.setGmtCreate(System.currentTimeMillis());
                history.setGmtModified(history.getGmtCreate());
                viewHistoryMapper.insertSelective(history);
            } else {
                //重复浏览
                viewed = true;
                ViewHistory history = viewHistories.get(0);
                history.setGmtModified(System.currentTimeMillis());
                viewHistoryMapper.updateByPrimaryKey(history);
            }
        }
        if (!viewed)
            increaseViewCount(questionId);
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        String tags = extensionMapper.listTagsByQuestion(questionId)
                .stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList())
                .toString().replace(" ", "");
        dto.setTags(tags.substring(1, tags.length() - 1));
        dto.setUser(creator);
        return dto;
    }

    @Transactional
    public void saveOrUpdate(Question question, String tag) {
        String[] tags = tag.split(",");
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andTagNameIn(Arrays.asList(tags));
        List<Tag> tagList = tagMapper.selectByExample(tagExample);
        if (tags.length != tagList.size()) {
            throw new CustomizeException(CustomizeErrorCode.TAG_NOT_FOUND);
        }
        Map<String, Tag> tagMap = tagList.stream().collect(Collectors.toMap(Tag::getTagName, obj -> obj));
        if (question.getId() == null) {
            //创建一个问题
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            extensionMapper.savePublish(question);
            for (String t : tags) {
                Tag tagObj = tagMap.get(t);
                QuestionTag record = new QuestionTag();
                record.setQuestionId(question.getId());
                record.setTagId(tagObj.getId());
                record.setGmtCreate(System.currentTimeMillis());
                extensionMapper.insertQuestionTag(record);
                tagObj.setCount(tagObj.getCount() + 1);
                tagMapper.updateByPrimaryKey(tagObj);
            }
        } else {
            //更新问题信息
            question.setGmtModified(System.currentTimeMillis());
            int count = questionMapper.updateByPrimaryKeySelective(question);
            if (count != 1)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_ALREADY_DELETED);
        }
    }

    private void increaseViewCount(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setViewCount(question.getViewCount() + 1);
        questionMapper.updateByPrimaryKey(question);
    }

    public QuestionDTO findById(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        String tags = extensionMapper.listTagsByQuestion(questionId)
                .stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList())
                .toString().replace(" ", "");
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        user.setPassword(null);
        user.setToken(null);
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        dto.setTags(tags.substring(1, tags.length() - 1));
        dto.setUser(user);
        return dto;
    }
}
