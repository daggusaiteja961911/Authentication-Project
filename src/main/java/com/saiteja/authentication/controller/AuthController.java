package com.saiteja.authentication.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saiteja.authentication.dto.LoginRequest;
import com.saiteja.authentication.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	// ----------------- Registration code would go here -----------------
	@PostMapping("/register")
	public String register(@RequestBody Map<String, String> request) {
		return authService.register(
				request.get("username"), 
				request.get("email"), 
				request.get("password")
		);
	}
	
//	@PostMapping("/login")
//	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
//		String response = authService.login(request.getUsername(), request.getPassword());
//		return ResponseEntity.ok(response);
//	}
	
	// ----------------- Login STEP 1 -----------------
	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> request) {
		return authService.login(
				request.get("username"), 
				request.get("password")
		);
	}
	
	// ----------------- Login STEP 2 -----------------
	@PostMapping("/verify-otp")
	public Map<String, String> verifyOtp(@RequestBody Map<String, String> request) {
		String token = authService.verifyLoginOtp(
				request.get("username"), 
				request.get("otp")
		);
		
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		
		return response;
	}
	
	// ----------------- Forgot Password code would go here -----------------
	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		authService.forgotPassword(email);
		return "OTP sent to registered email if it exists.";
	}
	
	// ----------------- Verify Reset OTP STEP  -----------------
	@PostMapping("/verify-reset-otp")
	public String verifyResetOtp(@RequestBody Map<String, String> request) {
		
		String email = request.get("email");
		String otp = request.get("otp");
		
		authService.verifyResetPasswordOtp(email, otp);
		
		return "OTP verified Successfully.";
	}
	
	// ----------------- Reset Password STEP  -----------------
	@PostMapping("/reset-password")
	public String resetPassword(@RequestBody Map<String, String> request) {
		
		String email = request.get("email");
		String newPassword = request.get("newPassword");
		
		authService.resetPassword(email, newPassword);
		
		return "Password reset Successfully.";
	}

}
