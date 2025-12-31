package com.saiteja.authentication.otp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.saiteja.authentication.otp.entity.OtpPurpose;
import com.saiteja.authentication.otp.entity.OtpVerification;

import jakarta.transaction.Transactional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
	
	// Fetch latest unused OTP for user and purpose
	Optional<OtpVerification> findTopByUserIdAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(Long userId, OtpPurpose purpose);
	
	// Check if there is any unverified OTP for user and purpose
	boolean existsByUserIdAndPurposeAndVerifiedTrue(Long userId, OtpPurpose purpose);
	
	@Modifying
	@Transactional
	// Delete OTPs by user and purpose
	void deleteByUserIdAndPurpose(Long userId, OtpPurpose purpose);
	
//	@Modifying
//	@Query("""
//	UPDATE OtpVerification o
//	SET o.verified = true
//	WHERE o.userId = :userId AND o.purpose = :purpose
//	""")
//	void invalidateOtps(Long userId, OtpPurpose purpose);


}
