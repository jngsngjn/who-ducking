package hello.controller;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.GetAniDetailDTO;
import hello.entity.animation.AnimationRating;
import hello.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//  커뮤니티 페이지
@Controller
public class CommunityController {

    @Autowired
    private final AnimationService animationService;


    public CommunityController(AnimationService animationService) {
        this.animationService = animationService;
    }

    // 전체 애니메이션 데이터 조회
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

    // 애니메이션 상세페이지 GET(리뷰작성페이지)
    @GetMapping("/community/{id}")
    public String getAnimationDetails(@PathVariable Long id, Model model) {
        GetAniDetailDTO aniDetailInfo = animationService.getAnimationDetailById(id);

        System.out.println("Animation ID: " + aniDetailInfo.getAnimationId());
        System.out.println("Name: " + aniDetailInfo.getName());
        System.out.println("Author: " + aniDetailInfo.getAuthor());
        System.out.println("Description: " + aniDetailInfo.getDescription());
        System.out.println("First Date: " + aniDetailInfo.getFirstDate());
        System.out.println("Image Path: " + aniDetailInfo.getImagePath());
        System.out.println("Is Finished: " + aniDetailInfo.getIsFinished());
        System.out.println("Rating: " + aniDetailInfo.getRating());

        model.addAttribute("aniDetailInfo", aniDetailInfo);
        return "reviewWrite";
    }


}
