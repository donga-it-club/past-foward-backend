package aws.retrospective.service;

import aws.retrospective.dto.GetPreSignedUrlRequestDto;
import aws.retrospective.dto.GetPreSignedUrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Service s3Service;

    @Transactional
    public GetPreSignedUrlResponseDto getPreSignedUrl(GetPreSignedUrlRequestDto request) {
        String preSignedUrl = s3Service.getPresignUrl(request.getFilename());
        return new GetPreSignedUrlResponseDto(request.getFilename(), preSignedUrl);
    }
}