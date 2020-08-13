package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: ViewHistoryDTO
 * Description:
 *
 * @Date: 2020/3/29
 */
@Data
public class ViewHistoryDTO implements Serializable {
    Long userId;
    Long questionId;
    String username;
    String title;
    Long gmtModified;
}
