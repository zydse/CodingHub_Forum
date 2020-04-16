package top.zydse.dto;

import lombok.Data;

/**
 * CreateBy: zydse
 * ClassName: HotTagDTO
 * Description:
 *
 * @Date: 2020/4/15
 */
@Data
public class HotTagDTO {
    Integer id;
    String tagName;
    Long questionCount;
    Long commentCount;
    Long priority;
}
