package com.alan.movie.controller;

import com.alan.movie.dto.MovieRequest;
import com.alan.movie.dto.MovieResponse;
import com.alan.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
  @Autowired
  private MovieService movieService;
  
  
  @GetMapping("")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  public ResponseEntity<Page<MovieResponse>> allMovie(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      Pageable pageable
  ) {
    return ResponseEntity.ok(movieService.allMovie(keyword, pageable));
  }
  
  @PostMapping("")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<MovieResponse> createMovie(
      @RequestBody MovieRequest movieRequest
  ) throws Exception {
    return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movieRequest));
  }
  
  
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  public ResponseEntity<MovieResponse> getMovie(
      @PathVariable String id) throws Exception {
    return ResponseEntity.ok(movieService.getMovie(id));
  }
  
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<MovieResponse> updateMovie(
      @PathVariable String id,
      @RequestBody MovieRequest movieRequest
  ) throws Exception {
    return ResponseEntity.ok(movieService.updateMovie(id, movieRequest));
  }
  
  
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Void> deleteMovie(
      @PathVariable String id
  ) throws Exception {
    movieService.deleteMovie(id);
    return ResponseEntity.ok().build();
  }
  
}
