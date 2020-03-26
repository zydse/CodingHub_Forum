package top.zydse.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.CommentCreateDTO;
import top.zydse.dto.CommentDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.enums.CommentTypeEnum;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.model.Comment;
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
        Comment comment;
        comment = new Comment();
        comment.setContent(commentCreateDTO.getContent());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setReviewer(user.getId());
        commentService.insert(comment, user);
        return ResultDTO.successOf();
    }

    @ResponseBody
    @GetMapping("/comment/{parentId}")
    public ResultDTO<List<CommentDTO>> subComment(@PathVariable(name = "parentId") Long pid) {
        List<CommentDTO> commentDTOList = commentService.listByParentId(pid, CommentTypeEnum.COMMENT);
        return ResultDTO.successOf(commentDTOList);
    }
}
