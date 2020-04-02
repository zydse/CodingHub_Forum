package top.zydse.service;

import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.model.Question;
import top.zydse.model.User;

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

    QuestionDTO viewQuestion(Long questionId, User viewer, boolean viewed);

    void saveOrUpdate(Question question, String tag);

    QuestionDTO findById(Long questionId);
}
