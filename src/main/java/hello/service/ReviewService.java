//package hello.service;
//
//import hello.dto.animation.ReviewDTO;
//import hello.entity.review.Review;
//import hello.entity.user.User;
//import hello.repository.AnimationRepository;
//import hello.repository.ReviewRepository;
//import hello.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class ReviewService {
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    public ReviewDTO createReview(ReviewDTO reviewDTO) {
//
//        User user = userRepository.findById(reviewDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
//
//        //프론트 단에서 id와 nickname 꺼내쓸수있을거같은데
//        Review review = new Review(
//                reviewDTO.getContent(),
//                reviewDTO.getLikeCount(),
//                reviewDTO.getDislikeCount(),
//                LocalDateTime.now(),
//                reviewDTO.getScore(),
//                user.getId(),
//                user.getNickname()
//        );
//
//        review = reviewRepository.save(review);
//
//        return new ReviewDTO(
//                review.getContent(),
//                review.getLikeCount(),
//                review.getDislikeCount(),
//                review.getWriteDate(),
//                review.getScore(),
//                review.getId(),
//                review.getNickname()
//        );
//    }
//
//}
