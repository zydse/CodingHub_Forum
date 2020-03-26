package top.zydse.dto;

import lombok.Data;

/**
 * CreateBy: zydse
 * ClassName: FileResultDTO
 * Description:
 *
 * @Date: 2020/3/22
 */
@Data
public class FileResultDTO {
    private int success;
    private String message;
    private String url;
}
