package aws.retrospective.service;

import aws.retrospective.entity.Bookmark;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.User;
import aws.retrospective.repository.BookmarkRepository;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final RetrospectiveRepository retrospectiveRepository;


    @Transactional
    public boolean toggleBookmark(Long userId, Long retrospectiveId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Not found user: " + userId));
        Retrospective retrospective = retrospectiveRepository.findById(retrospectiveId)
            .orElseThrow(
                () -> new EntityNotFoundException("Not found retrospective: " + retrospectiveId));

        Optional<Bookmark> bookmark = bookmarkRepository.findByUserIdAndRetrospectiveId(userId,
            retrospectiveId);

        if (bookmark.isEmpty()) {
            Bookmark createdBookmark = Bookmark.builder()
                .user(user)
                .retrospective(retrospective)
                .build();
            bookmarkRepository.save(createdBookmark);
            return true;
        } else {
            bookmarkRepository.delete(bookmark.get());
            return false;
        }
    }

}