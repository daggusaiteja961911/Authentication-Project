package com.saiteja.authentication.controller;

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
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
		String response = authService.login(request.getUsername(), request.getPassword());
		return ResponseEntity.ok(response);
	}

}
