package com.annapoorna.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Address {
	
	@NotBlank(message = "Street cannot be blank")
	private String street;

	@NotBlank(message = "City cannot be blank")
	private String city;

	@NotBlank(message = "State cannot be blank")
	private String state;

	@NotBlank(message = "Country cannot be blank")
	private String country;

	@NotBlank(message = "Zip code cannot be blank")
	private String zip;

	// Standard getters and setters
}
