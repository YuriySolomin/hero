package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.PlayersForGroup;

import java.util.List;

@Repository
public interface PlayersForGroupRepository extends JpaRepository<PlayersForGroup, Long> {

    @Query(value = "select * from tc_players_for_group " +
            "where id_group = :idGroup " +
            "and player_status = :playerStatus", nativeQuery = true)
    List<PlayersForGroup> getAllPlayersForGroupByStatus(@Param("idGroup") Long idGroup, @Param("playerStatus") int playerStatus);

    @Query(value = "select pfg.* from " +
            "tc_players_for_group pfg join tc_players p on pfg.id_player = p.id_player " +
            "where p.gender = :gender " +
            "and pfg.player_status = :playerStatus " +
            "and pfg.id_group = :idGroup", nativeQuery = true)
    List<PlayersForGroup> getAllPlayersForGroupByStatusAndGender(@Param("idGroup") Long idGroup, @Param("playerStatus") int playerStatus
            , @Param("gender") int gender);


}
