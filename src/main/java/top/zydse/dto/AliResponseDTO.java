package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: AliResponseDTO
 * Description:
 *
 * @Date: 2020/3/27
 */
@Data
public class AliResponseDTO implements Serializable {
    private String message;
    private String requestId;
    private String code;
}
