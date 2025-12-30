package com.saiteja.authentication.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private long expiration;

	// Generate signing key
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	// Generate JWT token
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)          // username inside token
				.setIssuedAt(new Date())       // token creation time
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	// Extract all claims
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	// Extract username from token
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	// Check token expiry
	public boolean isTokenExpired(String token) {
		return extractAllClaims(token)
				.getExpiration()
				.before(new Date());
	}
	
	// Validate token
	public boolean validateToken(String token, String username) {

		return username.equals(extractUsername(token)) && !isTokenExpired(token);
	}
	
	// Alternative method name for clarity
	public boolean isTokenValid(String token, String username) {
	    String extractedUsername = extractUsername(token);
	    return extractedUsername.equals(username) && !isTokenExpired(token);
	}

}
