package aws.retrospective.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;t st
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class RedisCrudTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    final String KEY = "MY_KEY";
    final String VALUE = "MY_VALUE";

    @Test
    @DisplayName("키 저장")
    void saveKey() {
        redisTemplate.opsForValue().set(KEY, VALUE);
        String findValue = redisTemplate.opsForValue().get(KEY);
        assertThat(findValue).isEqualTo(VALUE);
    }

    @Test
    @DisplayName("키 삭제")
    void deleteKey() {
        redisTemplate.delete(KEY);
        String findValue = redisTemplate.opsForValue().get(KEY);
        assertThat(findValue).isNull();
    }

    @Test
    @DisplayName("value 업데이트")
    void updateTest() {
        redisTemplate.opsForValue().set(KEY, VALUE);
        String prevValue = redisTemplate.opsForValue().get(KEY);
        assertThat(prevValue).isEqualTo(VALUE);
        redisTemplate.opsForValue().set(KEY, "NEW_VALUE");
        String newValue = redisTemplate.opsForValue().get(KEY);
        assertThat(newValue).isEqualTo("NEW_VALUE");
    }

}
