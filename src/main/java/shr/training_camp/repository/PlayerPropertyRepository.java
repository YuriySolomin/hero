package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.PlayerProperty;

@Repository
public interface PlayerPropertyRepository extends JpaRepository<PlayerProperty, Long> {

    @Query(value = "select * from tc_players_properties where id_player = :idPlayer and id_property = :idProperty", nativeQuery = true)
    PlayerProperty getPPByPlayerAndProperty(@Param("idPlayer") Long idPlayer, @Param("idProperty") Long idProperty);
}
