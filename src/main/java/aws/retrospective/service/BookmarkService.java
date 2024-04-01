package aws.retrospective.service;

import aws.retrospective.entity.Bookmark;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.User;
import aws.retrospective.repository.BookmarkRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final RetrospectiveRepository retrospectiveRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository,
        RetrospectiveRepository retrospectiveRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.retrospectiveRepository = retrospectiveRepository;
    }

    @Transactional
    public boolean toggleBookmark(Long userId, Long retrospectiveId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Not found user: " + userId));
        Retrospective retrospective = retrospectiveRepository.findById(retrospectiveId)
            .orElseThrow(
                () -> new EntityNotFoundException("Not found retrospective: " + retrospectiveId));

        Bookmark bookmark = bookmarkRepository.findByUserIdAndRetrospectiveId(userId,
            retrospectiveId);

        if (bookmark != null) {
            bookmark.removeBookmark();
            bookmarkRepository.delete(bookmark);
            return false;
        } else {
            bookmark = new Bookmark(user, retrospective);
            bookmark.addBookmark();
            bookmarkRepository.save(bookmark);
            return true;
        }
    }

}