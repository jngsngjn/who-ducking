package hello.controller;

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
    private String path;

    @GetMapping("/image/{imageName}")
    public Resource renderImage(@PathVariable("imageName") String imageName) throws MalformedURLException {
        return new UrlResource("file:" + path + imageName);
    }
}