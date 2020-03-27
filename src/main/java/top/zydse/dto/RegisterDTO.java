package top.zydse.dto;

import lombok.Data;

/**
 * CreateBy: zydse
 * ClassName: RegisterDTO
 * Description:
 *
 * @Date: 2020/3/27
 */
@Data
public class RegisterDTO {
    String username;
    String password;
    Long gmtCreate;
}
