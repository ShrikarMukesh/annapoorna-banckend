package com.annapoorna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {

    private String email;
    private String phoneNumber;

    private String twitter;

    private String facebook;

    private String instagram;

}
