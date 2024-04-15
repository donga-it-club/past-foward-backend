package aws.retrospective.config;

// Java

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String domainUrl;

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl(domainUrl);

        return new OpenAPI()
            .components(new Components())
            .info(apiInfo())
            .servers(List.of(server));
    }

    private Info apiInfo() {

        return new Info()
            .title("PastForward API")
            .description("PastForward API")
            .version("1.0.0");
    }
}

