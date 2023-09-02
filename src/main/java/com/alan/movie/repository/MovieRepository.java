package com.alan.movie.repository;

import com.alan.movie.Model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
  @Query("{'$or': [{'name': { '$regex' : ?0, '$options' : 'i' }}, {'code': { '$regex' : ?1, '$options' : 'i' }}]}")
  Page<Movie> findAllByNameLikeOrCode(String name, String code, Pageable pageable);
}
