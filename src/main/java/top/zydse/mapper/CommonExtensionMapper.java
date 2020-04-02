package top.zydse.mapper;

import org.apache.ibatis.annotations.Param;
import top.zydse.dto.QuestionQueryDTO;
import top.zydse.dto.ThumbHistoryDTO;
import top.zydse.dto.ViewHistoryDTO;
import top.zydse.model.Permission;
import top.zydse.model.Question;
import top.zydse.model.QuestionTag;
import top.zydse.model.Tag;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: CommonExtensionMapper
 * Description:
 *
 * @Date: 2020/3/29
 */
public interface CommonExtensionMapper {
    List<ViewHistoryDTO> listViewHistory(@Param("userId") Long userId,
                                         @Param("offset") Integer offset,
                                         @Param("size") Integer size);

    List<ThumbHistoryDTO> listThumbHistory(@Param("userId") Long userId,
                                           @Param("offset") Integer offset,
                                           @Param("size") Integer size);

    Long savePublish(Question question);

    List<Tag> listTagsByQuestion(Long questionId);

    int countBySearch(String search);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

    int insertQuestionTag(QuestionTag record);

    List<String> getPermCodeByUser(Long userId);
}
