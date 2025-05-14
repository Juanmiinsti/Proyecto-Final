package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMatchRepository extends JpaRepository<Match, Integer> {
    @Query("SELECT m FROM Match m INNER JOIN m.userWinner u WHERE u.name = ?1")
    List<Match>winnedmatchsbyUsername(String username);
    @Query("SELECT m FROM Match m INNER JOIN m.userLoser u WHERE u.name = ?1")
    List<Match>lostmatchsbyName(String name);
    @Query(value =
            "select m.*" +
            "from  `match` m \n" +
            "inner join user u on u.id= m.user_loser_id or u.id= m.user_winner_id \n" +
            "where u.name =?1",nativeQuery = true)
    List<Match>matchesbyuUsername(String name);


}
