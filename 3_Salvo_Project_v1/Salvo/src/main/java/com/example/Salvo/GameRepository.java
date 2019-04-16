package com.example.Salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;



@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {

        List<Game> findByDate(Date date);

}