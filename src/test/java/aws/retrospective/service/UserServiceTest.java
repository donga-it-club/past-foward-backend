package aws.retrospective.service;

import aws.retrospective.dto.AdminRoleDtO;
import aws.retrospective.entity.User;
import aws.retrospective.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .phone("123-456-7890")
                .tenantId("tenant1")
                .isAdministrator(false)
                .build();
    }

    @Test
    @Transactional
    public void testUpdateAdminStatus() {
        AdminRoleDtO adminRoleDTO = new AdminRoleDtO("test@example.com", true);

        // 테스트 대상 메서드 호출
        userService.updateAdminStatus(user, adminRoleDTO);

        // 결과 검증
        assertTrue(user.isAdministrator());
    }

    @Test
    @Transactional
    public void testIsAdmin() {
        // userRepository에서 이메일로 사용자 검색 시 user 반환
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // 테스트 대상 메서드 호출 및 결과 검증
        boolean isAdmin = userService.isAdmin(user.getEmail());
        assertFalse(isAdmin);

        // 관리자 권한 업데이트 및 결과 검증
        AdminRoleDtO adminRoleDTO = new AdminRoleDtO("test@example.com", true);
        userService.updateAdminStatus(user, adminRoleDTO);

        // 업데이트 후 영속성 컨텍스트가 변경된 상태를 반환하도록 설정
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        isAdmin = userService.isAdmin(user.getEmail());
        assertTrue(isAdmin);
    }
}
