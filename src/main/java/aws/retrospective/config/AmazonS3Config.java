package aws.retrospective.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AmazonS3Config {

    @Value("${AWS_REGION}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .credentialsProvider(InstanceProfileCredentialsProvider.create())
            .region(Region.of(region))
            .build();
    }

    @Bean
    public S3Presigner presigner() {
        return S3Presigner.builder()
            .credentialsProvider(InstanceProfileCredentialsProvider.create())
            .region(Region.of(region))
            .build();
    }
}
