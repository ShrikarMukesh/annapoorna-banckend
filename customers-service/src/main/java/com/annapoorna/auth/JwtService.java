package com.annapoorna.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {


	public static final String SECRET = "ajkhakjlzomwnxywtgsfxcdegbjeplqmbxfdgehcbnwkwhwhgcwjwkcwkhcwkjhwkjhwkjhwkjh";


	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		// Check against both username and email, as UserDetails might return either depending on how it was loaded
		String userDetailsUsername = userDetails.getUsername();
		// If UserDetails username is null (unlikely but possible), fallback to false
		if (userDetailsUsername == null) return false;
		
		return (username.equals(userDetailsUsername) && !isTokenExpired(token));
	}


	public String generateToken(String username){
		Map<String,Object> claims=new HashMap<>();
		return createToken(claims,username);
	}

	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}
}
