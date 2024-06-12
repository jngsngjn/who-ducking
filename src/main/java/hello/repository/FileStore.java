package hello.repository;

import hello.entity.user.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${imagePath}")
    private String imagePath; // 실제 파일 데이터가 저장될 서버 위치

    // 서버에 파일을 저장 후 Image 반환
    public Image storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        // 클라이언트가 업로드한 파일명을 서버에 저장할 고유한 파일명으로 변경한다.
        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());

        // fileDir 경로에 고유한 파일명으로 파일을 저장한다.
        String fullPath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(fullPath));

        return new Image(storeFileName, fullPath);
    }

    // 서버 내부에서 관리할 고유한 파일명을 생성하여 반환한다.
    private String createStoreFileName(String originalFilename) {
        String ext = extractedExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    // 파일명과 함께 파일이 저장될 전체 경로를 만들어 반환한다.
    public String getFullPath(String filename) {
        return imagePath + filename;
    }

    // 서버에서도 확장자를 확인하기 위해 파일명에서 확장자를 추출하여 반환한다.
    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    // 서버에서 파일을 삭제하는 메서드
    public void deleteFile(String filename) {
        String fullPath = getFullPath(filename);
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
    }
}