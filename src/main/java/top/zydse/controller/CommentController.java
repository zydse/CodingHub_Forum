package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.CommentCreateDTO;
import top.zydse.dto.CommentDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.SubCommentDTO;
import top.zydse.enums.CommentTypeEnum;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.model.Comment;
import top.zydse.model.SubComment;
import top.zydse.model.User;
import top.zydse.provider.SensitiveWordFilter;
import top.zydse.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: CommentController
 * Description:
 *
 * @Date: 2020/3/11
 */
@Controller
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private SensitiveWordFilter wordFilter;

    @RequiresPermissions("comment:create")
    @PostMapping("/comment")
    @ResponseBody
    public ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO,
                          HttpServletRequest request) {
        if (commentCreateDTO.getParentId() == null || commentCreateDTO.getParentId() == 0) {
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_TARGET_NOT_FOUND);
        }
        if (commentCreateDTO.getType() == null || !CommentTypeEnum.isExist(commentCreateDTO.getType())) {
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_TYPE_NOT_FOUND);
        }
        if (StringUtils.isBlank(commentCreateDTO.getContent()))
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY_OR_TO_SHORT);
        User user = (User) request.getSession().getAttribute("user");
        if (wordFilter.isContainSensitiveWord(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_DESCRIPTION);
        }
        if (commentCreateDTO.getType().equals(1)) {
            Comment comment = new Comment();
            comment.setReviewer(user.getId());
            comment.setContent(commentCreateDTO.getContent());
            comment.setQuestionId(commentCreateDTO.getParentId());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(comment.getGmtCreate());
            commentService.insert(comment, user);
            return ResultDTO.successOf();
        } else {
            SubComment subComment = new SubComment();
            subComment.setReviewer(user.getId());
            subComment.setCommentId(commentCreateDTO.getParentId());
            subComment.setContent(commentCreateDTO.getContent());
            subComment.setGmtCreate(System.currentTimeMillis());
            subComment.setGmtModified(subComment.getGmtCreate());
            commentService.insert(subComment, user);
        }
        return ResultDTO.successOf();
    }

    @ResponseBody
    @GetMapping("/comment/list/{commentId}")
    public ResultDTO<List<CommentDTO>> subComment(@PathVariable(name = "commentId") Long commentId) {
        List<SubCommentDTO> subCommentDTO = commentService.listSubComment(commentId);
        return ResultDTO.successOf(subCommentDTO);
    }

    @RequiresPermissions("comment:thumb")
    @ResponseBody
    @PostMapping("/comment/thumb")
    public ResultDTO thumbComment(@RequestParam(name = "commentId") Long commentId,
                                  HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return commentService.thumbUpComment(commentId, user);
    }

}
