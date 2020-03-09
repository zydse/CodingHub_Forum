package top.zydse.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.PageDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.mapper.QuestionMapper;
import top.zydse.mapper.UserMapper;
import top.zydse.model.Question;
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
        int totalCount = questionMapper.findTotal();
        int pageCount = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        page = page < 1 ? 1 : page;
        page = page > pageCount ? pageCount : page;
        pageDTO.setPagination(pageCount, page, size);
        int offset = (page - 1) * size;
        List<Question> questionList = questionMapper.findAll(offset, size);
        List<QuestionDTO> dtoList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            dtoList.add(questionDTO);
        }
        pageDTO.setQuestions(dtoList);
        return pageDTO;
    }
}
