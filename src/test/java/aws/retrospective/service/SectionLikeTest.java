package aws.retrospective.service;

import static org.assertj.core.api.Assertions.*;

import aws.retrospective.entity.Section;
import aws.retrospective.entity.User;
import aws.retrospective.repository.SectionRepository;
import aws.retrospective.repository.UserRepository;
import aws.retrospective.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SectionLikeTest {

    @Autowired
    SectionService sectionService;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void beforeEach() {
        redisTemplate.delete(getSectionLikeKey(1L));
    }

    @Test
    @DisplayName("좋아요를 처음으로 누른다면 좋아요가 추가되어야 한다.")
    void increaseLikeTest() {
        // given
        User user = TestUtil.createUser();
        userRepository.save(user);
        Section section = TestUtil.createSection(user);
        sectionRepository.save(section);

        // when
        // clickLikeSection()을 호출하는 시점에는 Redis에 좋아요 기록이 존재하지 않는다.
        sectionService.clickLikeSection(section.getId(), user);

        // then
        // clickLikeSection()을 호출한 후에는 Redis에 좋아요 기록이 존재해야 한다.
        Boolean isClicked = redisTemplate.opsForSet().isMember(getSectionLikeKey(section.getId()), user.getId().toString());
        assertThat(isClicked).isTrue();
    }

    @Test
    @DisplayName("좋아요를 누른 상태에서 다시 누른다면 좋아요가 취소된다.")
    void cancelLikeTest() {
        // given
        User user = TestUtil.createUser();
        userRepository.save(user);
        Section section = TestUtil.createSection(user);
        sectionRepository.save(section);

        // when
        // 좋아요를 누른 상태로 만든다.
        sectionService.clickLikeSection(section.getId(), user);
        // 좋아요를 다시 누른다.
        sectionService.clickLikeSection(section.getId(), user);

        // then
        Boolean isClicked = redisTemplate.opsForSet()
            .isMember(getSectionLikeKey(section.getId()), user.getId().toString());
        assertThat(isClicked).isFalse();
    }


    private String getSectionLikeKey(Long sectionId) {
        return "section:" + sectionId + ":like";
    }

}
