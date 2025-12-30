package com.saiteja.authentication.service;

import com.saiteja.authentication.model.User;
import com.saiteja.authentication.model.UserStatus;
import com.saiteja.authentication.otp.entity.OtpPurpose;
import com.saiteja.authentication.otp.service.OtpService;
import com.saiteja.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	//private final PasswordEncoder passwordEncoder;
	
	private final OtpService otpService;
	private final EmailService emailService;
	
	// ----------------- Registration code would go here -----------------
	public String register(String username, String email, String password) {
		if(userRepository.existsByUsername(username)) {
			throw new RuntimeException("Username already taken");
		}
		
		if(userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email already registered");
		}
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password); // plain text for now
		user.setStatus(UserStatus.ACTIVE);
		userRepository.save(user);
		
		return "User registered successfully";
	}
	
	// ----------------- Login STEP 1 -----------------
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
		
		// Generate OTP
		String otp = otpService.generateAndSaveOtp(
				user.getId(), 
				OtpPurpose.LOGIN
		);
		
		// Send OTP via email
		emailService.sendOtpEmail(
				user.getEmail(),
				user.getUsername(),
				otp
		);
		
		return "OTP sent to registered email";
		
		// Later: generate JWT / trigger OTP
		//return "Login successful";
	}
	
	// ----------------- Login STEP 2 -----------------
	public void verifyLoginOtp(String username, String otp) {
		User user = userRepository.findByUsername(username)
		        .orElseThrow(() -> new RuntimeException("User not found"));

		otpService.verifyOtp(user.getId(), otp, OtpPurpose.LOGIN);
	}
}
