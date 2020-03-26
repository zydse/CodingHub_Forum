package top.zydse.dto;

import lombok.Data;

/**
 * CreateBy: zydse
 * ClassName: QuestionQueryDTO
 * Description:
 *
 * @Date: 2020/3/22
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private int offset;
    private int size;
}
