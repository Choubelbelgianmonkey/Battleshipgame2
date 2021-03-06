package com.example.Salvo;

import java.util.List;
import java.lang.String;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(@Param("username") String password);

}



