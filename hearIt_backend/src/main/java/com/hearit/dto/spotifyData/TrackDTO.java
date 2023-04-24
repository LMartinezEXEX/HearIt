package com.hearit.dto.spotifyData;

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
public class TrackDTO {

	private String id;
	private String name;
	private int popularity;
	private AlbumDTO album;
	private ArtistDTO[] artists;
}
