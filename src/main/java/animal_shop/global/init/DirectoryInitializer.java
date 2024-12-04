package animal_shop.global.init;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class DirectoryInitializer {

    @Value("${file.upload-dir-comment}")  // 댓글 파일 업로드 경로
    private String commentUploadDir;

    @Value("${file.upload-dir-post}")  // 게시글 파일 업로드 경로
    private String postUploadDir;

    @Value("${file.upload-dir-profile}")  // 게시글 파일 업로드 경로
    private String profileUploadDir;

    @Value("${file.upload-dir-item}")  // 게시글 파일 업로드 경로
    private String itemUploadDir;

    @Value("${file.upload-dir-item-comment}")  // 게시글 파일 업로드 경로
    private String itemCommentUploadDir;

    @Value("${file.upload-dir-pet}")  // 파일 저장 경로를 application.properties에 설정
    private String petUploadDir;

    @Value("${file.upload-dir-notices}")
    private String noticesUploadDir;

    @PostConstruct
    public void init() {
        createDirectoryIfNotExists(commentUploadDir);
        createDirectoryIfNotExists(postUploadDir);
        createDirectoryIfNotExists(profileUploadDir);
        createDirectoryIfNotExists(itemUploadDir);
        createDirectoryIfNotExists(itemCommentUploadDir);
        createDirectoryIfNotExists(petUploadDir);
        createDirectoryIfNotExists(noticesUploadDir);
    }

    private void createDirectoryIfNotExists(String dirPath) {
        Path path = Paths.get(dirPath);

        if (!Files.exists(path)) {  // 디렉토리 존재 여부 확인
            try {
                Files.createDirectories(path);  // 디렉토리 생성
                log.info("Directory created: " + dirPath);
            } catch (IOException e) {
                log.info("Failed to create directory: " + dirPath);
                e.printStackTrace();
            }
        } else {
            log.info("Directory already exists: " + dirPath);
        }
    }
}

