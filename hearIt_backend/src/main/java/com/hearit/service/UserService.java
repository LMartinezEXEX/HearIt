package com.hearit.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hearit.dto.spotifyData.TracksResponseDTO;
import com.hearit.dto.user.TrackIdRequestDTO;
import com.hearit.model.User;
import com.hearit.repository.UserRepository;
import com.hearit.spotify.service.SpotifyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	private final SpotifyService spotifyService;
	
	public TracksResponseDTO getAllFavoritesTracks(
			String accessToken,
			Authentication auth
	) {
		User user = getUser(auth.getName());
		Set<String> trackIds = user.getFavoriteTrackIds();
		if (trackIds.isEmpty()) {
			return null;
		}
		return spotifyService.searchTrackIds(trackIds, accessToken);
	}

	public void saveToFavorite(
			TrackIdRequestDTO request, 
			Authentication auth
	) {
		User user = getUser(auth.getName());
		user.getFavoriteTrackIds().add(request.getId());
		userRepository.save(user);
	}
	
	public void removeFromFavorite(
			TrackIdRequestDTO request, 
			Authentication auth
	) {
		User user = getUser(auth.getName());
		user.getFavoriteTrackIds().remove(request.getId());
		userRepository.save(user);
	}
	
	public User getUser(String username) {
		Optional<User> userOpt = userRepository.findByUsername(username);
		return (userOpt.isPresent()) ? userOpt.get() : null;
	}
}
