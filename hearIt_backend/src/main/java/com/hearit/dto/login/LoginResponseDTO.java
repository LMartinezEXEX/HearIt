package com.hearit.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginResponseDTO {
	
	private int id;
	private String token;
	private String spotifyCode;
	private String accessUrl;
}
