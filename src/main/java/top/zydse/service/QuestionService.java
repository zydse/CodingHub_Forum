package top.zydse.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.dto.QuestionQueryDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.QuestionMapper;
import top.zydse.mapper.QuestionMapperExt;
import top.zydse.mapper.UserMapper;
import top.zydse.model.Question;
import top.zydse.model.QuestionExample;
import top.zydse.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: QuestionService
 * Description:
 *
 * @Date: 2020/3/9
 */
@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionMapperExt questionMapperExt;

    //有查询条件的方法
    public PaginationDTO<QuestionDTO> findAll(String search, Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)) {
            search = search.replace(" ", "|");
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        int totalCount = questionMapperExt.countBySearch(search);
        paginationDTO.setPagination(totalCount, page, size);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        QuestionQueryDTO queryDTO = new QuestionQueryDTO();
        queryDTO.setSearch(search);
        queryDTO.setOffset(offset);
        queryDTO.setSize(size);
        List<Question> questionList = questionMapperExt.selectBySearch(queryDTO);
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
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        return getQuestionDTOPaginationDTO(paginationDTO, questionList);
    }

    public PaginationDTO<QuestionDTO> findAll(int page, int size, Long id) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        int totalCount = (int) questionMapper.countByExample(example);
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

    public QuestionDTO findById(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        dto.setTags("标签1,标签2");
        dto.setUser(user);
        return dto;
    }

    public void saveOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建一个问题
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        } else {
            //更新问题信息
            question.setGmtModified(System.currentTimeMillis());
            int count = questionMapper.updateByPrimaryKeySelective(question);
            if (count != 1)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_ALREADY_DELETED);
        }
    }

    public void increaseViewCount(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setViewCount(question.getViewCount() + 1);
        questionMapper.updateByPrimaryKey(question);
    }
}
