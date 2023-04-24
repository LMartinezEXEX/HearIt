package com.hearit.service;

import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hearit.dto.login.LoginRequestDTO;
import com.hearit.dto.login.LoginResponseDTO;
import com.hearit.dto.register.RegisterRequestDTO;
import com.hearit.dto.register.RegisterResponseDTO;
import com.hearit.model.Role;
import com.hearit.model.User;
import com.hearit.repository.UserRepository;
import com.hearit.spotify.service.SpotifyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	
	private final UserService userService;
	
	private final JwtService jwtService;
	
	private final SpotifyService spotifyService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;

	public RegisterResponseDTO register(RegisterRequestDTO request) {
		User user = userService.getUser(request.getUsername());
		if (Objects.nonNull(user)) {
			return null;
		}
		
		user = User.builder()
			.username(request.getUsername())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(Role.USER)
			.build();
		userRepository.save(user);
		
		return RegisterResponseDTO.builder()
				.id(user.getId())
				.response("User created successfully.")
				.build();
	}
	
	public LoginResponseDTO authenticate(LoginRequestDTO request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(), 
						request.getPassword()
				)
		);
		User user = userService.getUser(request.getUsername());
		if (Objects.isNull(user)) {
			return null;
		}
		String token = jwtService.generateToken(user);
		
		return LoginResponseDTO.builder()
				.token(token)
				.accessUrl(spotifyService.getAccesUrl())
				.build();
	}
}
