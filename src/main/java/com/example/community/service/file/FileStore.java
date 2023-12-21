package com.example.community.service.file;

import com.example.community.web.dto.file.UploadFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStore {

    @Value("${property.file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFileDto> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        log.info("storeFiles {}", multipartFiles.get(0).getOriginalFilename());
        List<UploadFileDto> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            log.info("multipartFile {}", multipartFile);
            log.info("multipartFile isEmpty{}", multipartFile.isEmpty());
            if (!multipartFiles.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return  storeFileResult;
    }

    public UploadFileDto storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFileName);
        String fullPath = getFullPath(storeFilename);
        log.info("fullPath {}", fullPath);
        multipartFile.transferTo(new File(fullPath));
        return new UploadFileDto(originalFileName, storeFilename);
    }

    private String createStoreFilename(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }
}
