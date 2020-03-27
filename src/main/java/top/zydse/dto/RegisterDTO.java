package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: RegisterDTO
 * Description:
 *
 * @Date: 2020/3/27
 */
@Data
public class RegisterDTO implements Serializable {
    String username;
    String password;
    String phoneNumber;
    String verifyCode;
    Long gmtCreate;
}
