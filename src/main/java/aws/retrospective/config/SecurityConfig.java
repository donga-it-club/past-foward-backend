package aws.retrospective.config;

import aws.retrospective.common.CustomAuthenticationConverter;
import aws.retrospective.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    private final UserRepository userRepository;

    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests(
            (authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/v3/**",
                    "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/api/**", "/swaaagger-ui/**")
                .permitAll().anyRequest().authenticated()

        ).csrf((csrf) -> csrf.disable()).oauth2ResourceServer(oauth2 -> oauth2.jwt(
            jwt -> jwt.decoder(JwtDecoders.fromOidcIssuerLocation(issuerUri))
                .jwtAuthenticationConverter(customAuthenticationConverter())));
        return http.build();
    }

    @Bean
    public CustomAuthenticationConverter customAuthenticationConverter() {
        return new CustomAuthenticationConverter(userRepository);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
