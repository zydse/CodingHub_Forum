package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: FileResultDTO
 * Description:
 *
 * @Date: 2020/3/22
 */
@Data
public class FileResultDTO implements Serializable {
    private int success;
    private String message;
    private String url;
}
