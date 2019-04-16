package com.example.Salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Date;
import java.lang.Long;



@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

    List<GamePlayer> findByGame(Long GamePlayer);


}
