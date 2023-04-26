package com.hearit.controller;

import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearit.dto.spotifyData.TrackDTO;
import com.hearit.dto.spotifyData.TracksResponseDTO;
import com.hearit.dto.user.SpotifyUserCodeDTO;
import com.hearit.dto.user.TrackIdRequestDTO;
import com.hearit.service.JwtService;
import com.hearit.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private JwtService jwtService;
	
	private final static String API_URL = "http://localhost:8080/api/v1/user";
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void successfullAddTrack() throws Exception {
		TrackIdRequestDTO payload = TrackIdRequestDTO.builder()
				.id("asdasdasd")
				.build();
		
		mockMvc.perform(
				put(API_URL + "/tracks")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(payload))
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isNoContent()
		);
	}
	
	@Test
	public void successfullDeleteTrack() throws Exception {
		TrackIdRequestDTO payload = TrackIdRequestDTO.builder()
				.id("asdasdasd")
				.build();
		
		mockMvc.perform(
				delete(API_URL + "/tracks")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(payload))
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isNoContent()
		);
	}
	
	@Test
	public void getAllWhenEmptyFavoriteTracks() throws Exception {
		
		when(userService.getAllFavoritesTracks(any(String.class), any(Authentication.class)))
			.thenReturn(null);
		
		mockMvc.perform(
				get(API_URL + "/tracks").header("spotify_access_token", "BQAasdasASDS")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isNoContent()
		);
	}
	
	@Test
	public void getAllFavoriteTracksWhenNonEmpty() throws Exception {
		TrackDTO t_1 = TrackDTO.builder()
				.id("0nrRP2bk19rLc0orkWPQk2")
				.name("Waka Waka")
				.popularity(34)
				.album(null)
				.artists(null)
				.build();
		
		TrackDTO t_2 = TrackDTO.builder()
				.id("5xjhX5AWqLvz9ZahKqApLZ")
				.name("Hello")
				.popularity(98)
				.album(null)
				.artists(null)
				.build();
		
		TracksResponseDTO expected = TracksResponseDTO.builder()
				.tracks(new TrackDTO[] {t_1, t_2})
				.build();
		
		when(userService.getAllFavoritesTracks(any(String.class), eq(null)))
			.thenReturn(expected);
		
		mockMvc.perform(
				get(API_URL + "/tracks").header("spotify_access_token", "BQAasdasASDS")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isOk()
		).andExpect(
				content().string(equalTo(mapper.writeValueAsString(expected)))
		);
	}
	
	@Test
	public void getTracksWhenHavingOnlyInvalidIds() throws Exception {
		TracksResponseDTO expected = TracksResponseDTO.builder()
				.tracks(new TrackDTO[] { null })
				.build();
		
		when(userService.getAllFavoritesTracks(any(String.class), any(Authentication.class)))
			.thenReturn(expected);
		
		mockMvc.perform(
				get(API_URL + "/tracks").header("spotify_access_token", "BQAasdasASDS")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isNoContent()
		);
	}
	
	@Test
	public void successfulSaveUserSpotifyCode() throws Exception {
		SpotifyUserCodeDTO payload = SpotifyUserCodeDTO.builder()
				.spotifyCode("testCode")
				.build();
		
		mockMvc.perform(
				post(API_URL + "/spotifyCode")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(payload))
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isNoContent()
		);
	}
}
