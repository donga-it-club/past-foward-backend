package aws.retrospective.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aws.retrospective.entity.Bookmark;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.User;
import aws.retrospective.repository.BookmarkRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.util.TestUtil;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class BookmarkServiceTest {

    @Mock
    private RetrospectiveRepository retrospectiveRepository;


    @Mock
    private UserRepository userRepository;


    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;


    @Test
    void toggleBookmark_ReturnsTrue_WhenBookmarkDoesNotExist() {
        // Arrange
        User user = TestUtil.createUser();
        Retrospective retrospective = TestUtil.createRetrospective(
            TestUtil.createTemplate(), user, TestUtil.createTeam());

        ReflectionTestUtils.setField(retrospective, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 1L);

        when(retrospectiveRepository.findById(retrospective.getId())).thenReturn(
            Optional.of(retrospective));

        when(bookmarkRepository.findByUserIdAndRetrospectiveId(user.getId(),
            retrospective.getId())).thenReturn(
            Optional.empty());
        when(bookmarkRepository.save(any())).thenReturn(null);

        // Act
        boolean result = bookmarkService.toggleBookmark(user, retrospective.getId());

        // Assert
        assertTrue(result);
        verify(bookmarkRepository).save(any());
    }

    @Test
    void toggleBookmark_ReturnsFalse_WhenBookmarkExists() {
        // Arrange
        User user = TestUtil.createUser();
        Retrospective retrospective = TestUtil.createRetrospective(
            TestUtil.createTemplate(), user, TestUtil.createTeam());
        Bookmark bookmark = TestUtil.createBookmark(user, retrospective);

        ReflectionTestUtils.setField(retrospective, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 1L);

        when(retrospectiveRepository.findById(retrospective.getId())).thenReturn(
            Optional.of(retrospective));
        when(bookmarkRepository.findByUserIdAndRetrospectiveId(user.getId(),
            retrospective.getId())).thenReturn(Optional.of(bookmark));

        // Act
        boolean result = bookmarkService.toggleBookmark(user, retrospective.getId());

        // Assert
        assertThat(result).isFalse();
        verify(bookmarkRepository).delete(bookmark);
    }

}
