package com.ssafy.nagne.service;

import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.getFilenameExtension;

import com.ssafy.nagne.error.FIleStoreException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    private String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<String> store(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            return List.of();
        }

        if (multipartFiles.get(0).isEmpty()) {
            return List.of();
        }

        return multipartFiles.stream()
                .map(this::store)
                .map(this::getFullPath)
                .toList();
    }

    public String store(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        try {
            String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());
            multipartFile.transferTo(new File(getFullPath(storeFileName)));

            return getFullPath(storeFileName);
        } catch (IOException e) {
            throw new FIleStoreException("파일 저장 중 예외가 발생했습니다.");
        }
    }

    private String createStoreFileName(String originalFileName) {
        return randomUUID() + "." + getFilenameExtension(originalFileName);
    }
}
