package top.zydse.cache;

import lombok.Data;
import org.springframework.stereotype.Component;
import top.zydse.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: HotQuestionCache
 * Description:
 *
 * @Date: 2020/4/22
 */
@Component
@Data
public class HotQuestionCache {
    private List<Question> questions = new ArrayList<>();
}
