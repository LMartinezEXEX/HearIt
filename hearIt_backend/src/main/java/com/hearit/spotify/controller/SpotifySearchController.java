package com.hearit.spotify.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hearit.dto.spotifyData.SearchResponseDTO;
import com.hearit.spotify.service.SpotifyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/spotify/search")
@RequiredArgsConstructor
public class SpotifySearchController {
	
	private final SpotifyService spotifyService;
	
	@GetMapping()
	public ResponseEntity<SearchResponseDTO> search(
			@RequestParam("q") String query,
			@RequestParam(name = "amount", defaultValue = "10") int amount,
			@RequestHeader("spotify_access_token") String accessToken
	) {
		SearchResponseDTO response = spotifyService.search(query, amount, accessToken);
		if (Objects.isNull(response) || response.getTracks().length == 0) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(response);
	}
}