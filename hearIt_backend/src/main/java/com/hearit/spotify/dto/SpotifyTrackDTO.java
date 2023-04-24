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
public class SpotifyTrackDTO {

	private String id;
	private String name;
	private int popularity;
	private SpotifyAlbumDTO album;
	private SpotifyArtistDTO[] artists;
}
