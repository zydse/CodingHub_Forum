package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: GithubUserDTO
 * Description:
 *
 * @Date: 2020/3/3
 */
@Data
public class GithubUserDTO implements Serializable {
    private String name;
    private Long id;
    private String bio;
    private String login;
    private String avatarUrl;
}
