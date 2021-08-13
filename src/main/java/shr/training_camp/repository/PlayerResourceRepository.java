package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.PlayersResources;
import shr.training_camp.core.model.database.addition.PlayerResourceStatistics;

import java.util.List;

@Repository
public interface PlayerResourceRepository extends JpaRepository<PlayersResources, Long> {

    @Query(value = "select * from tc_players_resources where id_player=:playerId and id_resource=:resourceId", nativeQuery = true)
    PlayersResources getPRByPlayerAndResource(@Param("playerId") Long playerId, @Param("resourceId") Long resourceId);

    @Query(value = "select p.nick_name as nickName, r.name as resourceName, sum(pr.quantity) as quantity from " +
            "tc_players p, tc_resources r, tc_players_resources pr " +
            "where p.id_player = pr.id_player " +
            "and r.id_resource = pr.id_resource " +
            "and p.id_player = :playerId " +
            "group by nickName, resourceName", nativeQuery = true)
    List<PlayerResourceStatistics> getPRGroupedByPlayerAndResource(@Param("playerId") Long playerId);
}
