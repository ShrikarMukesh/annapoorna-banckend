package com.annapoorna.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annapoorna.review.entity.RatingAndReview;
import com.annapoorna.review.service.RatingAndReviewService;


@RestController
@RequestMapping("/rating-and-review")
public class RatingAndReviewController {
	
    @Autowired
    private RatingAndReviewService ratingAndReviewService;

    @GetMapping("/user/{userId}")
    public List<RatingAndReview> getRatingsAndReviewsByUserId(@PathVariable String userId) {
        return ratingAndReviewService.getRatingsAndReviewsByUserId(userId);
    }

    @GetMapping("/user/{username}")
    public List<RatingAndReview> getRatingsAndReviewsByUsername(@RequestParam String username) {
        return ratingAndReviewService.getRatingsAndReviewsByUsername(username);
    }

    @PostMapping("/addRating")
    public RatingAndReview postRatingAndReview(@RequestBody RatingAndReview ratingAndReview) {
        return ratingAndReviewService.postRatingAndReview(ratingAndReview);
    }

    // Exception handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
    
}
