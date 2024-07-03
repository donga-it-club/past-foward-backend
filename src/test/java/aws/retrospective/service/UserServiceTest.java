package aws.retrospective.service;

import aws.retrospective.entity.User;
import aws.retrospective.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자가 이메일 수신 동의 상태를 업데이트 할 수 있다.")
    void updateEmailConsent() {
        // Given
        Long userId = 1L;
        User user = new User("user1", "test", "test", "test", true);
        boolean consent = user.isEmailConsent();


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.updateEmailConsent(userId, consent);

        // Then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();

        assertThat(savedUser.isEmailConsent()).isEqualTo(consent);
    }
}
