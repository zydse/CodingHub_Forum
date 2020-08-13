package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: AccessTokenDTO
 * Description:
 *
 * @Date: 2020/3/3
 */
@Data
public class AccessTokenDTO implements Serializable {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private Integer state;
}
