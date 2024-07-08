package aws.retrospective.service;

import aws.retrospective.dto.NoticeBoardListDto;
import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.dto.PagedResponseDto;
import aws.retrospective.entity.NoticeBoardWriting;
import aws.retrospective.entity.SaveStatus;
import aws.retrospective.repository.NoticeBoardWritingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeBoardWritingService {

    private final NoticeBoardWritingRepository noticeBoardWritingRepository;

    public NoticeBoardWritingResponseDto savePost(NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status(SaveStatus.PUBLISHED)
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);
        return new NoticeBoardWritingResponseDto(savedNoticeBoardWriting);
    }

    public NoticeBoardWritingResponseDto saveTempPost(NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status(SaveStatus.TEMP)
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);
        return new NoticeBoardWritingResponseDto(savedNoticeBoardWriting);
    }

    public void deletePost(Long id) {
        noticeBoardWritingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<NoticeBoardListDto> getAllPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<NoticeBoardWriting> postsPage = noticeBoardWritingRepository.findAll(pageRequest);
        List<NoticeBoardListDto> posts = postsPage.stream()
                .map(NoticeBoardListDto::new)
                .collect(Collectors.toList());
        return new PagedResponseDto<>(posts, postsPage.getTotalPages());
    }

    @Transactional
    public NoticeBoardWritingResponseDto getPostById(Long id) {
        NoticeBoardWriting post = noticeBoardWritingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        post.incrementViews();
        noticeBoardWritingRepository.save(post); // 조회수 증가한 내용 저장
        return new NoticeBoardWritingResponseDto(post);
    }

    @Transactional
    public NoticeBoardWritingResponseDto updatePost(Long id, NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWriting post = noticeBoardWritingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        post.updateBoard(requestDto.getTitle(), requestDto.getContent());

        NoticeBoardWriting updatedPost = noticeBoardWritingRepository.save(post);
        return new NoticeBoardWritingResponseDto(updatedPost);
    }
}

