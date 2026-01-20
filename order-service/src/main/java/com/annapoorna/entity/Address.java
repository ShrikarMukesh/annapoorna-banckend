package com.annapoorna.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@AllArgsConstructor
@Setter
@Getter
public class Address {

	@NotBlank(message = "Street cannot be blank")
	private String street;

	@NotBlank(message = "City cannot be blank")
	private String city;

	@NotBlank(message = "State cannot be blank")
	private String state;

	@NotBlank(message = "Country cannot be blank")
	private String country;

	@NotBlank(message = "Postal Code cannot be blank")
	private String postalCode;
}
