package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: UserProfileDTO
 * Description:
 *
 * @Date: 2020/4/15
 */
@Data
public class UserProfileDTO implements Serializable {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
    private Long publishNum;
    private Long commentNum;
    private Integer thumbedNum;
}
