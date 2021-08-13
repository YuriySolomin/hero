package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.GameActivityGrowLog;
import shr.training_camp.core.model.database.addition.GameActivityGrowStatistics;

import java.util.List;

@Repository
public interface GameActivityGrowLogRepository extends JpaRepository<GameActivityGrowLog, Long> {

    @Query(value = "select p.gender as gender, p.id_player as idPlayer, p.player_type as playerType, p.nick_name as nickName, " +
            "gagl.grow as grow, gagl.factor as factor, gagl.convert_date, as converDate, gagl.factor2 as factor2, " +
            "(select count(*) + 1 from tc_game_activity_grow_log gagl_1, tc_players p_1 " +
            "where gagl_1.id_player <> gagl.id_player and gagl_1.id_player = p_1.id_player " +
            "and gagl_1.convert_date = (select max(gag2.convert_date) from tc_game_activity_grow_log gag2) " +
            "and gagl_1.factor > gagl.factor) as place " +
            "from tc_game_activity_grow_log gagl join tc_players p on gagl.id_player = p.id_player " +
            "where gagl.convert_date = (select max(gag2.convert_date) from tc_game_activity_grow_log gag2)",
            nativeQuery = true)
    List<GameActivityGrowStatistics> getGrowLastDateStatistic();

}
