package aws.retrospective.service;

import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.entity.NoticeBoardWriting;
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
    private final String uploadDir = "uploads/";  // 파일을 저장할 디렉토리 경로

    @Transactional
    public NoticeBoardWritingResponseDto savePost(NoticeBoardWritingRequestDto requestDto) { // 새로운 게시글을 데이터베이스에 저장하는 메서드
        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status("PUBLISHED")
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);

        return new NoticeBoardWritingResponseDto(
                savedNoticeBoardWriting.getId(),
                savedNoticeBoardWriting.getTitle(),
                savedNoticeBoardWriting.getContent(),
                savedNoticeBoardWriting.getStatus(),
                savedNoticeBoardWriting.getCreatedDate(),
                savedNoticeBoardWriting.getModifiedDate()
        );
    }

    @Transactional
    public NoticeBoardWritingResponseDto saveTempPost(NoticeBoardWritingRequestDto requestDto) { // 임시 게시글을 데이터베이스에 저장하는 메서드
        NoticeBoardWriting noticeBoardWriting = NoticeBoardWriting.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status("TEMP")
                .build();
        NoticeBoardWriting savedNoticeBoardWriting = noticeBoardWritingRepository.save(noticeBoardWriting);
        return new NoticeBoardWritingResponseDto(
                savedNoticeBoardWriting.getId(),
                savedNoticeBoardWriting.getTitle(),
                savedNoticeBoardWriting.getContent(),
                savedNoticeBoardWriting.getStatus(),
                savedNoticeBoardWriting.getCreatedDate(),
                savedNoticeBoardWriting.getModifiedDate()
        );
    }

    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {  // 파일을 서버에 업로드하는 메서드
        if (file.isEmpty()) {
            throw new IOException("Empty file");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        } // 업로드 디렉토리가 없을 경우 생성

        String fileName = file.getOriginalFilename(); // 파일 이름 얻기
        Path filePath = uploadPath.resolve(fileName); // 파일 경로 설정
        file.transferTo(filePath); // 파일을 설정된 경로에 저장

        return filePath.toString();
    }

    @Transactional
    public void deletePost(Long id) {
        noticeBoardWritingRepository.deleteById(id); // 게시글을 삭제하는 메서드
    }

    @Transactional(readOnly = true)
    public List<NoticeBoardWritingResponseDto> getAllPosts() { // 모든 게시글을 조회하는 메서드
        List<NoticeBoardWriting> posts = noticeBoardWritingRepository.findAll();
        return posts.stream()
                .map(post -> new NoticeBoardWritingResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getStatus(),
                        post.getCreatedDate(),
                        post.getModifiedDate()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<NoticeBoardWritingResponseDto> getPostById(Long id) { // 특정 ID를 가진 게시글을 조회하는 메서드
        Optional<NoticeBoardWriting> post = noticeBoardWritingRepository.findById(id);
        return post.map(p -> new NoticeBoardWritingResponseDto(
                p.getId(),
                p.getTitle(),
                p.getContent(),
                p.getStatus(),
                p.getCreatedDate(),
                p.getModifiedDate()
        ));
    }
}
