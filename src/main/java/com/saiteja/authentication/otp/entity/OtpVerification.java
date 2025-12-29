package com.saiteja.authentication.otp.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "otp_verification")
@Data
public class OtpVerification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Link OTP to a user
	@Column(nullable = false)
	private Long userId;
	
	@Column(nullable = false, length = 6)
	private String otp;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OtpPurpose purpose;
	
	@Column(nullable = false)
	private LocalDateTime expiresAt;
	
	@Column(nullable = false)
	private boolean verified = false;
	
	@Column(nullable = false)
	private int attempts = 0;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;

}
