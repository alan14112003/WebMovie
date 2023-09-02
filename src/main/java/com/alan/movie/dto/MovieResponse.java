package com.alan.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {
  private String id;
  private String name;
  private String code;
  private String avatar;
  private String video;
  private ActorInMovieResponse actor;
}
