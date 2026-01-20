package com.annapoorna.review.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ratings_and_reviews")
public class RatingAndReview {
	
    @Id
    private String ReviewId; // Using String for id as MongoDB ObjectIds are strings
    private Long productId;
    private String userId;
    private String username;
    private int rating;
    private String reviewText;
}