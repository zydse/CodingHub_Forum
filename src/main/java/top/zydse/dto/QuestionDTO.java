package top.zydse.dto;

import lombok.Data;
import top.zydse.model.User;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: QuestionDTO
 * Description:
 *
 * @Date: 2020/3/9
 */
@Data
public class QuestionDTO implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private String tags;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer thumbCount;
    private User user;
}
