package org.example.plzdrawing.common.config.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AwsCredentialsProvider customAwsCredentialsProvider() {
        return () -> new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return accessKey;
            }
            @Override
            public String secretAccessKey() {
                return secretKey;
            }
        };
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(customAwsCredentialsProvider())
                .region(Region.of(region))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .s3Client(s3Client())
                .build();
    }
}

