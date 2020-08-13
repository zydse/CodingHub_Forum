package top.zydse.cache;

import lombok.Data;
import org.springframework.stereotype.Component;
import top.zydse.dto.HotTagDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: HotTagCache
 * Description:
 *
 * @Date: 2020/4/15
 */
@Component
@Data
public class HotTagCache {
    private List<HotTagDTO> hots = new ArrayList<>();
}
