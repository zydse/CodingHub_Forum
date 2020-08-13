package top.zydse.model;

import lombok.Data;

@Data
public class QuestionTag {
    private Long id;
    private Long questionId;
    private Integer tagId;
    private Long gmtCreate;
}