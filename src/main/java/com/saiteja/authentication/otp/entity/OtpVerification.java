package com.saiteja.authentication.otp.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "otp_verification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	private boolean verified;
	
	@Column(nullable = false)
	private int attempts;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	
	
	// -------- BUSINESS METHODS --------
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public void incrementAttempts() {
        this.attempts++;
    }

}
