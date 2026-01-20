package com.annapoorna.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.annapoorna.entity.Address;
import com.annapoorna.entity.Payment;
import com.annapoorna.entity.Referral;
import com.annapoorna.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {

	private String customerId;
	private String firstName;
	private String userName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Set<Role> roles;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<Address> address;
	private List<Payment> payments;
	private List<Referral> referrals;
	public CustomerResponseDTO() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}
}
