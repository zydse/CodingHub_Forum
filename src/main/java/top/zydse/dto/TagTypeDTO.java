package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: TagTypeDTO
 * Description:
 *
 * @Date: 2020/3/30
 */
@Data
public class TagTypeDTO implements Serializable {
    String type;
    List<String> tags;
}
