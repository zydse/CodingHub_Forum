package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: ThumbHistoryDTO
 * Description:
 *
 * @Date: 2020/3/29
 */
@Data
public class ThumbHistoryDTO implements Serializable {
    Long userId;
    Long commentId;
    Long questionId;
    String username;
    String comment;
    Long gmtCreate;
}
