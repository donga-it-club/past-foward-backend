package aws.retrospective.service;

import aws.retrospective.dto.GetPreSignedUrlRequestDto;
import aws.retrospective.dto.GetPreSignedUrlResponseDto;
import aws.retrospective.dto.PresigendUrlMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Service s3Service;

    public GetPreSignedUrlResponseDto getPreSignedUrl(GetPreSignedUrlRequestDto request) {
        PresigendUrlMethod method = request.getMethod();
        String filename = request.getFilename();

        if (method == PresigendUrlMethod.GET) {
            String url = s3Service.getPresignedUrl(filename);
            return new GetPreSignedUrlResponseDto(filename, url);
        } else if (method == PresigendUrlMethod.PUT) {
            String url = s3Service.getPresignedUrlForUpload(filename);
            return new GetPreSignedUrlResponseDto(filename, url);
        }

        throw new IllegalArgumentException("Unsupported method type: " + method);
    }
}