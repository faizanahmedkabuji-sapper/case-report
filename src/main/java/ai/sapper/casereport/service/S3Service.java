package ai.sapper.casereport.service;

import ai.sapper.casereport.config.S3Config;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.nio.file.Paths;

@Log4j2
@Component
@DependsOn("s3Config")
@RequiredArgsConstructor
public class S3Service {
    private final S3Config s3Config;
    private S3Client s3Client;

    @PostConstruct
    public void init() {
        s3Client = S3Client.builder()
                .region(Region.of(s3Config.getRegion()))
                .build();
    }

    public void uploadFile(File file) {
        try {
            String key = Paths.get(s3Config.getLocation(), file.getName()).toString();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Config.getBucket())
                    .key(key)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, file.toPath());
            log.info("File uploaded successfully. ETag: {}", response.eTag());
        } catch (Exception e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
        }
    }
}
