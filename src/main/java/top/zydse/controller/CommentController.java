package top.zydse.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.CommentCreateDTO;
import top.zydse.dto.CommentDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.SubCommentDTO;
import top.zydse.enums.CommentTypeEnum;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.model.Comment;
import top.zydse.model.SubComment;
import top.zydse.model.User;
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
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping("/comment")
    public ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO.getParentId() == null || commentCreateDTO.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TARGET_NOT_FOUND);
        }
        if (commentCreateDTO.getType() == null || !CommentTypeEnum.isExist(commentCreateDTO.getType())) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_NOT_FOUND);
        }
        if (StringUtils.isBlank(commentCreateDTO.getContent()))
            throw new CustomizeException(CustomizeErrorCode.CONTENT_IS_EMPTY_OR_TO_SHORT);
        if (commentCreateDTO.getType().equals(1)) {
            Comment comment = new Comment();
            comment.setReviewer(user.getId());
            comment.setContent(commentCreateDTO.getContent());
            comment.setQuestionId(commentCreateDTO.getParentId());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(comment.getGmtCreate());
            commentService.insert(comment, user);
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

    @ResponseBody
    @PostMapping("/comment/thumb")
    public ResultDTO thumbComment(@RequestParam(name = "commentId") Long commentId,
                                  HttpServletRequest request) {
        System.out.println(commentId);
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        long thumb = commentService.thumbUpComment(commentId, user);
        return ResultDTO.successOf(thumb);
    }

}
