package com.alan.movie.service;

import com.alan.movie.Model.Actor;
import com.alan.movie.dto.ActorRequest;
import com.alan.movie.dto.ActorResponse;
import com.alan.movie.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ActorService {
  @Autowired
  private ActorRepository actorRepository;
  
  public Page<ActorResponse> allActor(String keyword, Pageable pageable) {
    Page<Actor> actorPage = actorRepository.findAllByNameLike(keyword, pageable);
    return actorPage.map(this::getActorResponse);
  }
  
  private ActorResponse getActorResponse(Actor actor) {
    return ActorResponse.builder()
        .id(actor.getId())
        .name(actor.getName())
        .avatar(actor.getAvatar())
        .description(actor.getDescription())
        .movies(actor.getMovies().stream().map(MovieService::getMovieResponse).toList())
        .build();
  }
  
  public ActorResponse getActor(String id) throws Exception {
    Actor actor = actorRepository.findById(id).orElseThrow(() -> new Exception("Actor not found " +
        "with " +
        "ID: " + id));
    
    return getActorResponse(actor);
  }
  
  public ActorResponse createActor(ActorRequest actorRequest) {
    Actor actor = Actor.builder()
        .name(actorRequest.getName())
        .avatar(actorRequest.getAvatar())
        .description(actorRequest.getDescription())
        .movies(new ArrayList<>())
        .build();
    
    actorRepository.save(actor);
    
    return ActorResponse.builder()
        .id(actor.getId())
        .name(actor.getName())
        .avatar(actor.getAvatar())
        .description(actor.getDescription())
        .build();
  }
  
  public ActorResponse updateActor(String id, ActorRequest actorRequest) throws Exception {
    Actor actor = actorRepository.findById(id).orElseThrow(
        () -> new Exception("Actor not found with ID: " + id)
    );
    actor.setName(actorRequest.getName());
    actor.setAvatar(actorRequest.getAvatar());
    actor.setDescription(actorRequest.getDescription());
    
    actorRepository.save(actor);
    
    return getActorResponse(actor);
  }
  
  public void deleteActor(String id) {
      actorRepository.deleteById(id);
  }
  
}
