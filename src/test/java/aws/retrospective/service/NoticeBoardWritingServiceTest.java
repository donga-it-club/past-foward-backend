package aws.retrospective.service;

import aws.retrospective.dto.NoticeBoardWritingRequestDto;
import aws.retrospective.dto.NoticeBoardWritingResponseDto;
import aws.retrospective.entity.NoticeBoardWriting;
import aws.retrospective.repository.NoticeBoardWritingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

    private NoticeBoardWriting noticeBoardWriting;

    @BeforeEach
    public void setUp() {
        noticeBoardWriting = NoticeBoardWriting.builder()
                .title("Test Title")
                .content("Test Content")
                .status("PUBLISHED")
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

        noticeBoardWriting.updateStatus("TEMP");
        when(noticeBoardWritingRepository.save(any(NoticeBoardWriting.class))).thenReturn(noticeBoardWriting);

        NoticeBoardWritingResponseDto responseDto = noticeBoardWritingService.saveTempPost(requestDto);

        assertNotNull(responseDto);
        assertEquals("Test Title", responseDto.getTitle());
        assertEquals("Test Content", responseDto.getContent());
        assertEquals("TEMP", responseDto.getStatus());
        verify(noticeBoardWritingRepository, times(1)).save(any(NoticeBoardWriting.class));
    }

    @Test
    public void testUploadFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.txt");

        Path uploadPath = Paths.get("uploads/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileUrl = noticeBoardWritingService.uploadFile(file);

        assertTrue(fileUrl.endsWith("test.txt"));
        verify(file, times(1)).transferTo(any(Path.class));
    }

    @Test
    public void testDeletePost() {
        doNothing().when(noticeBoardWritingRepository).deleteById(any(Long.class));

        noticeBoardWritingService.deletePost(1L);

        verify(noticeBoardWritingRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void testGetAllPosts() {
        when(noticeBoardWritingRepository.findAll()).thenReturn(List.of(noticeBoardWriting));

        List<NoticeBoardWritingResponseDto> responseDtos = noticeBoardWritingService.getAllPosts();

        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals("Test Title", responseDtos.get(0).getTitle());
        verify(noticeBoardWritingRepository, times(1)).findAll();
    }

    @Test
    public void testGetPostById() {
        when(noticeBoardWritingRepository.findById(any(Long.class))).thenReturn(Optional.of(noticeBoardWriting));

        Optional<NoticeBoardWritingResponseDto> responseDto = noticeBoardWritingService.getPostById(1L);

        assertTrue(responseDto.isPresent());
        assertEquals("Test Title", responseDto.get().getTitle());
        verify(noticeBoardWritingRepository, times(1)).findById(any(Long.class));
    }
}
