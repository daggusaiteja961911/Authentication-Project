package com.saiteja.authentication.otp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saiteja.authentication.otp.entity.OtpPurpose;
import com.saiteja.authentication.otp.entity.OtpVerification;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
	
	// Fetch latest unused OTP for user and purpose
	Optional<OtpVerification> findTopByUserIdAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(Long userId, OtpPurpose purpose);

}
