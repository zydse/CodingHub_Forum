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
public class CommentDTO implements Serializable {
    private Long id;
    private Long questionId;
    private Long reviewer;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer thumbCount;
    private Integer subCommentCount;
    private Integer isThumbUp;
    private User user;
}
