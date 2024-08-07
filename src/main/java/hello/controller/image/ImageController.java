package hello.controller.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class ImageController {

    @Value("${levelPath}")
    private String levelPath;

    @Value("${profilePath}")
    private String profilePath;

    @Value("${boardPath}")
    private String boardPath;

    @Value("${prizePath}")
    private String prizePath;

    @Value("${aniPath}")
    private String aniPath;

    @Value("${popupPath}")
    private String popupPath;

    @GetMapping("/image/{type}/{imageName}")
    public Resource renderImage(@PathVariable("type") String type, @PathVariable("imageName") String imageName) throws MalformedURLException {
        String basePath = null;
        if (type.equals("level")) {
            basePath = levelPath;
        }
        else if (type.equals("profile")) {
            basePath = profilePath;
        }
        else if (type.equals("board")){
            basePath = boardPath;
        } else if (type.equals("prize")) {
            basePath = prizePath;
        } else if (type.equals("ani")) {
            basePath = aniPath;
        } else if (type.equals("popup")) {
            basePath = popupPath;
        }
        return new UrlResource("file:" + basePath + imageName);
    }

    @GetMapping("/image/{imageName}")
    public Resource renderImageHeader(@PathVariable("imageName") String imageName) throws MalformedURLException {
        if (imageName.contains("level")) {
            return new UrlResource("file:" + levelPath + imageName);
        } else {
            return new UrlResource("file:" + profilePath + imageName);
        }
    }
}