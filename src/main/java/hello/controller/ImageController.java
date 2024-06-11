package hello.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class ImageController {

    private static final String IMAGE_BASE_PATH = "/Users/jeongseongjin/server/level/"; // 기본 경로

    @GetMapping("/image/{levelImagePath}")
    public Resource downloadImage(@PathVariable("levelImagePath") String levelImagePath) throws MalformedURLException {
        System.out.println("fileName = " + levelImagePath);
        return new UrlResource("file:" + IMAGE_BASE_PATH + levelImagePath);
    }
}