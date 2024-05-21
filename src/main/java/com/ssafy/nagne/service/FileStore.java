package com.ssafy.nagne.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.getFilenameExtension;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.nagne.error.FIleStoreException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileStore {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> store(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            return List.of();
        }

        if (multipartFiles.get(0).isEmpty()) {
            return List.of();
        }

        return multipartFiles.stream()
                .map(this::store)
                .toList();
    }

    public String store(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        try {
            String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());

            amazonS3.putObject(
                    bucket,
                    storeFileName,
                    multipartFile.getInputStream(),
                    objectMetadata(multipartFile)
            );

            return URLDecoder.decode(amazonS3.getUrl(bucket, storeFileName).toString(), UTF_8);
        } catch (IOException e) {
            throw new FIleStoreException("파일 저장 중 예외가 발생했습니다.");
        }
    }

    private String createStoreFileName(String originalFileName) {
        return randomUUID() + "." + getFilenameExtension(originalFileName);
    }

    private ObjectMetadata objectMetadata(MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        return objectMetadata;
    }
}
