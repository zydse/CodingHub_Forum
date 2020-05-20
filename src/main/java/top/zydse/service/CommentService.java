package top.zydse.service;

import top.zydse.dto.CommentDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.SubCommentDTO;
import top.zydse.model.Comment;
import top.zydse.model.SubComment;
import top.zydse.model.User;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: CommentService
 * Description:
 *
 * @Date: 2020/3/31
 */
public interface CommentService {
    void insert(Comment comment, User user);

    void insert(SubComment subComment, User user);

    List<SubCommentDTO> listSubComment(Long commentId);

    List<CommentDTO> listComment(Long questionId, User user);

    ResultDTO thumbUpComment(Long commentId, User user);

    PaginationDTO<CommentDTO> findAllByUser(Long id, Integer page, Integer size);

    int deleteCommentByUserId(Long userId);

    int deleteThumbHistoryByUserId(Long userId);
}
