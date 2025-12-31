package com.saiteja.authentication.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.saiteja.authentication.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


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
	public String generateToken(User user) {
		
		Map<String, Object> claims = new HashMap<>();
		// Add ROLE Claim
		claims.put("role", user.getRole().name());
		
		return Jwts.builder()
				.setClaims(claims)          // custom claims
				.setSubject(user.getUsername())          // username inside token
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
	
	// Extract role
	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
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


	// Extract specific claim using a resolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

}
