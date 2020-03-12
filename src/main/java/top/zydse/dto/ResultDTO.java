package top.zydse.dto;

import lombok.Data;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.enums.ICustomizeErrorCode;
import top.zydse.exception.CustomizeException;

/**
 * CreateBy: zydse
 * ClassName: ResultDTO
 * Description:
 *
 * @Date: 2020/3/11
 */
@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO valueOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode noLogin) {
        return valueOf(noLogin.getCode(), noLogin.getMessage());
    }

    public static ResultDTO successOf(int code, String message) {
        return valueOf(code, message);
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return valueOf(ex.getCode(), ex.getMessage());
    }
}
