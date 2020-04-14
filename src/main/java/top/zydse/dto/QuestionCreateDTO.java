package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: QuestionCreateDTO
 * Description:
 *
 * @Date: 2020/4/14
 */
@Data
public class QuestionCreateDTO implements Serializable {
    String title;
    String description;
    String tags;
    Long id;
}
