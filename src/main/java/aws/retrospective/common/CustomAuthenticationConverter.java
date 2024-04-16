package aws.retrospective.common;

import aws.retrospective.entity.User;
import aws.retrospective.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

@RequiredArgsConstructor
public class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        String username = jwt.getClaimAsString("cognito:username");

        User user = getOrInsertUser(username, jwt.getClaims());


        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }


    private User getOrInsertUser(String username, Map<String, Object> claims) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return insertUser(username);
        }

        return user;
    }

    private User insertUser(String username) {
        User user =  User.builder()
            .username(username)
            .build();


        return userRepository.save(user);
    }
}
