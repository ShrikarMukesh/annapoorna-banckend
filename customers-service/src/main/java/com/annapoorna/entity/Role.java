package com.annapoorna.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	ADMIN,
	CUSTOMER,
	RESTURENT_OWNER,
	DELIVERY_BOY,
	SUPPORT_TEAM;

	public String getAuthority() {
		return name();
	}
}