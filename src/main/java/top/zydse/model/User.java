package top.zydse.model;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: User
 * Description:
 *
 * @Date: 2020/3/4
 */
@Data
public class User implements Serializable {
    private Integer id;
    private String accountId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String bio;
    private String avatarUrl;
}
