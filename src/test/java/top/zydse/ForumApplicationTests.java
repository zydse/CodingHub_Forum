package top.zydse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

@SpringBootTest
class ForumApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testAdd(){
        Instant start = Instant.now();
        long reduce = LongStream.rangeClosed(0, 10000000000L)
                .reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());
    }

}
