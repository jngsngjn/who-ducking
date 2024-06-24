package hello.controller.animations;

import hello.dto.animation.GetAniListDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.user.User;
import hello.service.animations.AnimationService;
import hello.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AnimationsController {

    private final AnimationService animationService;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AnimationsController.class);


    // 전체 애니메이션 데이터 조회
    @GetMapping("/animations")
    public String getAllAnimationsWithReviewData(Model model) {
        model.addAttribute("animationLists", animationService.getAllAnimationWithReviewData());
        return "reviewBoard";
    }

    // 필터링해서 선택된 장르 id로 보여줄 애니 리스트 조회

    /* 애니메이션 상세페이지 GET(리뷰작성페이지)
     * PathVariable id = animationId */
    @GetMapping("animations/{id}")
    public String getAnimation(@PathVariable Long id,
                               Model model,
                               @AuthenticationPrincipal CustomOAuth2User user) {

        /* @ 애니메이션 데이터 조회 */
        Animation animation = animationService.getAnimationById(id);
        animation.setId(id); //경로에 있는 애니메이션 id를 animation 클래스의 id필드에 세팅
        model.addAttribute("aniDetailInfo", animation);


        /* @ 전체 리뷰수와 평점 */
        List<GetAniListDTO> reviewAndScore = animationService.getCountReviewAndScore(id);
        model.addAttribute("totalReviews", reviewAndScore);


        /* @ 로그인 유저정보  */
        User loginUser = userService.getLoginUserDetail(user); // -> dto의 CustomOauth2User로 받아와야해
        model.addAttribute("nickname", loginUser.getNickname());
        model.addAttribute("userId", loginUser.getId());


        /* @ 애니의 리뷰 데이터 조회 (최신순&좋아요순) */
        List<Review> recentReviews = animationService.getRecentReviewsByAnimationId(id); // 최신순
        List<Review> topReviews = animationService.getTopReviewsByAnimationId(id); //인기순
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("topReviews", topReviews);
        return "reviewWrite";
    }
}


