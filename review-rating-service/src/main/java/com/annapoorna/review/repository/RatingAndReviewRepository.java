package com.annapoorna.review.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.annapoorna.review.entity.RatingAndReview;


@Repository
public interface RatingAndReviewRepository extends MongoRepository<RatingAndReview, String> {
    List<RatingAndReview> findByUserId(String userId);
    List<RatingAndReview> findByUsername(String username);
}
