package top.zydse.mapper;

import top.zydse.dto.QuestionQueryDTO;
import top.zydse.model.Question;

import java.util.List;

public interface QuestionMapperExt {
    int increaseViewCount(Question record);

    int increaseThumbCount(Question record);

    List<Question> selectRelevant(Question record);

    int countBySearch(String search);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

}