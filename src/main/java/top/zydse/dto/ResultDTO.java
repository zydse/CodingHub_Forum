package top.zydse.dto;

import lombok.Data;
import top.zydse.exception.CustomizeException;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: ResultDTO
 * Description:
 *
 * @Date: 2020/3/11
 */
@Data
public class ResultDTO<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO valueOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO successOf() {
        return valueOf(200, "请求成功");
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return valueOf(ex.getCode(), ex.getMessage());
    }

    public static <T> ResultDTO successOf(T data) {
        ResultDTO resultDTO = ResultDTO.successOf();
        resultDTO.setData(data);
        return resultDTO;
    }
}
