package top.zydse.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * CreateBy: zydse
 * ClassName: SensitiveWordFilter
 * Description:
 *
 * @Date: 2020/4/1
 */
@Component
public class SensitiveWordFilter {
    @Value("${sensitiveWord.matchType}")
    private int matchType; //matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则

    @SuppressWarnings("rawtypes")
    public Map sensitiveWordMap = null;
    public static int minMatchTYpe = 1;      //最小匹配规则
    public static int maxMatchType = 2;      //最大匹配规则

    /**
     * 构造函数，初始化敏感词库
     */

    public SensitiveWordFilter(SensitiveWordInit initiator) {
        sensitiveWordMap = initiator.initKeyWord();
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt       文字
     * @return 若包含返回true，否则返回false
     * @version 1.0
     */
    public boolean isContainSensitiveWord(String txt) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = this.CheckSensitiveWord(txt, i); //判断是否包含敏感字符
            if (matchFlag > 0) {    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：
     *
     * @param txt
     * @param beginIndex
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes"})
    public int CheckSensitiveWord(String txt, int beginIndex) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (SensitiveWordFilter.minMatchTYpe == matchType) {    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        if (matchFlag < 2 || !flag) {        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt 要检测的文本
     * @return
     * @version 1.0
     */
    public Set<String> getSensitiveWord(String txt) {
        Set<String> sensitiveWordList = new HashSet<>();
        for (int i = 0; i < txt.length(); i++) {
            int length = CheckSensitiveWord(txt, i);    //判断是否包含敏感字符
            if (length > 0) {    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }
        return sensitiveWordList;
    }
}
