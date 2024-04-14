package aws.retrospective;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RetrospectiveApplicationTests.class)
@ActiveProfiles("local")
class RetrospectiveApplicationTests {

    @Test
    void contextLoads() {
    }

}
