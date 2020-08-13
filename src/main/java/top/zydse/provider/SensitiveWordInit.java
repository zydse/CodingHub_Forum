package top.zydse.provider;

import io.netty.util.internal.ResourcesUtil;
import org.apache.shiro.io.ResourceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * CreateBy: zydse
 * ClassName: SensitiveWordInit
 * Description:
 *
 * @Date: 2020/4/1
 */
@Component("sensitiveWordInit")
public class SensitiveWordInit {

    @Value("${sensitiveWord.path}")
    private String filePath;
    @Value("${sensitiveWord.encoding}")
    private String encoding;

    @SuppressWarnings("rawtypes")
    public HashMap sensitiveWordMap;

    @SuppressWarnings("rawtypes")
    public Map initKeyWord() {
        try {
            Set<String> keyWordSet = readSensitiveWordFile();
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    //将得到的敏感词库用一个DFA算法模型放到map中
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);

                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    //读取敏感词文件 加到set集合中
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception{
        Set<String> set = null;
        InputStreamReader read = null;
        try {

            InputStream file = ResourceUtils.getInputStreamForPath(filePath);
            if (file != null) {
                read = new InputStreamReader(file, encoding);
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) {
                    set.add(txt);
                }
            } else {
                throw new Exception("敏感词库文件不存在");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (read != null)
                read.close();
        }
        return set;
    }
}
