package top.zydse.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.CommentDTO;
import top.zydse.enums.CommentTypeEnum;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.CommentMapper;
import top.zydse.mapper.QuestionMapper;
import top.zydse.mapper.UserMapper;
import top.zydse.model.*;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
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

    public List<CommentDTO> listByParentId(Long parentId, CommentTypeEnum question) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(parentId)
                .andTypeEqualTo(question.getType());
        example.setOrderByClause("gmt_modified desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 从comment List中获取去重的评论人id
        List<Long> userIds = comments.stream()
                .map(Comment::getReviewer)
                .distinct()
                .collect(Collectors.toList());

        // 使用IN语句查询
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> userList = userMapper.selectByExample(userExample);
        //User List转为Map
        Map<Long, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        return comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getReviewer()));
            return commentDTO;
        }).collect(Collectors.toList());
    }
}
