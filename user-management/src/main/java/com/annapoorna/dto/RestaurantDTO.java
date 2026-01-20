package com.annapoorna.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "restaurantsv3")
public class RestaurantDTO {

    private String id;

    private String title;

    private List<String> images;

    private String description;

    private String location;

}
