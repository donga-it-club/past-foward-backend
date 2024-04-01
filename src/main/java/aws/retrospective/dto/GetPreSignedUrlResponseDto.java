package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPreSignedUrlResponseDto {

    @Schema(description = "PreSigned URL", example = "https://test-bucket-past-foward.s3.ap-northeast-2.amazonaws.com/test.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240401T151853Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=AKIAXYKJWPIGH3S5RHFR%2F20240401%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=52e8fe1fe625ba40d3fa3c30d59f3674bc100bea19f8b94b70f74b93e80733ef")
    private String preSignedUrl;
}
