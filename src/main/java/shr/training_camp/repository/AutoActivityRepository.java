package shr.training_camp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.AutoActivity;

import java.util.List;

@Repository
public interface AutoActivityRepository extends JpaRepository<AutoActivity, Long> {

    @Query(value = "select * from tc_auto_activities where id_player = :playerId", nativeQuery = true)
    List<AutoActivity> findAllPlayerAutoActivities(@Param("playerId") Long playerId);

    /*@Query("select aa.* from tc_auto_activities aa where join tc_players p on aa.id_player = p.id_player " +
            "join tc_players_groups pg on p.id_player = pg.id_player " +
            "where id_group = :groupId")
    Page<AutoActivity> findAllPlayerAutoActivitiesByGroup(Pageable pageable, @Param("groupId") Long groupId);
*/
    @Query(value = "select aa.* from tc_auto_activities aa join tc_players p on aa.id_player = p.id_player " +
            "join tc_players_groups pg on p.id_player = pg.id_player " +
            "where pg.id_group = :groupId " +
            "and aa.id_activity = :activityId", nativeQuery = true)
    Page<AutoActivity> findAllPlayerAutoActivitiesByGroupAndActivity(Pageable pageable, @Param("groupId") Long groupId, @Param("activityId") Long activityId);

    @Query(value = "select * from tc_auto_activities " +
            "where id_player = :playerId " +
            "and id_activity = :activityId", nativeQuery = true)
    AutoActivity findAutoActivityForPlayer(@Param("playerId") Long playerId, @Param("activityId") Long activityId);

    @Query(value = "select a.id_activity, a.name, a.activity_type, a.description, a.activity_filter, a.virtual_text " +
            "from tc_activities a, tc_players p, tc_auto_activities aa " +
            "where p.id_player = :playerId " +
            "and p.id_player = aa.id_player " +
            "and a.id_activity = aa.id_activity", nativeQuery = true)
    List<Activity> getActivitiesArePresentInTheAA(@Param("playerId") Long playerId);






}
