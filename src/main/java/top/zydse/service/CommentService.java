package top.zydse.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.CommentDTO;
import top.zydse.dto.SubCommentDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.enums.NotificationType;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.*;
import top.zydse.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private SubCommentMapper subCommentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private ThumbHistoryMapper thumbHistoryMapper;

    @Transactional
    public void insert(Comment comment, User user) {
        Question question = questionMapper.selectByPrimaryKey(comment.getQuestionId());
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        question.setCommentCount(question.getCommentCount() + 1);
        questionMapper.updateByPrimaryKey(question);
        //评论插入评论表
        commentMapper.insertSelective(comment);
        //创建一个通知
        createNotification(comment, question, user);
    }

    @Transactional
    public void insert(SubComment subComment, User user) {
        Comment comment = commentMapper.selectByPrimaryKey(subComment.getCommentId());
        if (comment == null)
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
        Question question = questionMapper.selectByPrimaryKey(comment.getQuestionId());
        //增加子回复数
        comment.setSubCommentCount(comment.getSubCommentCount() + 1);
        commentMapper.updateByPrimaryKey(comment);
        //创建两个通知
        //通知问题的创建者
        createNotification(comment, question, user);
        //通知评论的创建者
        createNotification(subComment, comment, user);
    }

    //此方法用于子回复时，通知一级回复者
    private void createNotification(SubComment subcomment, Comment comment, User user) {
        //如果二级回复人跟一级回复人相同，则不创建通知
        if (subcomment.getReviewer().equals(comment.getReviewer()))
            return;
        Notification notification = new Notification();
        notification.setNotifier(subcomment.getReviewer());
        notification.setReceiver(comment.getReviewer());
        notification.setOuterId(comment.getQuestionId());
        notification.setNotifierName(user.getName());
        notification.setType(NotificationType.ANSWER_COMMENT.getType());
        notification.setOuterTitle(comment.getContent().substring(0, 15) + "...");
        notification.setGmtCreate(System.currentTimeMillis());
        //通知插入通知表
        notificationMapper.insertSelective(notification);
    }

    //创建回复提问的通知
    private void createNotification(Comment comment, Question question, User user) {
        if (comment.getReviewer().equals(question.getCreator()))
            return;
        Notification notification = new Notification();
        notification.setNotifier(comment.getReviewer());
        notification.setReceiver(question.getCreator());
        notification.setOuterId(comment.getQuestionId());
        notification.setNotifierName(user.getName());
        notification.setType(NotificationType.ANSWER_QUESTION.getType());
        notification.setOuterTitle(question.getTitle());
        notification.setGmtCreate(System.currentTimeMillis());
        //通知插入通知表
        notificationMapper.insertSelective(notification);
    }

    public List<SubCommentDTO> listSubComment(Long commentId) {
        SubCommentExample subCommentExample = new SubCommentExample();
        subCommentExample.createCriteria()
                .andCommentIdEqualTo(commentId);
        subCommentExample.setOrderByClause("gmt_modified desc");
        List<SubComment> subComments = subCommentMapper.selectByExample(subCommentExample);
        if (subComments.size() == 0) {
            return new ArrayList<>();
        }
        // 从List中获取去重的评论人id
        List<Long> userIds = subComments.stream()
                .map(subComment -> subComment.getReviewer())
                .distinct()
                .collect(Collectors.toList());
        // 使用IN语句查询
        Map<Long, User> userMap = getUserMap(userIds);
        return subComments.stream().
                map(subComment -> {
                    SubCommentDTO subCommentDTO = new SubCommentDTO();
                    BeanUtils.copyProperties(subComment, subCommentDTO);
                    subCommentDTO.setUser(userMap.get(subComment.getReviewer()));
                    return subCommentDTO;
                }).collect(Collectors.toList());
    }

    public List<CommentDTO> listComment(Long questionId, User user) {
        List<ThumbHistory> thumbList = null;
        List<Long> thumbs = null;
        if (user != null) {
            ThumbHistoryExample thumbHistoryExample = new ThumbHistoryExample();
            thumbHistoryExample.createCriteria().
                    andThumbUserEqualTo(user.getId());
            thumbList = thumbHistoryMapper.selectByExample(thumbHistoryExample);
        }
        if (thumbList != null && thumbList.size() != 0) {
            thumbs = thumbList.stream().map(ThumbHistory::getCommentId).distinct().collect(Collectors.toList());
        }
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andQuestionIdEqualTo(questionId);
        commentExample.setOrderByClause("gmt_modified desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 从List中获取去重的评论人id
        List<Long> userIds = comments.stream()
                .map(Comment::getReviewer)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, User> userMap = getUserMap(userIds);
        List<CommentDTO> commentDTOList = comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    commentDTO.setUser(userMap.get(comment.getReviewer()));
                    commentDTO.setIsThumbUp(0);
                    return commentDTO;
                }).collect(Collectors.toList());
        if (thumbs != null && thumbs.size() != 0) {
            for (CommentDTO commentDTO : commentDTOList) {
                commentDTO.setIsThumbUp(thumbs.contains(commentDTO.getId()) ? 1 : 0);
            }
        }
        return commentDTOList;
    }

    @Transactional
    public long thumbUpComment(Long commentId, User user) {
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        ThumbHistoryExample thumbHistoryExample = new ThumbHistoryExample();
        thumbHistoryExample.createCriteria()
                .andCommentIdEqualTo(commentId)
                .andThumbUserEqualTo(user.getId());
        List<ThumbHistory> thumbHistories = thumbHistoryMapper.selectByExample(thumbHistoryExample);
        if(thumbHistories != null && thumbHistories.size() > 0){
            return comment.getThumbCount();
        }
        comment.setThumbCount(comment.getThumbCount() + 1);
        commentMapper.updateByPrimaryKeySelective(comment);
        return comment.getThumbCount() + 1;
    }

    @NotNull
    private Map<Long, User> getUserMap(List<Long> userIds) {
        // 使用IN语句查询
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> userList = userMapper.selectByExample(userExample);
        //User List转为Map
        return userList.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }
}
