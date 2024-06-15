package hello.controller;

import hello.dto.animation.GetAniListDTO;
import hello.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// @ 커뮤니티 페이지
@Controller
//@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private final AnimationService animationService;


    public CommunityController(AnimationService animationService) {
        this.animationService = animationService;
    }

    // 전체 애니메이션 데이터 조회
//    @GetMapping("/community")
//    public String animations(Model model) {
//        List<GetAniListDTO> aniLists = animationService.getAnimationReviews();
//        model.addAttribute("aniLists", aniLists);
//        aniLists.forEach(System.out::println);
//        return "reviewBoard";
//    }
    @GetMapping("/community")
    public String getAllAnimationsWithReviewData(Model model) {
        List<GetAniListDTO> animationLists = animationService.getAllAnimationWithReviewData();
        model.addAttribute(("animationLists"), animationLists);
        animationLists.forEach(dto -> {
            System.out.println("Animation ID: " + dto.getAnimationId());
            System.out.println("Image Path: " + dto.getImagePath());
            System.out.println("Score: " + dto.getScore());
            System.out.println("Review Count: " + dto.getReviewCount());
        });

        return "reviewBoard";
    }

    // 애니메이션 상세페이지(리뷰작성페이지)
    @GetMapping("/community/1")
    public String getAnimation() {
//    @GetMapping("/reviewBoard/{id}")
//    public String getAnimation(@PathVariable("id") Long id, Model model) {
//        GetOneAniDTO oneAnimation = animationService.findById(id);
//        model.addAttribute("animation", oneAnimation);
        return "reviewWrite";
    }




}
