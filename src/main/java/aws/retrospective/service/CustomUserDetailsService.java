package aws.retrospective.service;

import aws.retrospective.common.CustomUserDetails;
import aws.retrospective.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String tenantId) throws UsernameNotFoundException {
        return new CustomUserDetails(userRepository.findByTenantId(tenantId).orElseThrow(() ->
            new UsernameNotFoundException("사용자가 존재하지 않습니다.")));
    }

}
