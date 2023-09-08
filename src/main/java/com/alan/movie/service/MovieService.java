package com.alan.movie.service;

import com.alan.movie.Model.Actor;
import com.alan.movie.Model.Movie;
import com.alan.movie.dto.ActorInMovieResponse;
import com.alan.movie.dto.MovieRequest;
import com.alan.movie.dto.MovieResponse;
import com.alan.movie.repository.ActorRepository;
import com.alan.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
  @Autowired
  private MovieRepository movieRepository;
  
  
  @Autowired
  private ActorRepository actorRepository;
  
  public static MovieResponse getMovieResponse(Movie movie) {
    ActorInMovieResponse actorInMovieResponse =
        ActorInMovieResponse.builder()
            .id(movie.getActor().getId())
            .name(movie.getActor().getName())
            .avatar(movie.getActor().getAvatar())
            .build()
        ;
    
    return MovieResponse.builder()
        .id(movie.getId())
        .name(movie.getName())
        .code(movie.getCode())
        .avatar(movie.getAvatar())
        .video(movie.getVideo())
        .actor(actorInMovieResponse)
        .build();
  }
  
  public Page<MovieResponse> allMovie(String keyword, Pageable pageable) {
    Page<Movie> moviePage = movieRepository.findAllByNameLikeOrCode(keyword, keyword, pageable);
    
    return moviePage.map(MovieService::getMovieResponse);
  }
  
  public MovieResponse getMovie(String id) throws Exception {
    Movie movie =
        movieRepository.findById(id).orElseThrow(() -> new Exception("Movie not found " + "with ID:" + id));
    ActorInMovieResponse actorInMovieResponse =
        ActorInMovieResponse.builder().id(movie.getActor().getId()).name(movie.getActor().getName()).avatar(movie.getActor().getAvatar()).build();
    
    return MovieResponse.builder().id(movie.getId()).name(movie.getName()).code(movie.getCode()).avatar(movie.getAvatar()).video(movie.getVideo()).actor(actorInMovieResponse).build();
  }
  
  public MovieResponse createMovie(MovieRequest movieRequest) throws Exception {
    Actor actor =
        actorRepository.findById(movieRequest.getActorId()).orElseThrow(() -> new Exception("Actor not found with ID:" + movieRequest.getActorId()));
    
    Movie movie =
        Movie.builder().name(movieRequest.getName()).code(movieRequest.getCode()).avatar(movieRequest.getAvatar()).video(movieRequest.getVideo()).actor(actor).build();
    
    movieRepository.save(movie);
    actor.getMovies().add(movie);
    actorRepository.save(actor);
    
    return getMovieResponse(movie);
  }
  
  public MovieResponse updateMovie(String id, MovieRequest movieRequest) throws Exception {
    Movie movie =
        movieRepository.findById(id).orElseThrow(() -> new Exception("Movie not found with ID: " + id));
    movie.setName(movieRequest.getName());
    movie.setCode(movieRequest.getCode());
    movie.setAvatar(movieRequest.getAvatar());
    movie.setVideo(movieRequest.getVideo());
    
    if (!movie.getActor().getId().equals(movieRequest.getActorId())) {
      Actor actorOld =
          actorRepository.findById(movie.getActor().getId()).orElseThrow(() -> new Exception("Actor not found with ID: " + id));
      Actor actorNew =
          actorRepository.findById(movieRequest.getActorId()).orElseThrow(() -> new Exception("Actor not found with ID: " + id));
      
      actorOld.getMovies().remove(movie);
      actorNew.getMovies().add(movie);
      actorRepository.save(actorOld);
      actorRepository.save(actorNew);
      
      movie.setActor(actorNew);
    }
    
    movieRepository.save(movie);
    
    return getMovieResponse(movie);
  }
  
    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }
  
}
