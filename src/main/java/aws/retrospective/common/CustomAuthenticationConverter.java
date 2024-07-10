package aws.retrospective.common;

import aws.retrospective.entity.User;
import aws.retrospective.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        String tenantId = jwt.getClaimAsString("sub");
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("nickname");

        User user = getOrInsertUser(tenantId, email, username);

        // 사용자가 관리자이면 ROLE_ADMIN 권한을 부여합니다.
        List<GrantedAuthority> authorities = user.isAdministrator() ?
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) :
            Collections.emptyList();

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }


    @Transactional
    public User getOrInsertUser(String tenantId, String email, String username) {
        return userRepository.findByTenantId(tenantId)
            .orElseGet(() -> insertUser(tenantId, email, username));

    }

    private User insertUser(String tenantId, String email, String username) {
        User user = User.builder()
            .tenantId(tenantId)
            .email(email)
            .username(username)
            .build();

        return userRepository.save(user);
    }
}

