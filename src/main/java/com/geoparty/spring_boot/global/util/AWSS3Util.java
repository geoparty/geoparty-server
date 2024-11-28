package com.geoparty.spring_boot.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AWSS3Util {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return s3Client.utilities().getUrl(builder ->
                builder.bucket(bucket).key(fileName)
        ).toString();
    }

    private String createFileName(String originalFileName) {
        String encodedFileName = Base64.getUrlEncoder().encodeToString(originalFileName.getBytes());
        return UUID.randomUUID().toString().substring(0,3) + "-" + encodedFileName;
    }

}
