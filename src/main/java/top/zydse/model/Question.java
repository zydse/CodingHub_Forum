package top.zydse.model;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: Question
 * Description:
 *
 * @Date: 2020/3/4
 */
@Data
public class Question implements Serializable {
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
}
