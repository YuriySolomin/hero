package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.Player;

import java.util.List;

@Repository
public interface   PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = "select * from tc_players where player_type = :plType", nativeQuery = true)
    List<Player> findPlayersByType(@Param("plType") Integer plType);

    @Query(value = "select * from tc_players where nick_name = :nickName", nativeQuery = true)
    Player findPlayerByNickName(@Param("nickName") String nickName);

    @Query(value = "select * from tc_players where nick_name like :nameMask", nativeQuery = true)
    List<Player> findAllPlayersByNickNameMask(@Param("nameMask") String nameMask);

}
