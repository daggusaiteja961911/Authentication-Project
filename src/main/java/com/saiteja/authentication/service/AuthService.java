package com.saiteja.authentication.service;

import com.saiteja.authentication.model.User;
import com.saiteja.authentication.model.UserStatus;
import com.saiteja.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	//private final PasswordEncoder passwordEncoder;
	
	public String login(String username, String rawPassword) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Invalid username"));
		
		if (user.getStatus() != UserStatus.ACTIVE) {
			throw new RuntimeException("User account is not active");
		} 
		
		//boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());
		
//		if (!passwordMatches) {
//			throw new RuntimeException("Invalid password");
//		}
		
		if(user.getPassword().equals(rawPassword) == false) {
			throw new RuntimeException("Invalid password");
		}
		
		// Later: generate JWT / trigger OTP
		return "Login successful";
	}
}
