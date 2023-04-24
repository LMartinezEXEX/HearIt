package com.hearit.spotify.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/spotify/auth")
public class SpotifyAuthController {

	@GetMapping("token")
	public ResponseEntity<Void> retrieveAccessToken(
			@RequestParam("code") String userCode, 
			HttpServletResponse response
	) throws IOException {
		response.sendRedirect("http://localhost:4200/home?code=" + userCode);
		return ResponseEntity.noContent().build();
	}
	
}
