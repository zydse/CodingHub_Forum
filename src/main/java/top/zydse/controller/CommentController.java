package top.zydse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zydse.dto.CommentDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.model.Comment;
import top.zydse.model.User;
import top.zydse.service.CommentService;

import javax.servlet.http.HttpServletRequest;

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
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment;
        comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setReviewer(user.getId());
        commentService.insert(comment);
        return ResultDTO.successOf(200, "请求成功");
    }
}
