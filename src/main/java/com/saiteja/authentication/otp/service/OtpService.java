package com.saiteja.authentication.otp.service;

import org.springframework.stereotype.Service;

import com.saiteja.authentication.otp.entity.OtpPurpose;
import com.saiteja.authentication.otp.entity.OtpVerification;
import com.saiteja.authentication.otp.repository.OtpVerificationRepository;

import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {
	
	private static final int OTP_EXPIRY_MINUTES = 5;
	private static final int MAX_ATTEMPTS = 3;
	
	private final OtpVerificationRepository otpRepository;
	
	
	// ------------------- GENERATE OTP -------------------
	public String generateAndSaveOtp(Long userId, OtpPurpose purpose) {
		
		String otp = generateOtp();
		
		OtpVerification otpEntity = OtpVerification.builder()
		        .userId(userId)
		        .otp(otp)
		        .purpose(purpose)
		        .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
		        .verified(false)
		        .attempts(0)
		        .createdAt(LocalDateTime.now())
		        .build();

		
		otpRepository.save(otpEntity);
		
		return otp;
	}
	
	// ------------------- VERIFY OTP -------------------
	public void verifyOtp(Long userId, String otp, OtpPurpose purpose) {
		OtpVerification otpEntity = otpRepository
				.findTopByUserIdAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(userId, purpose)
				.orElseThrow(() -> new RuntimeException("OTP not found. Please request a new one."));
		
		if(otpEntity.isExpired()) {
			throw new RuntimeException("OTP has expired. Please request a new one.");
		}
		
		if(otpEntity.getAttempts() >= MAX_ATTEMPTS) {
			throw new RuntimeException("Maximum verification attempts exceeded. Please request a new OTP.");
		}
		
		if(!otpEntity.getOtp().equals(otp)) {
			otpEntity.incrementAttempts();
			otpRepository.save(otpEntity);
			throw new RuntimeException("Invalid OTP. Please try again.");
		}
		
		otpEntity.setVerified(true);
		otpRepository.save(otpEntity);
	}
	
	// ------------------- OTP GENERATOR -------------------
	private String generateOtp() {
		SecureRandom random = new SecureRandom();
		int otpInt = 100000 + random.nextInt(900000);
		return String.valueOf(otpInt);
	}

}
