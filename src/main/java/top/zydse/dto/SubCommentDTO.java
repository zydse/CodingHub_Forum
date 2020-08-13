package top.zydse.dto;

import lombok.Data;
import top.zydse.model.User;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: CommentDTO
 * Description:
 *
 * @Date: 2020/3/13
 */
@Data
public class SubCommentDTO implements Serializable {
    private Long id;
    private Long questionId;
    private Long commentId;
    private Long reviewer;
    private Long gmtCreate;
    private Long gmtModified;
    private String content;
    private User user;
}
