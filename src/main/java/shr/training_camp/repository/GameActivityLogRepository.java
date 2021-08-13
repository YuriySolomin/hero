package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.GameActivityLog;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;

import java.sql.Date;
import java.util.List;

@Repository
public interface GameActivityLogRepository extends JpaRepository<GameActivityLog, Long> {

    @Query(value = "select gal.id_player as idPlayer, p.nick_name as nickName, gal.id_activity as idActivity, a.name as activityName, " +
            "sum(gal.value) as value, sum(gal.bonus_value) as bonusValue, sum(gal.random_value) as randomValue " +
            "from tc_game_activity_log gal join tc_activities a on gal.id_activity = a.id_activity join tc_players p on gal.id_player = p.id_player " +
            "where gal.save_date <= :endDate " +
            "group by 1,2,3,4 " +
            "order by 5 desc", nativeQuery = true)
    List<GameActivityStatistics> getGameActivityStatistics(@Param("endDate") Date endDate);

    @Query(value = "select gal.id_player as idPlayer, p.nick_name as nickName, gal.id_activity as idActivity, a.name as activityName, " +
            "sum(gal.value) as value, sum(gal.bonus_value) as bonusValue, sum(gal.random_value) as randomValue " +
            "from tc_game_activity_log gal join tc_activities a on gal.id_activity = a.id_activity join tc_players p on gal.id_player = p.id_player " +
            "where gal.save_date <= :endDate " +
            "and gal.id_player = :playerId " +
            "group by 1,2,3,4 " +
            "order by 5 desc", nativeQuery = true)
    List<GameActivityStatistics> getGameActivityStatistics(@Param("endDate") Date endDate, @Param("playerId") Long playerId);

    @Query(value = "select gal.id_player as idPlayer, p.nick_name as nickName, gal.id_activity as idActivity, a.name as activityName, " +
            "sum(gal.value) as value, sum(gal.bonus_value) as bonusValue, sum(gal.random_value) as randomValue " +
            "from tc_game_activity_log gal join tc_activities a on gal.id_activity = a.id_activity join tc_players p on gal.id_player = p.id_player " +
            "where gal.save_date <= :endDate " +
            "and gal.id_activity = :activityId " +
            "group by 1,2,3,4 " +
            "order by 5 desc", nativeQuery = true)
    List<GameActivityStatistics> getGameActivityStatisticsByActivity(@Param("endDate") Date endDate, @Param("activityId") Long activityId);

    @Query(value = "select gal.id_player as idPlayer, p.nick_name as nickName, gal.id_activity as idActivity, a.name as activityName, " +
            "sum(gal.value) as value, sum(gal.bonus_value) as bonusValue, sum(gal.random_value) as randomValue " +
            "from tc_game_activity_log gal join tc_activities a on gal.id_activity = a.id_activity join tc_players p on gal.id_player = p.id_player " +
            "where id_group = :groupId " +
            "group by 1,2,3,4 " +
            "order by 5 desc", nativeQuery = true)
    List<GameActivityStatistics> getGameActivityStatisticsByActivity(@Param("groupId") Long groupId);
}
