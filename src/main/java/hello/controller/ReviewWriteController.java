package hello.controller;


import hello.dto.animation.ReviewDTO;
import hello.service.ReviewWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewWriteController {

    private final ReviewWriteService reviewService;

    @Autowired
    public ReviewWriteController(ReviewWriteService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Long> saveReview(@RequestBody ReviewDTO reviewDTO) {
        Long reviewId = reviewService.saveReview(reviewDTO);
        return new ResponseEntity<>(reviewId, HttpStatus.CREATED);
    }
}
