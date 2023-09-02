package com.alan.movie.controller;

import com.alan.movie.dto.ActorRequest;
import com.alan.movie.dto.ActorResponse;
import com.alan.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {
  @Autowired
  private ActorService actorService;
  
  
  @GetMapping("")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  public ResponseEntity<Page<ActorResponse>> allActor(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      Pageable pageable
  ) {
    return ResponseEntity.ok(actorService.allActor(keyword, pageable));
  }
  
  @PostMapping("")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ActorResponse> createActor(
      @RequestBody ActorRequest actorRequest
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(actorService.createActor(actorRequest));
  }
  
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  public ResponseEntity<ActorResponse> getActor(
      @PathVariable String id) throws Exception {
    return ResponseEntity.ok(actorService.getActor(id));
  }
  
  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ActorResponse> updateActor(
      @PathVariable String id,
      @RequestBody ActorRequest actorRequest
  ) throws Exception {
    return ResponseEntity.ok(actorService.updateActor(id, actorRequest));
  }
  
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Void> deleteActor(
      @PathVariable String id
  ) throws Exception {
    actorService.deleteActor(id);
    return ResponseEntity.ok().build();
  }
}
