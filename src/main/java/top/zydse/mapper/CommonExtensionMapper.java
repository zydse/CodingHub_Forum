package top.zydse.mapper;

import org.apache.ibatis.annotations.Param;
import top.zydse.dto.*;
import top.zydse.model.*;

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

    int insertQuestionTag(QuestionTag record);

    int saveUser(User user);

    List<String> getPermCodeByUser(Long userId);

    List<HotTagDTO> getTagPriority();

    List<Question> relatedQuestion(Long questionId);

    int readAllNotification(Long id);

    int countQuestionByTagId(Integer tagId);

    List<Question> findQuestionByTagId(@Param("tagId") Integer tagId,
                                       @Param("offset") int offset,
                                       @Param("size") Integer size);

    List<CollectionDTO> listCollection(@Param("userId") Long id,
                                       @Param("offset") int offset,
                                       @Param("size") int size);
}
