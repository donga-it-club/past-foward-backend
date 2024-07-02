package aws.retrospective.service;

import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.entity.NoticeBoardWriting;
import aws.retrospective.entity.SaveStatus;
import aws.retrospective.repository.NoticeBoardWritingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeBoardWritingService {

    private final NoticeBoardWritingRepository noticeBoardWritingRepository;
    private final String uploadDir = "uploads/";

    @Transactional
    public NoticeBoardWritingResponseDto savePost(NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status(SaveStatus.PUBLISHED) // 변경된 부분
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);
        return new NoticeBoardWritingResponseDto(savedNoticeBoardWriting);
    }

    @Transactional
    public NoticeBoardWritingResponseDto saveTempPost(NoticeBoardWritingRequestDto requestDto) {
        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status(SaveStatus.TEMP) // 변경된 부분
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);
        return new NoticeBoardWritingResponseDto(savedNoticeBoardWriting);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Empty file");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath);

        return filePath.toString();
    }

    @Transactional
    public void deletePost(Long id) {
        noticeBoardWritingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<NoticeBoardWritingResponseDto> getAllPosts() {
        List<NoticeBoardWriting> posts = noticeBoardWritingRepository.findAll();
        return posts.stream()
                .map(NoticeBoardWritingResponseDto::new) // 변경된 부분
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<NoticeBoardWritingResponseDto> getPostById(Long id) {
        Optional<NoticeBoardWriting> post = noticeBoardWritingRepository.findById(id);
        return post.map(NoticeBoardWritingResponseDto::new); // 변경된 부분
    }
}

