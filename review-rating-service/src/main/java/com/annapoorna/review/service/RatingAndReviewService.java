package com.annapoorna.review.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.annapoorna.review.entity.RatingAndReview;
import com.annapoorna.review.repository.RatingAndReviewRepository;


@Service
public class RatingAndReviewService {
	
    @Autowired
    private RatingAndReviewRepository ratingAndReviewRepository;

    public List<RatingAndReview> getRatingsAndReviewsByUserId(String userId) {
        return ratingAndReviewRepository.findByUserId(userId);
    }

    public List<RatingAndReview> getRatingsAndReviewsByUsername(String username) {
        return ratingAndReviewRepository.findByUsername(username);
    }

    public RatingAndReview postRatingAndReview(RatingAndReview ratingAndReview) {
        return ratingAndReviewRepository.save(ratingAndReview);
    }
}
