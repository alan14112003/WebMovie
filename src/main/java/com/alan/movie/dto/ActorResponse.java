package com.alan.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActorResponse {
  private String id;
  private String name;
  private String avatar;
  private String description;
  private List<MovieResponse> movies;
}
