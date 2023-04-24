package com.hearit.spotify.dto;

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
public class SpotifyTrackListDTO {
	
	private String href;
	private Integer limit;
	private String next;
	private String previous;
	private Integer offset;
	private Integer total;
	private SpotifyTrackDTO[] items;
}
