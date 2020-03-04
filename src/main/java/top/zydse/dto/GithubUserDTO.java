package top.zydse.dto;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: GithubUserDTO
 * Description:
 *
 * @Date: 2020/3/3
 */
public class GithubUserDTO implements Serializable {
    private String name;
    private Long id;
    private String bio;
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "GithubUserDTO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
