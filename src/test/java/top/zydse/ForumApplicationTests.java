package top.zydse;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import top.zydse.elasticsearch.dao.PublishRepository;
import top.zydse.elasticsearch.entity.Publish;
import top.zydse.provider.SensitiveWordFilter;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

@SpringBootTest
class ForumApplicationTests {
    @Autowired
    private SensitiveWordFilter wordFilter;

//    @Autowired
//    private PublishRepository publishRepository;
//
    @Autowired
    private ElasticsearchTemplate template;

//    @Resource(name = "RestHighLevelClient")
//    private RestHighLevelClient client;

    @Test
    void contextLoads() {
    }
//    @Test
    void testAdd(){
        Md5Hash hash = new Md5Hash("sdfa", "sdfa" ,1);

        Instant start = Instant.now();
        long reduce = LongStream.rangeClosed(0, 10000000000L)
                .reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());
    }
//    @Test
    void test(){
        List<Long> ids = new ArrayList<>();
        ids.add(4l);
        ids.add(41l);
        ids.add(42l);
        ids.addAll(Arrays.asList(1l,2l,3l));
        String s = Arrays.toString(ids.toArray());
        s = s.replace(" ","");
        System.out.println(ids);
        System.out.println(s);
    }

    @Test
    public void testSensitive(){
        System.out.println("========词库加载完成，即将开始比对");
        String text = "```java";
        System.out.println(System.currentTimeMillis());
        Set<String> set = wordFilter.getSensitiveWord(text);
        System.out.println(System.currentTimeMillis());
        System.out.println("============");
        System.out.println(set.size());
        System.out.println(set);
    }

    @Test
    public void testEs(){
        template.createIndex(Publish.class);
//        template.putMapping(Publish.class);//对已建但未设置mapping信息的索引创建mapping
//        System.out.println(client);
    }

}
