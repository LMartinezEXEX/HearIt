package com.hearit.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hearit.dto.spotifyData.TracksResponseDTO;
import com.hearit.dto.user.SpotifyUserCodeDTO;
import com.hearit.dto.user.TrackIdRequestDTO;
import com.hearit.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/spotifyCode")
	public ResponseEntity<Void> saveSpotifyCode(
			@RequestBody SpotifyUserCodeDTO code,
			Authentication auth
	) {
		userService.saveSpotifyCode(code, auth);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/tracks")
	public ResponseEntity<TracksResponseDTO> getAllFavoriteTracks(
			@RequestHeader("spotify_access_token") String accessToken,
			Authentication auth
	) {
		TracksResponseDTO response = userService.getAllFavoritesTracks(accessToken, auth);
		if (Objects.isNull(response) || response.getTracks().length == 0) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping("/tracks")
	public ResponseEntity<Void> addToFavorite(
			@RequestBody TrackIdRequestDTO request, 
			Authentication auth
	) {
		userService.saveToFavorite(request, auth);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/tracks")
	public ResponseEntity<Void> deleteFromFavorite(
			@RequestBody TrackIdRequestDTO request, 
			Authentication auth
	) {
		userService.removeFromFavorite(request, auth);
		return ResponseEntity.noContent().build();
	}
}
