package com.annapoorna.restaurantservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "restaurantsv2")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDTO {

    @Id
    private String restId;
    private String name;
    private String cloudinaryImageId;
    private String locality;
    private String areaName;
    private String costForTwo;
    private List<String> cuisines;
    private double avgRating;
    private String parentId;
    private String avgRatingString;
    private String totalRatingsString;
    private boolean promoted;
    private String adTrackingId;
    private Sla sla;
    private Availability availability;
    private boolean isOpen;
    private AggregatedDiscountInfoV3 aggregatedDiscountInfoV3;
    private ExternalRatings externalRatings;
    private BadgesV2 badgesV2; // Added badgesV2

    // Inner classes for nested fields
    @Data
    public static class Sla {
        private int deliveryTime;
        private double lastMileTravel;
        private String serviceability;
        private String slaString;
        private String lastMileTravelString;
        private String iconType;
    }

    @Data
    public static class Availability {
        private String nextCloseTime;
        private boolean opened;
    }

    @Data
    public static class AggregatedDiscountInfoV3 {
        private String header;
        private String subHeader;
    }

    @Data
    public static class ExternalRatings {
        private AggregatedRating aggregatedRating;
        private String source;
        private String sourceIconImageId;

        @Data
        public static class AggregatedRating {
            private String rating;
            private String ratingCount;
        }
    }

    @Data
    public static class BadgesV2 {
        private EntityBadges entityBadges;

        @Data
        public static class EntityBadges { // Nested EntityBadges class
            private TextBased textBased;
            private ImageBased imageBased;
            private TextExtendedBadges textExtendedBadges;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class TextBased {
                // Add fields for text-based badges if needed
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ImageBased {
                // Add fields for image-based badges if needed
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class TextExtendedBadges {
                // Add fields for extended text badges if needed
            }
        }
    }

}
