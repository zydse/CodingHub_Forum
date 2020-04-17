package top.zydse.dto;

import lombok.Data;
import top.zydse.model.Tag;

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
    List<Tag> tags;
}
