package aws.retrospective.service;

import aws.retrospective.dto.AdminRoleDTO;
import aws.retrospective.dto.GetUserInfoDto;
import aws.retrospective.dto.UpdateUserProfileRequestDto;
import aws.retrospective.dto.UpdateUserProfileResponseDto;
import aws.retrospective.entity.User;
import aws.retrospective.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .phone("123-456-7890")
                .tenantId("tenant1")
                .administrator(false)
                .build();
    }

    @Test
    public void testUpdateProfile() {
        UpdateUserProfileRequestDto request = new UpdateUserProfileRequestDto("newThumbnail", "newUsername");

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        UpdateUserProfileResponseDto response = userService.updateProfile(user, request);

        assertNotNull(response);
        assertEquals("newUsername", response.getUsername());
        assertEquals("newThumbnail", response.getThumbnail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUserInfo() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        GetUserInfoDto response = userService.getUserInfo(user);

        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getUsername(), response.getUserName());
        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    public void testUpdateAdminStatus() {
        AdminRoleDTO adminRoleDTO = new AdminRoleDTO("test@example.com", true);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        userService.updateAdminStatus(user, adminRoleDTO);
        assertTrue(user.isAdministrator());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testIsAdmin() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        boolean isAdmin = userService.isAdmin(user.getEmail());
        assertFalse(isAdmin);

        // 관리자 권한을 업데이트하는 메서드를 사용
        user.updateAdministrator(true);
        isAdmin = userService.isAdmin(user.getEmail());
        assertTrue(isAdmin);
    }
}

