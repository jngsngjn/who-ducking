package hello.controller;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.GetAniDetailDTO;
import hello.entity.animation.AnimationRating;
import hello.repository.AnimationRepository;
import hello.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity <GetAniDetailDTO> getAnimationDetails(@PathVariable Long id, Model model) {

        System.out.println("Received ID: " + id);

        GetAniDetailDTO aniDetailInfo = animationService.getAnimationDetails(id);

        System.out.println(id);
        System.out.println("Animation ID: " + aniDetailInfo.getAnimationId());
        System.out.println("Name: " + aniDetailInfo.getName());
//        System.out.println("Author: " + aniDetailInfo.getAuthor());
//        System.out.println("Description: " + aniDetailInfo.getDescription());
//        System.out.println("First Date: " + aniDetailInfo.getFirstDate());
//        System.out.println("Image Path: " + aniDetailInfo.getImagePath());
//        System.out.println("Is Finished: " + aniDetailInfo.getIsFinished());
//        System.out.println("Rating: " + aniDetailInfo.getRating());
//        System.out.println("Review ID: " + aniDetailInfo.getReviewId());
//        System.out.println("Like Count: " + aniDetailInfo.getLikeCount());
//        System.out.println("Dislike Count: " + aniDetailInfo.getDislikeCount());
//        System.out.println("Write Date: " + aniDetailInfo.getWriteDate());
//        System.out.println("Score: " + aniDetailInfo.getScore());
//        System.out.println("Review Content: " + aniDetailInfo.getReviewContent());
//        System.out.println("User ID: " + aniDetailInfo.getUserId());
//        System.out.println("User Nickname: " + aniDetailInfo.getUserNickname());

        model.addAttribute("aniDetailInfo", aniDetailInfo);
        return ResponseEntity.ok(aniDetailInfo);
    }

//    @GetMapping("/community/{id}")
//    public ResponseEntity<GetAniDetailDTO> getAnimationDetails(@PathVariable Long id, Model model) {
//        GetAniDetailDTO aniDetail = animationService.getAnimationDetails(id);
//        model.addAttribute("aniDetail", aniDetail);
//        return ResponseEntity.ok(aniDetail);
//    }

}
