package com.hearit.controller;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hearit.dto.login.LoginRequestDTO;
import com.hearit.dto.login.LoginResponseDTO;
import com.hearit.dto.register.RegisterRequestDTO;
import com.hearit.dto.register.RegisterResponseDTO;
import com.hearit.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDTO> register(
			@RequestBody RegisterRequestDTO request
	) {
		RegisterResponseDTO response = authService.register(request);
		if (Objects.isNull(response)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		return ResponseEntity.ok(authService.register(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(
			@RequestBody LoginRequestDTO request
	) {
		LoginResponseDTO response = authService.authenticate(request);
		if (Objects.isNull(response)) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(response);
	}
}
