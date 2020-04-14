package top.zydse.service;

import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.dto.TagTypeDTO;
import top.zydse.model.Question;
import top.zydse.model.User;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: QuestionService
 * Description:
 *
 * @Date: 2020/3/31
 */
public interface QuestionService {

    /**
     * 根据查询条件查询所有相关问题
     * @param search
     * @param page
     * @param size
     * @return
     */
    PaginationDTO<QuestionDTO> findAll(String search, Integer page, Integer size);

    /**
     * 主页查看所有帖子
     * @param page
     * @param size
     * @return
     */
    PaginationDTO<QuestionDTO> findAll(Integer page, Integer size);

    /**
     * 根据用户id查询所有发帖记录
     * @param page
     * @param size
     * @param id 用户id
     * @return
     */
    PaginationDTO<QuestionDTO> findAll(int page, int size, Long id);

    /**
     * 点击问题后查看问题，记录浏览历史
     * @param questionId
     * @param viewer
     * @return
     */
    QuestionDTO viewQuestion(Long questionId, User viewer);

    /**
     * 新增或者修改一个提问
     * @param question
     * @param tags
     * @param avatarUrl
     */
    void saveOrUpdate(Question question, String tags, String avatarUrl);

    /**
     * 根据id查询提问详情
     * @param questionId
     * @return
     */
    QuestionDTO findById(Long questionId);


    /**
     * 根据传入的问题查询标签相同的问题
     * @param questionDTO
     * @return
     */
    List<Question> relatedQuestion(QuestionDTO questionDTO);

    List<TagTypeDTO> getAllTags();

    int deleteById(Long questionId);

    int top(Long questionId);

    int quality(Long questionId);

}
