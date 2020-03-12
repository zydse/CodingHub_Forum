package top.zydse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.enums.CommentTypeEnum;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.CommentMapper;
import top.zydse.mapper.QuestionMapper;
import top.zydse.model.Comment;
import top.zydse.model.Question;

/**
 * CreateBy: zydse
 * ClassName: CommentService
 * Description:
 *
 * @Date: 2020/3/11
 */
@Service
public class CommentService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TARGET_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_NOT_FOUND);
        }

        if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            question.setCommentCount(question.getCommentCount() + 1);
            questionMapper.updateByPrimaryKey(question);
        } else {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null)
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }
        commentMapper.insert(comment);
    }
}
