package top.zydse.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.CommentDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.SubCommentDTO;
import top.zydse.elasticsearch.dao.PublishRepository;
import top.zydse.elasticsearch.entity.Publish;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.enums.NotificationType;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.*;
import top.zydse.model.*;
import top.zydse.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CreateBy: zydse
 * ClassName: CommentService
 * Description:
 *
 * @Date: 2020/3/11
 */
@Service
public class CommentServiceImpl implements CommentService {
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
    @Autowired
    private PublishRepository publishRepository;

    @Transactional
    public void insert(Comment comment, User user) {
        Question question = questionMapper.selectByPrimaryKey(comment.getQuestionId());
        if (question == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        //增加问题回复数
        question.setCommentCount(question.getCommentCount() + 1);
        questionMapper.updateByPrimaryKey(question);
        //更新elasticsearch
        Optional<Publish> optional = publishRepository.findById(question.getId());
        if (!optional.isPresent())
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        Publish publish = optional.get();
        publish.setCommentCount(publish.getCommentCount() + 1);
        publishRepository.save(publish);
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
        //增加问题的回复数
        question.setCommentCount(question.getCommentCount() + 1);
        questionMapper.updateByPrimaryKey(question);
        //更新elasticsearch
        Optional<Publish> optional = publishRepository.findById(question.getId());
        if (!optional.isPresent())
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        Publish publish = optional.get();
        publish.setCommentCount(publish.getCommentCount() + 1);
        publishRepository.save(publish);
        //存储子回复
        subCommentMapper.insertSelective(subComment);
        //子评论的通知，可能是通知一级评论人和问题创建者
        createNotification(subComment, comment, question, user);
    }

    //此方法用于子回复时，通知一级回复者
    private void createNotification(SubComment subcomment, Comment comment, Question question, User user) {
        //通知一级评论人
        //如果二级回复人跟一级回复人不同创建通知
        if (!subcomment.getReviewer().equals(comment.getReviewer())) {
            Notification notification = new Notification();
            //消息发送者
            notification.setNotifier(subcomment.getReviewer());
            //消息接收者
            notification.setReceiver(comment.getReviewer());
            notification.setOuterId(comment.getQuestionId());
            notification.setNotifierName(user.getName());
            notification.setType(NotificationType.ANSWER_COMMENT.getType());
            if (comment.getContent().length() < 15) {
                notification.setOuterTitle(comment.getContent());
            } else {
                notification.setOuterTitle(comment.getContent().substring(0, 15) + "...");
            }
            notification.setGmtCreate(System.currentTimeMillis());
            //通知插入通知表
            notificationMapper.insertSelective(notification);
        }
        //通知问题创建者
        if (!subcomment.getReviewer().equals(question.getCreator())) {
            Notification notification = new Notification();
            //消息发送者
            notification.setNotifier(subcomment.getReviewer());
            //消息接收者
            notification.setReceiver(question.getCreator());
            notification.setOuterId(question.getId());
            notification.setNotifierName(user.getName());
            notification.setType(NotificationType.ANSWER_QUESTION.getType());
            notification.setOuterTitle(question.getTitle());
            notification.setGmtCreate(System.currentTimeMillis());
            //通知插入通知表
            notificationMapper.insertSelective(notification);
        }
    }

    //创建一级回复通知提问者
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
        commentExample.setOrderByClause("thumb_count desc, gmt_modified desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 从List中获取去重的评论人id
        List<Long> userIds = comments.stream()
                .map(Comment::getReviewer)
                .distinct()
                .collect(Collectors.toList());
        //IN查询
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
    public ResultDTO thumbUpComment(Long commentId, User user) {
        //查找要点赞的评论
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        //自己点赞自己
        if (comment.getReviewer().equals(user.getId())) {
            return ResultDTO.errorOf(CustomizeErrorCode.THUMB_UP_SELF);
        }
        //查询用户的点赞历史
        ThumbHistoryExample thumbHistoryExample = new ThumbHistoryExample();
        thumbHistoryExample.createCriteria()
                .andCommentIdEqualTo(commentId)
                .andThumbUserEqualTo(user.getId());
        List<ThumbHistory> thumbHistories = thumbHistoryMapper.selectByExample(thumbHistoryExample);
        //已经点赞过，无法点赞
        if (thumbHistories.size() > 0) {
            return ResultDTO.errorOf(CustomizeErrorCode.DUPLICATE_THUMB_UP);
        }
        //生成一个点赞历史
        ThumbHistory record = new ThumbHistory();
        record.setCommentId(commentId);
        record.setThumbUser(user.getId());
        record.setGmtCreate(System.currentTimeMillis());
        thumbHistoryMapper.insertSelective(record);
        //生成一个通知
        Notification notification = new Notification();
        notification.setNotifier(user.getId());
        notification.setReceiver(comment.getReviewer());
        notification.setType(NotificationType.THUMB_UP.getType());
        notification.setOuterId(comment.getQuestionId());
        if (comment.getContent().length() < 15) {
            notification.setOuterTitle(comment.getContent());
        } else {
            notification.setOuterTitle(comment.getContent().substring(0, 15) + "...");
        }
        notification.setNotifierName(user.getName());
        notification.setGmtCreate(System.currentTimeMillis());
        notificationMapper.insertSelective(notification);
        //点赞数加一
        comment.setThumbCount(comment.getThumbCount() + 1);
        commentMapper.updateByPrimaryKeySelective(comment);
        return ResultDTO.successOf(comment.getThumbCount());
    }

    @Override
    public PaginationDTO<CommentDTO> findAllByUser(Long id, Integer page, Integer size) {
        CommentExample example = new CommentExample();
        example.createCriteria().andReviewerEqualTo(id);
        int totalCount = (int) commentMapper.countByExample(example);
        PaginationDTO<CommentDTO> paginationDTO = new PaginationDTO<>();
        if (totalCount == 0)
            return paginationDTO;
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Comment> commentList = commentMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<CommentDTO> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO dto = new CommentDTO();
            BeanUtils.copyProperties(comment, dto);
            dtoList.add(dto);
        }
        paginationDTO.setPageData(dtoList);
        return paginationDTO;
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
