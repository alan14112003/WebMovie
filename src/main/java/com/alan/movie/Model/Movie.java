package com.alan.movie.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Movie {
  private String id;
  private String name;
  private String code;
  private String avatar;
  private String video;
  @DocumentReference
  private Actor actor;
}
