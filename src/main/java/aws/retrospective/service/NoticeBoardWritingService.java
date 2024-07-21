package aws.retrospective.service;

import aws.retrospective.dto.NoticeBoardListDto;
import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.dto.PagedResponseDto;
import aws.retrospective.entity.NoticeBoardViewCounting;
import aws.retrospective.entity.NoticeBoardWriting;
import aws.retrospective.entity.SaveStatus;
import aws.retrospective.repository.NoticeBoardViewCountingRepository;
import aws.retrospective.repository.NoticeBoardWritingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeBoardWritingService {

    private final NoticeBoardWritingRepository noticeBoardWritingRepository;
    private final NoticeBoardViewCountingRepository noticeBoardViewCountingRepository;

    public NoticeBoardWritingResponseDto savePost(NoticeBoardWritingRequestDto requestDto) {
        SaveStatus status = (requestDto.getStatus() != null) ? requestDto.getStatus() : SaveStatus.PUBLISHED;

        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status(status)
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);

        // Redis에 초기 조회수 설정
        NoticeBoardViewCounting viewCountRedis = NoticeBoardViewCounting.of(savedNoticeBoardWriting.getId(), 0);
        noticeBoardViewCountingRepository.save(viewCountRedis);

        return new NoticeBoardWritingResponseDto(savedNoticeBoardWriting, 0);
    }

    public NoticeBoardWritingResponseDto saveTempPost(NoticeBoardWritingRequestDto requestDto) {
        SaveStatus status = (requestDto.getStatus() != null) ? requestDto.getStatus() : SaveStatus.TEMP;

        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status(status)
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);
        return new NoticeBoardWritingResponseDto(savedNoticeBoardWriting, 0);
    }

    public void deletePost(Long id) {
        noticeBoardWritingRepository.deleteById(id);
        noticeBoardViewCountingRepository.deleteById(id); // Redis에서 조회수 삭제
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<NoticeBoardListDto> getAllPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<NoticeBoardWriting> postsPage = noticeBoardWritingRepository.findAll(pageRequest);
        List<NoticeBoardListDto> posts = postsPage.stream()
                .map(post -> {
                    Long postId = post.getId();
                    if (postId == null) {
                        postId = 0L;
                    }
                    Integer viewCount = noticeBoardViewCountingRepository.findById(postId)
                            .map(NoticeBoardViewCounting::getViewCount)
                            .orElse(0);
                    return new NoticeBoardListDto(post, viewCount);
                })
                .collect(Collectors.toList());
        return new PagedResponseDto<>(posts, postsPage.getTotalPages());
    }

    @Transactional
    public NoticeBoardWritingResponseDto getPostById(Long id) {
        NoticeBoardWriting post = noticeBoardWritingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // Redis에서 조회수 증가
        Optional<NoticeBoardViewCounting> optionalViewCount = noticeBoardViewCountingRepository.findById(id);
        int currentViewCount;
        if (optionalViewCount.isPresent()) {
            NoticeBoardViewCounting viewCountRedis = optionalViewCount.get();
            viewCountRedis.incrementViewCount();
            noticeBoardViewCountingRepository.save(viewCountRedis);
            currentViewCount = viewCountRedis.getViewCount();
        } else {
            // Redis에 조회수 정보가 없는 경우 새로 생성
            NoticeBoardViewCounting viewCountRedis = NoticeBoardViewCounting.of(id, 1);
            noticeBoardViewCountingRepository.save(viewCountRedis);
            currentViewCount = 1;
        }

        return new NoticeBoardWritingResponseDto(post, currentViewCount);
    }

    @Transactional
    public NoticeBoardWritingResponseDto updatePost(Long id, NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWriting post = noticeBoardWritingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        post.updateBoard(requestDto.getTitle(), requestDto.getContent());

        int currentViewCount = noticeBoardViewCountingRepository.findById(id)
                .map(NoticeBoardViewCounting::getViewCount)
                .orElse(0);
        return new NoticeBoardWritingResponseDto(post, currentViewCount);
    }
}
