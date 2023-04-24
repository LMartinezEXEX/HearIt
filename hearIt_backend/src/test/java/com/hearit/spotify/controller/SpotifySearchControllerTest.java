package com.hearit.spotify.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearit.dto.spotifyData.SearchResponseDTO;
import com.hearit.dto.spotifyData.TrackDTO;
import com.hearit.service.JwtService;
import com.hearit.spotify.service.SpotifyService;


@WebMvcTest(SpotifySearchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SpotifySearchControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean 
	private SpotifyService spotifyService;
	
	@MockBean
	private JwtService jwtService;
	
	private final static String API_URL = "http://localhost:8080/api/v1/spotify/search";
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void searchWithValidQueryAndAmount() throws Exception {
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
		
		SearchResponseDTO expected = SearchResponseDTO.builder()
				.tracks(new TrackDTO[] {t_1, t_2})
				.total(2)
				.build();
		
		when(spotifyService.search(any(String.class), any(Integer.class), any(String.class)))
			.thenReturn(expected);
		
		mockMvc.perform(
				get(API_URL)
				.header("spotify_access_token", "BQAasdasASDS")
				.queryParam("q", "Avicii")
				.queryParam("amount", "2")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isOk()
		).andExpect(
				content().string(equalTo(mapper.writeValueAsString(expected)))
		);
	}
	
	@Test
	public void searchWhenHavingOnlyInvalidIds() throws Exception {
		SearchResponseDTO expected = SearchResponseDTO.builder()
				.tracks(new TrackDTO[] {})
				.total(0)
				.build();
		
		when(spotifyService.search(any(String.class), any(Integer.class), any(String.class)))
			.thenReturn(expected);
		
		mockMvc.perform(
				get(API_URL)
				.header("spotify_access_token", "BQAasdasASDS")
				.queryParam("q", "Avicii")
				.queryParam("amount", "2")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isNoContent()
		);
	}
	
	@Test
	public void searchWhenSpotifyApiErrorIsThrown() throws Exception {
		
		when(spotifyService.search(any(String.class), any(Integer.class), any(String.class)))
			.thenReturn(null);
		
		mockMvc.perform(
				get(API_URL)
				.header("spotify_access_token", "BQAasdasASDS")
				.queryParam("q", "Avicii")
				.queryParam("amount", "5")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isNoContent()
		);
	}
	
	@Test
	public void searchWhenAmountIsNotAnInteger() throws Exception {
		
		when(spotifyService.search(any(String.class), any(Integer.class), any(String.class)))
			.thenReturn(null);
		
		mockMvc.perform(
				get(API_URL)
				.header("spotify_access_token", "BQAasdasASDS")
				.queryParam("q", "Avicii")
				.queryParam("amount", "cuarenta")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isBadRequest()
		);
	}
}
