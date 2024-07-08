package aws.retrospective.service;

import aws.retrospective.dto.*;
import aws.retrospective.entity.NoticeBoardWriting;
import aws.retrospective.entity.SaveStatus;
import aws.retrospective.repository.NoticeBoardWritingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeBoardWritingServiceTest {

    @InjectMocks
    private NoticeBoardWritingService noticeBoardWritingService;

    @Mock
    private NoticeBoardWritingRepository noticeBoardWritingRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private AmazonS3Service amazonS3Service;

    private NoticeBoardWriting noticeBoardWriting;

    @BeforeEach
    public void setUp() {
        noticeBoardWriting = NoticeBoardWriting.builder()
                .title("Test Title")
                .content("Test Content")
                .status(SaveStatus.PUBLISHED)
                .build();
    }

    @Test
    public void testSavePost() {
        NoticeBoardWritingRequestDto requestDto = new NoticeBoardWritingRequestDto("Test Title", "Test Content");

        when(noticeBoardWritingRepository.save(any(NoticeBoardWriting.class))).thenReturn(noticeBoardWriting);

        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.savePost(requestDto);

        assertNotNull(responseDto);
        assertEquals("Test Title", responseDto.getTitle());
        assertEquals("Test Content", responseDto.getContent());
        assertEquals("PUBLISHED", responseDto.getStatus());
        verify(noticeBoardWritingRepository, times(1)).save(any(NoticeBoardWriting.class));
    }

    @Test
    public void testSaveTempPost() {
        NoticeBoardWritingRequestDto requestDto = new NoticeBoardWritingRequestDto("Test Title", "Test Content");

        NoticeBoardWriting tempNoticeBoardWriting = NoticeBoardWriting.builder()
                .title("Test Title")
                .content("Test Content")
                .status(SaveStatus.TEMP)
                .build();

        when(noticeBoardWritingRepository.save(any(NoticeBoardWriting.class))).thenReturn(tempNoticeBoardWriting);

        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.saveTempPost(requestDto);

        assertNotNull(responseDto);
        assertEquals("Test Title", responseDto.getTitle());
        assertEquals("Test Content", responseDto.getContent());
        assertEquals("TEMP", responseDto.getStatus());
        verify(noticeBoardWritingRepository, times(1)).save(any(NoticeBoardWriting.class));
    }

    @Test
    public void testGetPresignedUrlForUpload() {
        String filename = "test.txt";
        String presignedUrl = "https://bucketname.s3.amazonaws.com/test.txt?presigned-url";

        when(amazonS3Service.getPresignedUrlForUpload(filename)).thenReturn(presignedUrl);

        String responseUrl = amazonS3Service.getPresignedUrlForUpload(filename);

        assertNotNull(responseUrl);
        assertEquals(presignedUrl, responseUrl);
        verify(amazonS3Service, times(1)).getPresignedUrlForUpload(filename);
    }

    @Test
    public void testDeletePost() {
        doNothing().when(noticeBoardWritingRepository).deleteById(any(Long.class));

        noticeBoardWritingService.deletePost(1L);

        verify(noticeBoardWritingRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void testGetAllPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<NoticeBoardWriting> page = new PageImpl<>(Collections.singletonList(noticeBoardWriting), pageable, 1);

        when(noticeBoardWritingRepository.findAll(any(Pageable.class))).thenReturn(page);

        PagedResponseDto<NoticeBoardListDto> responseDto = noticeBoardWritingService.getAllPosts(1, 10);

        assertNotNull(responseDto);
        assertEquals(1, responseDto.getPosts().size());
        assertEquals("Test Title", responseDto.getPosts().get(0).getTitle());
        assertEquals(1, responseDto.getTotalPages());
        verify(noticeBoardWritingRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetPostById() {
        when(noticeBoardWritingRepository.findById(any(Long.class))).thenReturn(Optional.of(noticeBoardWriting));

        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.getPostById(1L);

        assertNotNull(responseDto);
        assertEquals("Test Title", responseDto.getTitle());
        assertEquals("Test Content", responseDto.getContent());
        assertEquals("PUBLISHED", responseDto.getStatus());
        verify(noticeBoardWritingRepository, times(1)).findById(any(Long.class));
        verify(noticeBoardWritingRepository, times(1)).save(any(NoticeBoardWriting.class)); // 조회수 증가 저장 확인
    }

    @Test
    public void testUpdatePost() {
        Long postId = 1L;
        NoticeBoardWritingRequestDto requestDto = new NoticeBoardWritingRequestDto("Updated Title", "Updated Content");

        when(noticeBoardWritingRepository.findById(postId)).thenReturn(Optional.of(noticeBoardWriting));
        when(noticeBoardWritingRepository.save(any(NoticeBoardWriting.class))).thenReturn(noticeBoardWriting);

        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.updatePost(postId, requestDto);

        assertNotNull(responseDto);
        assertEquals("Updated Title", responseDto.getTitle());
        assertEquals("Updated Content", responseDto.getContent());
        verify(noticeBoardWritingRepository, times(1)).findById(postId);
    }
}

