package com.alan.movie.repository;

import com.alan.movie.Model.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends MongoRepository<Actor, String> {
  public Page<Actor> findAllByNameLike(String name, Pageable pageable);
}
