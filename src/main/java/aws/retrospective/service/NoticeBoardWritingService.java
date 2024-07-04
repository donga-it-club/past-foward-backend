package aws.retrospective.service;

import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.entity.NoticeBoardWriting;
import aws.retrospective.entity.SaveStatus;
import aws.retrospective.repository.NoticeBoardWritingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

