package com.hearit.spotify.service;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hearit.dto.spotifyData.AlbumDTO;
import com.hearit.dto.spotifyData.ArtistDTO;
import com.hearit.dto.spotifyData.ImageDTO;
import com.hearit.dto.spotifyData.SearchResponseDTO;
import com.hearit.dto.spotifyData.TrackDTO;
import com.hearit.dto.spotifyData.TracksResponseDTO;
import com.hearit.spotify.dto.SpotifyAlbumDTO;
import com.hearit.spotify.dto.SpotifyArtistDTO;
import com.hearit.spotify.dto.SpotifyImageDTO;
import com.hearit.spotify.dto.SpotifySearchDTO;
import com.hearit.spotify.dto.SpotifyTrackDTO;
import com.hearit.spotify.dto.SpotifyTrackListDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpotifyService {
	
	private static final String CLIENT_ID = "f7d4f571dfd34d208eb58310338ee6c3";
	
	private static final String CALLBACK = "http://localhost:8080/api/v1/spotify/auth/token";
	
	private final RestTemplate restTemplate;
	
	public String getAccesUrl() {
		return "https://accounts.spotify.com/es-ES/authorize?client_id=" + 
				CLIENT_ID + "&response_type=code&redirect_uri=" +
				URLEncoder.encode(CALLBACK) + "&expires_in=3600";
	}
	
	public SearchResponseDTO search(String query, int amount, String accessToken) {
		HttpEntity<String> entity = getHeader(accessToken);
		ResponseEntity<SpotifySearchDTO> data = restTemplate.exchange(
				"https://api.spotify.com/v1/search?q=" + query + "&type=track&limit=" + amount, 
				HttpMethod.GET, 
				entity,
				SpotifySearchDTO.class
		);
		
		return processApiResponse(data);
	}
	
	public TracksResponseDTO searchTrackIds(Set<String> trackIds, String accessToken) {
		String processedIds = processIds(trackIds);
		
		HttpEntity<String> entity = getHeader(accessToken);
		ResponseEntity<TracksResponseDTO> data = restTemplate.exchange(
				"https://api.spotify.com/v1/tracks?ids=" + processedIds, 
				HttpMethod.GET, 
				entity,
				TracksResponseDTO.class
		);
		
		return TracksResponseDTO.builder()
				.tracks(removeInvalidTracks(data.getBody().getTracks()))
				.build();
	}
	
	private String processIds(Set<String> ids) {
		return String.join(",", ids);
	}
	
	private TrackDTO[] removeInvalidTracks(TrackDTO[] tracks) {
		return Arrays.stream(tracks).filter(t -> Objects.nonNull(t)).toArray(TrackDTO[]::new);
	}
	
	private HttpEntity<String> getHeader(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		return new HttpEntity<String>("", headers);
	}
	
	private SearchResponseDTO processApiResponse(ResponseEntity<SpotifySearchDTO> apiResponse) {
		if (!apiResponse.getStatusCode().equals(HttpStatus.OK)) {
			return null;
		}
		
		SpotifyTrackListDTO data = apiResponse.getBody().getTracks();
		return SearchResponseDTO.builder()
				.total(data.getLimit())
				.tracks(
						removeInvalidTracks(
								buildTrackDTOList(data.getItems())
						)
				)
				.build();
	}
	
	private TrackDTO[] buildTrackDTOList(SpotifyTrackDTO[] tracksDTO) {
		TrackDTO[] tracks = new TrackDTO[tracksDTO.length];
		ArtistDTO[] artists = null;
		for (int track_idx = 0; track_idx < tracksDTO.length; ++track_idx) {	
			SpotifyTrackDTO t = tracksDTO[track_idx];
			
			if (Objects.nonNull(t.getArtists())) {
				artists = buildArtistDTOArray(t.getArtists());
			}
			
			TrackDTO track = TrackDTO.builder()
					.id(t.getId())
					.name(t.getName())
					.popularity(t.getPopularity())
					.artists(artists)
					.album(buildAlbumDTO(t.getAlbum()))
					.build();
			tracks[track_idx] = track;
		}
		
		return tracks;
	}
	
	private ArtistDTO[] buildArtistDTOArray(SpotifyArtistDTO[] artistsDTO) {
		ArtistDTO[] artists = new ArtistDTO[artistsDTO.length];
		ImageDTO[] images = null;
		for (int arti_idx = 0; arti_idx < artistsDTO.length; ++arti_idx) {
			
			SpotifyArtistDTO a = artistsDTO[arti_idx];
			if (Objects.nonNull(a.getImages())) {
				images = buildImageDTOArray(a.getImages());
			}
			
			ArtistDTO artist = ArtistDTO.builder()
					.name(a.getName())
					.images(images)
					.build();
			artists[arti_idx] = artist;
		}
		
		return artists;
	}
	
	private AlbumDTO buildAlbumDTO(SpotifyAlbumDTO albumDTO) {
		ImageDTO[] images = null;
		if (Objects.nonNull(albumDTO.getImages())) {
			images = buildImageDTOArray(albumDTO.getImages());
		}
	
		return AlbumDTO.builder()
				.name(albumDTO.getName())
				.images(images)
				.build();
	}
	
	private ImageDTO[] buildImageDTOArray(SpotifyImageDTO[] imagesDTO) {
		ImageDTO[] images = new ImageDTO[imagesDTO.length];
		for (int img_idx = 0; img_idx < imagesDTO.length; ++img_idx) {
			SpotifyImageDTO i = imagesDTO[img_idx];
			ImageDTO image = ImageDTO.builder()
					.url(i.getUrl())
					.width(i.getWidth())
					.height(i.getHeight())
					.build();
			images[img_idx] = image;
		}
		
		return images;
	}
}
