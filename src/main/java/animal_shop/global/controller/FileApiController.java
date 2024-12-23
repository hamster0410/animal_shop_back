package animal_shop.global.controller;

import animal_shop.global.dto.FileDownloadDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileApiController {
    @Value("${file.upload-dir-comment}")  // 파일 저장 경로를 application.properties에 설정
    private String commentUploadDir;

    @Value("${file.upload-dir-post}")  // 파일 저장 경로를 application.properties에 설정
    private String postUploadDir;

    @Value("${file.upload-dir-profile}")  // 파일 저장 경로를 application.properties에 설정
    private String profileUploadDir;

    @Value("${file.upload-dir-item}")  // 파일 저장 경로를 application.properties에 설정
    private String itemUploadDir;

    @Value("${file.upload-dir-item-comment}")  // 파일 저장 경로를 application.properties에 설정
    private String itemCommentUploadDir;

    @Value("${file.upload-dir-pet}")  // 파일 저장 경로를 application.properties에 설정
    private String petUploadDir;

    @Value("${file.upload-dir-notices}")
    private String noticesUploadDir;

    @Value("${file.upload-dir-map-comment}")
    private String mapUploadDir;

    @PostMapping("/post-image-upload")
    public String uploadPostImage(@RequestPart("image") final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
        String saveFilename = uuid + "." + extension;                                             // 디스크에 저장할 파일명
        String fileFullPath = Paths.get(postUploadDir, saveFilename).toString();                      // 디스크에 저장할 파일의 전체 경로


        try {
            // 파일 저장 (write to disk)
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return "post_" + saveFilename;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/profile-image-upload")
    public String uploadProfileImage(@RequestPart("image") final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
        String saveFilename = uuid + "." + extension;                                             // 디스크에 저장할 파일명
        String fileFullPath = Paths.get(profileUploadDir, saveFilename).toString();                      // 디스크에 저장할 파일의 전체 경로


        try {
            // 파일 저장 (write to disk)
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return "profile_" +saveFilename;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/item-image-upload")
    public String uploadItemImage(@RequestPart("image") final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
        String saveFilename = uuid + "." + extension;                                             // 디스크에 저장할 파일명
        String fileFullPath = Paths.get(itemUploadDir, saveFilename).toString();                      // 디스크에 저장할 파일의 전체 경로


        try {
            // 파일 저장 (write to disk)
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return "item_" +saveFilename;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/item-comment-image-upload")
    public String uploadItemCommentImage(@RequestPart("image") final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
        String saveFilename = uuid + "." + extension;                                             // 디스크에 저장할 파일명
        String fileFullPath = Paths.get(itemCommentUploadDir, saveFilename).toString();                      // 디스크에 저장할 파일의 전체 경로


        try {
            // 파일 저장 (write to disk)
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return "item-comment_" +saveFilename;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/pet-image-upload")
    public String uploadPetImage(@RequestPart("image") final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
        String saveFilename = uuid + "." + extension;                                             // 디스크에 저장할 파일명
        String fileFullPath = Paths.get(petUploadDir, saveFilename).toString();                      // 디스크에 저장할 파일의 전체 경로


        try {
            // 파일 저장 (write to disk)
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return "pet_" +saveFilename;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/map-comment-image-upload")
    public String uploadCommentImage(@RequestPart("image") final MultipartFile image){
        if(image.isEmpty()){
            return "";
        }
        String orgFilename = image.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".")+1);
        String saveFilename = uuid + "." + extension;
        String fileFullPath = Paths.get(mapUploadDir,saveFilename).toString();

        try {
            //파일저장
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return "mapCommentImage_"+saveFilename;
        } catch (IOException e) {
            //
            throw new RuntimeException(e);
        }
    }


    @GetMapping(value = "/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public byte[] printEditorImage(@RequestParam("filename") final String filename) {
        // 업로드된 파일의 전체 경로
        StringTokenizer st = new StringTokenizer(filename,"_");
        String dir="";
        String type = st.nextToken();

        dir = switch (type) {
            case "post" -> postUploadDir;
            case "profile" -> profileUploadDir;
            case "item" -> itemUploadDir;
            case "item-comment" -> itemCommentUploadDir;
            case "pet" -> petUploadDir;
            case "mapCommentImage" -> mapUploadDir;
            default -> throw new RuntimeException("path is not valid");
        };


        String fileFullPath = Paths.get(dir, st.nextToken()).toString();

        // 파일이 없는 경우 예외 throw
        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }

        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e.getMessage());
        }
    }
    @GetMapping(value = "/comment", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public ResponseEntity<byte[]> printImageComment(@RequestParam(value="filename") final String filename) {
        // 업로드된 파일의 전체 경로
        String fileFullPath = Paths.get(commentUploadDir, filename).toString();


        // 파일 객체 생성
        File uploadedFile = new File(fileFullPath);

        // 파일 존재 여부 확인
        if (!uploadedFile.exists()) {
            System.out.println("file not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found".getBytes());
        }

        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());

            // 파일의 MediaType 설정
            MediaType mediaType = getMediaTypeForFileName(filename);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);

        } catch (IOException e) {
            // 예외 발생 시 Internal Server Error와 함께 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading file".getBytes());
        }
    }
    @PostMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestBody FileDownloadDTO fileDownloadDTO) {
        try {
            // 파일 경로 설정
            String basePath = fileDownloadDTO.getFilePath();
            File file = new File(basePath);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Resource 생성
            Resource resource = new FileSystemResource(file);
            String[] nameParts = fileDownloadDTO.getFilePath().split("_");  // "_"로 파일 이름 분리
            String newFileName = nameParts.length > 2 ? nameParts[2] : nameParts[0];  // 첫 번째 "_" 이후 부분을 새 파일명으로 사용
            System.out.println("here 1 " + fileDownloadDTO.getFilePath());
            System.out.println("here 2 " + newFileName);



            // 파일 이름 인코딩 (UTF-8로 한글 및 특수문자 처리)
            String encodedFileName = URLEncoder.encode(newFileName, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20"); // 공백을 %20으로 처리
            // MIME 타입 추정 (파일 확장자에 맞게)
            String mimeType = getMimeType(fileDownloadDTO.getFilePath());
            // 헤더 설정: UTF-8 인코딩을 지원하는 방식으로 처리
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);
            headers.add(HttpHeaders.CONTENT_TYPE, mimeType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private MediaType getMediaTypeForFileName(String filename) {
        if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jfif")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (filename.endsWith(".avif")) {
            return MediaType.parseMediaType("image/avif");
        }else {
            return MediaType.APPLICATION_OCTET_STREAM; // 기본값
        }
    }

    // 확장자에 맞는 MIME 타입 반환
    private String getMimeType(String filePath) {
        String mimeType = null;
        try {
            mimeType = Files.probeContentType(Paths.get(filePath)); // Java NIO 방식으로 MIME 타입 추정
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // 기본 MIME 타입
            }
        } catch (IOException e) {
            mimeType = "application/octet-stream"; // 예외 발생 시 기본 MIME 타입
        }
        return mimeType;
    }


    private static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf('.') > 0) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        } else {
            return ""; // 확장자가 없는 경우 빈 문자열 반환
        }
    }



}
