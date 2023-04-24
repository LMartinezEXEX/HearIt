package com.hearit.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.hearit.dto.login.LoginRequestDTO;
import com.hearit.dto.login.LoginResponseDTO;
import com.hearit.dto.register.RegisterRequestDTO;
import com.hearit.dto.register.RegisterResponseDTO;
import com.hearit.service.AuthService;
import com.hearit.service.JwtService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthService authService;
	
	@MockBean
	private JwtService jwtService;
	
	private final static String API_URL = "http://localhost:8080/api/v1/auth";
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void successfullyCreateSingleUser() throws Exception {
		RegisterRequestDTO payload = RegisterRequestDTO.builder()
				.username("TestUser")
				.password("12345678")
				.build();
		
		RegisterResponseDTO expected = RegisterResponseDTO.builder()
				.id(0)
				.response("User created successfully.")
				.build();
		
		when(authService.register(payload)).thenReturn(expected);
		
		mockMvc.perform(
				post(API_URL + "/register")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(payload))
				.accept(MediaType.APPLICATION_JSON_VALUE)
		).andExpect(
				status().isOk()
		).andExpect(
				content().string(equalTo(mapper.writeValueAsString(expected)))
		);
	}
	
	@Test
	public void createUsersWithTakenUsername() throws Exception {
		RegisterRequestDTO payload = RegisterRequestDTO.builder()
				.username("TestUser")
				.password("12345678")
				.build();
		
		when(authService.register(payload)).thenReturn(null);
		
		mockMvc.perform(
				post(API_URL + "/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(payload))
				.accept(MediaType.APPLICATION_JSON)
		)
		.andExpect(
				status().isConflict()
		);
	}
	
	@Test
	public void successfulLogin() throws Exception {
		LoginRequestDTO payload = new LoginRequestDTO().builder()
				.username("TestUSer")
				.password("12345678")
				.build();
		LoginResponseDTO expected = new LoginResponseDTO().builder()
				.id(0)
				.token("aaaaaaa.eeeeeeeeee.ccccccccc")
				.accessUrl("url")
				.build();
		when(authService.authenticate(payload)).thenReturn(expected);
		
		mockMvc.perform(
				post(API_URL + "/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(payload))
				.accept(MediaType.APPLICATION_JSON)
		).andExpect(
				status().isOk()
		).andExpect(
				content().string(equalTo(mapper.writeValueAsString(expected)))
		);
	}
	
	@Test
	public void badCredentialsLogin() throws Exception {
		LoginRequestDTO payload = new LoginRequestDTO().builder()
				.username("TestUSer")
				.password("12345678")
				.build();
		
		when(authService.authenticate(any(LoginRequestDTO.class))).thenReturn(null);
		
		mockMvc.perform(
				post(API_URL + "/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(payload))
				.accept(MediaType.APPLICATION_JSON)
		).andExpect(
				status().isBadRequest()
		);
	}
}
