package com.annapoorna.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.annapoorna.review.entity.RatingAndReview;
import com.annapoorna.review.repository.RatingAndReviewRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RatingAndReviewService {

    @Autowired
    private RatingAndReviewRepository ratingAndReviewRepository;

    @Autowired
    private AuditLogService auditLogService;

    public List<RatingAndReview> getRatingsAndReviewsByUserId(String userId) {
        log.info("Fetching ratings and reviews for user id: {}", userId);
        List<RatingAndReview> reviews = ratingAndReviewRepository.findByUserId(userId);

        auditLogService.logSuccess(
                "READ",
                "RatingAndReview",
                null,
                userId,
                null,
                null,
                "Fetched ratings and reviews for user. Count: " + reviews.size(),
                null);

        return reviews;
    }

    public List<RatingAndReview> getRatingsAndReviewsByUsername(String username) {
        log.info("Fetching ratings and reviews for username: {}", username);
        List<RatingAndReview> reviews = ratingAndReviewRepository.findByUsername(username);

        auditLogService.logSuccess(
                "READ",
                "RatingAndReview",
                null,
                null,
                null,
                null,
                "Fetched ratings and reviews for username: " + username + ". Count: " + reviews.size(),
                null);

        return reviews;
    }

    public RatingAndReview postRatingAndReview(RatingAndReview ratingAndReview) {
        log.info("Posting new rating and review from user: {}", ratingAndReview.getUserId());
        RatingAndReview savedReview = ratingAndReviewRepository.save(ratingAndReview);

        auditLogService.logSuccess(
                "CREATE",
                "RatingAndReview",
                savedReview.getReviewId(),
                ratingAndReview.getUserId(),
                null,
                savedReview,
                "Rating and review posted successfully. Rating: " + ratingAndReview.getRating(),
                null);

        return savedReview;
    }
}
