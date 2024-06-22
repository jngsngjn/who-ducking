package hello.repository.server;

import hello.entity.user.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${profilePath}")
    private String profilePath;

    @Value("${aniPath}")
    private String aniPath;

    @Value("${prizePath}")
    private String prizePath;

    @Value("${boardPath}")
    private String boardPath;

    public Image storeProfileImageFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        // 클라이언트가 업로드한 파일명을 서버에 저장할 고유한 파일명으로 변경한다.
        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());

        String fullPath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(fullPath));

        return new Image(storeFileName, fullPath);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractedExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    public String getFullPath(String filename) {
        return profilePath + filename;
    }

    private String extractedExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public void deleteProfileImage(String filename) {
        String fullPath = getFullPath(filename);
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
    }

    public String storeAnimation(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return "";
        }

        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());

        String fullPath = aniPath + storeFileName;
        multipartFile.transferTo(new File(fullPath));
        return fullPath;
    }

    public String storePrize(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return "";
        }

        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());

        String fullPath = prizePath + storeFileName;
        multipartFile.transferTo(new File(fullPath));
        return fullPath;
    }

    public void deleteBoardImage(String filename) {
        String fullPath = boardPath + filename;
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
    }
}