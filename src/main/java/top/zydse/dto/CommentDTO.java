package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: CommentDTO
 * Description:
 *
 * @Date: 2020/3/11
 */
@Data
public class CommentDTO implements Serializable {
    private Long parentId;
    private String content;
    private Integer type;
}
