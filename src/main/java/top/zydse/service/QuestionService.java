package top.zydse.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.PageDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.exception.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.QuestionMapper;
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

    public PageDTO findAll(Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        int totalCount = (int) questionMapper.countByExample(new QuestionExample());
        int pageCount = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        page = page < 1 ? 1 : page;
        page = page > pageCount ? pageCount : page;
        pageDTO.setPagination(pageCount, page);
        int offset = (page - 1) * size;
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(
                example, new RowBounds(offset, size));
        List<QuestionDTO> dtoList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            dtoList.add(questionDTO);
        }
        pageDTO.setQuestions(dtoList);
        return pageDTO;
    }

    public PageDTO findAll(int page, int size, Integer id) {
        PageDTO pageDTO = new PageDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        int totalCount = (int) questionMapper.countByExample(example);
        int pageCount = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        page = page < 1 ? 1 : page;
        page = page > pageCount ? pageCount : page;
        pageDTO.setPagination(pageCount, page);
        int offset = (page - 1) * size;
        List<Question> plainQuestions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> dtoList = new ArrayList<>();
        for (Question plainQuestion : plainQuestions) {
            User user = userMapper.selectByPrimaryKey(plainQuestion.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(plainQuestion, questionDTO);
            questionDTO.setUser(user);
            dtoList.add(questionDTO);
        }
        pageDTO.setQuestions(dtoList);
        return pageDTO;
    }

    public QuestionDTO findById(Integer questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if(question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOND);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        dto.setUser(user);
        return dto;
    }

    public void saveOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建一个问题
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            //更新问题信息
            question.setGmtModified(System.currentTimeMillis());
            int count = questionMapper.updateByPrimaryKey(question);
            if(count != 1)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_ALREADY_DELETED);
        }
    }
}
