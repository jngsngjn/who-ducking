package hello.controller;

import hello.dto.animation.GetAniListDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.user.User;
import hello.service.AnimationService;
import hello.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommunityController {

    private final AnimationService animationService;
    private final UserService userService;

    // 전체 애니메이션 데이터 조회
    @GetMapping("/community")
    public String getAllAnimationsWithReviewData(Model model) {
        model.addAttribute("animationLists", animationService.getAllAnimationWithReviewData());
        return "reviewBoard";
    }

    /* 애니메이션 상세페이지 GET(리뷰작성페이지)
     * PathVariable id = animationId */
    @GetMapping("community/{id}")
    public String getAnimation(@PathVariable Long id,
                               Model model,
                               @AuthenticationPrincipal CustomOAuth2User user) {

        /* @ 애니메이션 데이터 조회 */
        Animation animation = animationService.getAnimationById(id);
        animation.setId(id); //경로에 있는 애니메이션 id를 animation 클래스의 id필드에 세팅
        model.addAttribute("aniDetailInfo", animation);

        List<GetAniListDTO> reviewAndScore = animationService.getCountReviewAndScore(id);
        model.addAttribute("totalReviews", reviewAndScore);

        User loginUser = userService.getLoginUserDetail(user); // -> dto의 CustomOauth2User로 받아와야해
        model.addAttribute("nickname", loginUser.getNickname());
        model.addAttribute("userId", loginUser.getId());



        /* @ 리뷰 데이터 조회 */
        List<Review> reviews = animationService.getReviewsByAnimationId(id);
        model.addAttribute("reviews", reviews);
        return "reviewWrite";
    }

}