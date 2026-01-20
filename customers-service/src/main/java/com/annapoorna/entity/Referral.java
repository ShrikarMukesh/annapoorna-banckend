package com.annapoorna.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Referral {

	private String id;

	private String referrerId;
	private String referredId;
	private boolean rewardGiven;
}

