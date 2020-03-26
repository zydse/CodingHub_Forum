package top.zydse.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.CommentDTO;
import top.zydse.enums.CommentTypeEnum;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.enums.NotificationType;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.CommentMapper;
import top.zydse.mapper.NotificationMapper;
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
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User user) {
        if (comment.getType().equals(CommentTypeEnum.QUESTION.getType())) {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            //增加回复数
            question.setCommentCount(question.getCommentCount() + 1);
            questionMapper.updateByPrimaryKey(question);
            //创建一个通知
            createNotification(comment, question, user);
        } else {
            // 回复评论
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null)
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            Question question = questionMapper.selectByPrimaryKey(parentComment.getParentId());
            if (question == null)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            //增加子回复数
            parentComment.setSubCommentCount(parentComment.getSubCommentCount() + 1);
            commentMapper.updateByPrimaryKey(parentComment);
            //创建一个通知
            createNotification(comment, parentComment, question, user);
        }
        //评论插入评论表
        commentMapper.insertSelective(comment);
    }

    //此方法用于子回复时，通知一级回复者
    private void createNotification(Comment comment, Comment parentComment, Question question, User user) {
        if(comment.getReviewer().equals(parentComment.getReviewer()))
            return;
        Notification notification = new Notification();
        notification.setNotifier(comment.getReviewer());
        notification.setReceiver(parentComment.getReviewer());
        notification.setType(NotificationType.ANSWER_COMMENT.getType());
        notification.setOuterId(comment.getParentId());
        notification.setOuterTitle(question.getTitle());
        notification.setNotifierName(user.getName());
        notification.setGmtCreate(System.currentTimeMillis());
        //通知插入通知表
        notificationMapper.insertSelective(notification);
    }

    //创建回复提问的通知
    private void createNotification(Comment comment, Question question, User user) {
        if(comment.getReviewer().equals(question.getCreator()))
            return;
        Notification notification = new Notification();
        notification.setNotifier(comment.getReviewer());
        notification.setReceiver(question.getCreator());
        notification.setType(NotificationType.ANSWER_QUESTION.getType());
        notification.setOuterId(comment.getParentId());
        notification.setOuterTitle(question.getTitle());
        notification.setNotifierName(user.getName());
        notification.setGmtCreate(System.currentTimeMillis());
        //通知插入通知表
        notificationMapper.insertSelective(notification);
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
