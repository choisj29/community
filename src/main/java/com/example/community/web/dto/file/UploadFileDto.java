package com.example.community.web.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadFileDto { //업로드 파일 정보 보관
    private String uploadFileName;
    private String storeFileName; //uuid 등으로 겹치지 않게 //여러 사용자가 같은 fileName을 업로도해도 덮어쓰기 방지

    public UploadFileDto(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
