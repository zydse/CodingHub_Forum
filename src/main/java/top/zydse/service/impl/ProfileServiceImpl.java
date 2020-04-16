package top.zydse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.ThumbHistoryDTO;
import top.zydse.dto.UserProfileDTO;
import top.zydse.dto.ViewHistoryDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.*;
import top.zydse.model.*;
import top.zydse.service.ProfileService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CreateBy: zydse
 * ClassName: ProfileService
 * Description:
 *
 * @Date: 2020/3/29
 */
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private CommonExtensionMapper commonExtensionMapper;
    @Autowired
    private ViewHistoryMapper viewHistoryMapper;
    @Autowired
    private ThumbHistoryMapper thumbHistoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SubCommentMapper subCommentMapper;

    public PaginationDTO<ViewHistoryDTO> viewHistory(Integer page, Integer size, Long userId) {
        PaginationDTO<ViewHistoryDTO> paginationDTO = new PaginationDTO<>();
        ViewHistoryExample viewHistoryExample = new ViewHistoryExample();
        viewHistoryExample.createCriteria()
                .andViewerEqualTo(userId);
        viewHistoryExample.setOrderByClause("gmt_modified desc");
        int totalCount = (int) viewHistoryMapper.countByExample(viewHistoryExample);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<ViewHistoryDTO> histories = commonExtensionMapper.listViewHistory(userId, offset, size);
        paginationDTO.setPageData(histories);
        return paginationDTO;
    }

    public PaginationDTO<ThumbHistoryDTO> thumbHistory(Integer page, Integer size, Long userId) {
        PaginationDTO<ThumbHistoryDTO> paginationDTO = new PaginationDTO<>();
        ThumbHistoryExample thumbHistoryExample = new ThumbHistoryExample();
        thumbHistoryExample.createCriteria()
                .andThumbUserEqualTo(userId);
        thumbHistoryExample.setOrderByClause("gmt_create desc");
        int totalCount = (int) thumbHistoryMapper.countByExample(thumbHistoryExample);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<ThumbHistoryDTO> thumbHistoryDTOS = commonExtensionMapper.listThumbHistory(userId, offset, size);
        paginationDTO.setPageData(thumbHistoryDTOS);
        return paginationDTO;
    }

    @Override
    public UserProfileDTO findUserById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null)
            throw new CustomizeException(CustomizeErrorCode.BAD_REQUEST);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(user, userProfileDTO);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        long publishNum = questionMapper.countByExample(questionExample);
        userProfileDTO.setPublishNum(publishNum);
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andReviewerEqualTo(id);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        SubCommentExample subCommentExample = new SubCommentExample();
        subCommentExample.createCriteria().andReviewerEqualTo(id);
        long subCommentNum = subCommentMapper.countByExample(subCommentExample);
        userProfileDTO.setCommentNum(commentList.size() + subCommentNum);
        if(commentList.size() == 0)
            userProfileDTO.setThumbedNum(0);
        else {
            Integer thumbedNum = commentList.stream()
                    .map(Comment::getThumbCount)
                    .reduce(Integer::sum)
                    .get();
            userProfileDTO.setThumbedNum(thumbedNum);
        }
        return userProfileDTO;
    }
}
