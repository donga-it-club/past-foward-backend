package aws.retrospective.service;

import aws.retrospective.dto.GetPreSignedUrlRequestDto;
import aws.retrospective.dto.GetPreSignedUrlResponseDto;
import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.dto.PresigendUrlMethod;
import aws.retrospective.entity.NoticeBoardWriting;
import aws.retrospective.entity.SaveStatus;
import aws.retrospective.repository.NoticeBoardWritingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        when(noticeBoardWritingRepository.save(any(NoticeBoardWriting.class))).thenReturn(noticeBoardWriting);

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
}

