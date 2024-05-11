package aws.retrospective.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Slf4j
@Configuration
@Profile("local")
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        try {
            redisServer = new RedisServer(port);
            redisServer.start();
            log.info("Embedded Redis Server Start");
        } catch (Exception ex) {
        }
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
        log.info("Embedded Redis Server Stop");
    }
}
