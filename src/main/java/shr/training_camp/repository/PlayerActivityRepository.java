package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import shr.training_camp.core.model.database.PlayerActivities;
import shr.training_camp.core.model.database.addition.HeroActivityResult;
import shr.training_camp.core.model.database.addition.PlayerActivityStatistics;

import java.sql.Date;
import java.util.List;

@Controller
public interface PlayerActivityRepository extends JpaRepository<PlayerActivities, Long> {

    @Query(value = "select ta.id_player as idPlayer, ta.id_activity as idActivity, sum(ta.quantity) as quantity, " +
            "a.name as activityName, p.nick_name as playerNickName " +
            "from tc_players_activities ta, tc_players p, tc_activities a " +
            "where ta.id_player = :playerId " +
            "and ta.id_player = p.id_player " +
            "and ta.id_activity = a.id_activity " +
            "group by ta.id_player, ta.id_activity, activityName, playerNickName", nativeQuery = true)
    List<PlayerActivityStatistics> findGroupedByAll(@Param("playerId") Long playerId);

    @Query(value = "select id_activity as idActivity, sum(quantity) as quantity " +
            "from tc_players_activities where " +
            "id_activity = :activityId " +
            // "and start_date >= ':startDate' " +
            "group by id_activity", nativeQuery = true)
    HeroActivityResult getGroupedQuantityByActivity(@Param("activityId") Long activityId);

    @Query(value = "select id_activity as idActivity, sum(quantity) as quantity " +
            "from tc_players_activities where " +
            "id_activity = :activityId " +
            "and start_date >= ':startDate' " +
            "and activity_filter = 0 " +
            "group by id_activity", nativeQuery = true)
        //HeroActivityResult getGroupedQuantityByActivity(@Param("activityId") Long activityId, @Param("startDate") Date startDate);
    List<PlayerActivityStatistics> getGroupedQuantityByActivity(@Param("activityId") Long activityId, @Param("startDate") Date startDate);

    @Query(value = "select ta.id_activity as idActivity, sum(quantity) as quantity, a.name as activityName " +
            "from tc_players_activities ta, tc_activities a where " +
            // "id_activity = :activityId " +
            "ta.id_player = :playerId " +
            "and ta.id_activity = a.id_activity " +
            "and ta.start_date >= :startDate " +
            "and activity_filter = 0 " +
            "group by ta.id_activity, activityName", nativeQuery = true)
    List<PlayerActivityStatistics> getGroupedQuantityByActivityAndDate(@Param("playerId") Long playerId, @Param("startDate") Date startDate);

    @Query(value = "select ta.id_activity as idActivity, sum(quantity) as quantity, a.name as activityName " +
            "from tc_players_activities ta, tc_activities a where " +
            // "id_activity = :activityId " +
            "ta.id_activity = :activityId " +
            "and ta.id_activity = a.id_activity " +
            "and ta.start_date >= :startDate " +
            "and activity_filter = 0 " +
            "group by ta.id_activity, activityName", nativeQuery = true)
    List<PlayerActivityStatistics> getGroupedQuantityByActivityAndDate2(@Param("activityId") Long activityId, @Param("startDate") Date startDate);


    @Query(value = "select ta.id_player as idPlayer, ta.id_activity as idActivity, sum(ta.quantity) as quantity, " +
            "a.name as activityName, p.nick_name as playerNickName " +
            "from tc_players_activities ta, tc_players p, tc_activities a " +
            "where ta.id_player = :playerId " +
            "and ta.id_activity = :activityId " +
            "and ta.id_player = p.id_player " +
            "and ta.id_activity = a.id_activity " +
            "and (ta.start_date between :startDate AND :endDate) " +
            "group by ta.id_player, ta.id_activity, activityName, playerNickName", nativeQuery = true)
    List<PlayerActivityStatistics> getGroupedByAllParameters(@Param("playerId") Long playerId, @Param("activityId") Long activityId,
                                                             @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(value = "select ta.id_activity as idActivity, a.name as activityName, sum(ta.quantity) as quantity " +
            "from tc_players_activities ta, tc_activities a " +
            "where ta.id_player = :playerId " +
            "and ta.id_activity = a.id_activity " +
            "and start_date >= CURDATE() " +
            "and activity_filter = 0 " +
            "group by ta.id_activity, a.name", nativeQuery = true)
    List<PlayerActivityStatistics> getGroupedActivitiesForToday(@Param("playerId") Long playerId);

    @Query(value = "select * from tc_players_activities where is_used = :isUsed", nativeQuery = true)
    List<PlayerActivities> getPlayersActivitiesFilteredByUsed(@Param("isUsed") Integer isUsed);

    @Query(value = "select pa.start_date as startDate, pa.id_player as idPlayer, p.nick_name as playerNickName, pa.id_activity as idActivity, " +
            "a.name as activityName,  DATEDIFF(pa.start_date, :startDate) + 1 as period, sum(pa.quantity) as quantity " +
            "from tc_players_activities pa join tc_players p on pa.id_player = p.id_player join tc_activities a on pa.id_activity = a.id_activity " +
            "where p.id_player = :playerId " +
            "and start_date >= :saveDate " +
            "and start_date < :endDate " +
            "group by 1, 2, 3, 4, 5, 6 " +
            "order by 1",
            nativeQuery = true)
    List<PlayerActivityStatistics> getRealPlayerActivityStatistic(@Param("playerId") Long playerId, @Param("startDate") Date startDate, @Param("saveDate") Date saveDate, @Param("endDate") Date endDate);
}
