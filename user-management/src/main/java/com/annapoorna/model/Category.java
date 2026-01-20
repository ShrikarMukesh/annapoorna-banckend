package com.annapoorna.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Category {

    @Id
    private Long id;

    private String name;

    private Restaurant restaurant;
}
