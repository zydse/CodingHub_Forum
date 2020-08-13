package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: QuestionQueryDTO
 * Description:
 *
 * @Date: 2020/3/22
 */
@Data
public class QuestionQueryDTO implements Serializable {
    private String search;
    private int offset;
    private int size;
}
