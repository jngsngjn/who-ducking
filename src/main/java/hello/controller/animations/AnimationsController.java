package hello.controller.animations;

import hello.dto.animation.GetAniListDTO;
import hello.dto.animation.ReviewLikeDTO;
import hello.dto.user.CustomOAuth2User;
import hello.entity.animation.Animation;
import hello.entity.review.Review;
import hello.entity.user.User;
import hello.repository.db.AnimationRepository;
import hello.repository.db.ReviewRepository;
import hello.service.animations.AnimationService;
import hello.service.animations.GenreService;
import hello.service.animations.ReviewService;
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
    private final GenreService genreService;
    private final ReviewService reviewService;
    private final AnimationRepository animationRepository;

    private static final Logger logger = LoggerFactory.getLogger(AnimationsController.class);
    private final ReviewRepository reviewRepository;

    // 전체 애니메이션 데이터 조회
    @GetMapping("/animations")
    public String getAllAnimationsWithReviewData(Model model) {
        model.addAttribute("genreLists", genreService.getAllGenres());
        model.addAttribute("animationLists", animationService.getAllAnimationWithReviewData());
        return "review/reviewBoard";
    }

    /* 애니메이션 상세페이지 GET(리뷰작성페이지)
     * PathVariable id = animationId */
    @GetMapping("/animations/{id}")
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

        boolean isAuthenticated = user != null;
        model.addAttribute("isAuthenticated", isAuthenticated); // 현재 사용자가 인증된 사용자인지 여부

        // 인증된 경우에만 model에 추가되는 값들
        if (isAuthenticated) {
            /* @ 로그인 유저정보  */
            User loginUser = userService.getLoginUserDetail(user); // -> dto의 CustomOauth2User로 받아와야해
            model.addAttribute("nickname", loginUser.getNickname());
            model.addAttribute("userId", loginUser.getId());

            // 좋아요 해내고 만다......
            // 좋아요 싫어요 정보 가져오기 조건문을 어떻게 줘야하지..
             List<ReviewLikeDTO> reviewLikes = animationService.getReviewLikesByAnimationId(id);
             model.addAttribute("reviewLikes", reviewLikes);
        }

            /* @ 애니의 리뷰 데이터 조회 (최신순&좋아요순) */
            List<Review> recentReviews = animationService.getRecentReviewsByAnimationId(id); // 최신순
            List<Review> topReviews = animationService.getTopReviewsByAnimationId(id); //인기순
            model.addAttribute("recentReviews", recentReviews);
            model.addAttribute("topReviews", topReviews);

            // 유저가 좋아요나 싫어요시 어떻게 배경 색을  남길까?
            // -> userId로 reviewLike 테이블을 조회하고 -> 거기에 존재한다면  -> reviewId를 이용 ->  animation의 reviewId와 일치하면 배경색을 변경

            int reviewCount = reviewService.getReviewCount(animation);
            model.addAttribute("reviewCount", reviewCount);
            return "review/reviewWrite";
        }

//        @GetMapping("/reviews/{reviewId}")
//        public Long getLikeUserId(@PathVariable Long reviewId) {
//
//        }

    }