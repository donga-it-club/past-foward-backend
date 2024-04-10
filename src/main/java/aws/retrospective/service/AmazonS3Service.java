package aws.retrospective.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonS3Service {

    private final S3Presigner presigner;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    public String getPresignUrl(String filename) {
        GetObjectRequest getObjectAclRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(5)) // 제한 시간 5분
            .getObjectRequest(getObjectAclRequest)
            .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner
            .presignGetObject(getObjectPresignRequest);

        String url = presignedGetObjectRequest.url().toString();

        presigner.close();
        return url;
    }
}
