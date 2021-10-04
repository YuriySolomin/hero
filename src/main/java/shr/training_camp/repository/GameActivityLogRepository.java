package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.GameActivityLog;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;

import java.sql.Date;
import java.time.LocalDate;
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
            "and gal.id_group = :groupId " +
            "group by 1,2,3,4 " +
            "order by 5 desc", nativeQuery = true)
    List<GameActivityStatistics> getGameActivityStatistics(@Param("endDate") Date endDate, @Param("playerId") Long playerId, @Param("groupId") Long groupId);

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

    @Query(value = "select max(gal.save_date) as max_save_date " +
            "from tc_game_activity_log gal " +
            "where gal.id_group = :groupId", nativeQuery = true)
    LocalDate getMaxDateGameActivityLogForGroup(@Param("groupId") Long groupId);

    @Query(value = "select aa.id_activity as idActivity, aa.id_player as idPlayer, aa.a_first as aFirst, " +
            "aa.ratio as ratio, sum(gal.value) as value, sum(gal.random_value) as randomVale, " +
            "sum(gal.bonus_value) as bonusValue " +
            "from tc_auto_activities aa join tc_game_activity_log gal on aa.id_activity = gal.id_activity  " +
            "and aa.id_group = gal.id_group " +
            "and aa.id_player = gal.id_player " +
            "where aa.id_group = :groupId " +
            "and aa.id_player = :playerId " +
            "group by 1, 2, 3, 4", nativeQuery = true)
    List<GameActivityStatistics> getGameActivityStatisticsByGroupAndPlayer(@Param("groupId") Long groupId,
                                                                           @Param("playerId") Long playerId);

}
