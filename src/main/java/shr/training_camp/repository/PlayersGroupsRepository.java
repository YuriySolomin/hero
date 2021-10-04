package shr.training_camp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.addition.AutoActivitiesPlayerGroups;
import shr.training_camp.core.model.database.addition.PlayerRandomChoice;
import shr.training_camp.core.model.database.addition.PlayersForGeneration;

import java.util.List;

public interface PlayersGroupsRepository extends JpaRepository<PlayersGroups, Long> {

    @Query(value = "select * from tc_players_groups where id_group = :groupId", nativeQuery = true)
    List<PlayersGroups> findByGroupId(@Param("groupId") Long groupId);

    @Query(value = "select * from tc_players_groups where id_group = :groupId " +
            "and is_hero = :isHero", nativeQuery = true)
    List<PlayersGroups> findByGroupId(@Param("groupId") Long groupId, @Param("isHero") int isHero);

    @Query(value = "select * from tc_players_groups where id_group = :groupId " +
            "and is_hero = :isHero and active_status > 0", nativeQuery = true)
    List<PlayersGroups> findActivePlayersByGroupId(@Param("groupId") Long groupId, @Param("isHero") int isHero);

    @Query(value = "select * from tc_players_groups where id_group = :groupId and id_player = :playerId", nativeQuery = true)
    List<PlayersGroups> findByGroupAndPlayerId(@Param("groupId") Long groupId, @Param("playerId") Long playerId);

    @Query(value = "select p.id_player as idPlayer, p.nick_name as nickName, p.gender as gender, p.player_type as playerType, pg.active_status as activeStatus, pg.actual_date as actualDate " +
            "from tc_players_groups pg join tc_players p on pg.id_player = p.id_player " +
            "where pg.id_group = :groupId and p.gender = :gender " +
            "and pg.is_hero = 0 and pg.active_status > 0", nativeQuery = true)
    List<PlayerRandomChoice> findPlayerInTheGroupByGender(@Param("groupId") Long groupId, @Param("gender") int gender);

    @Query(value = "select p.id_player as idPlayer, p.nick_name as nickName, p.gender as gender, p.player_type as playerType, pg.active_status as activeStatus, pg.actual_date as actualDate " +
            "from tc_players_groups pg join tc_players p on pg.id_player = p.id_player " +
            "where pg.id_group = :groupId and p.gender = :gender and p.player_type = :playerType " +
            "and pg.is_hero = 0 and pg.active_status > 0", nativeQuery = true)
    List<PlayerRandomChoice> findPlayerInTheGroupByGenderAndType(@Param("groupId") Long groupId, @Param("gender") int gender, @Param("playerType") int playerType);

    @Query(value = "select p.id_player as idPlayer, p.nick_name as nickName, p.gender as gender, p.player_type as playerType, pg.active_status as activeStatus, pg.actual_date as actualDate " +
            "from tc_players_groups pg join tc_players p on pg.id_player = p.id_player " +
            "where pg.id_group = :groupId and p.nick_name like :pattern " +
            "and pg.is_hero = 0 and pg.active_status > 0", nativeQuery = true)
    List<PlayerRandomChoice> findPlayerInTheGroupByNickNamePattern(@Param("groupId") Long groupId, @Param("pattern") String pattern);

    @Query(value = "select p.id_player as idPlayer, p.nick_name as nickName, p.gender as gender, p.player_type as playerType, aac.a_first as firstElement, aac.ratio as ratio, " +
            "aac.save_date as saveDate, pg.active_status as activeStatus, aac.random as randomValue, aac.bonus as bonusValue, aac.days_factor as daysFactor, " +
            "aac.id_activity as idActivity, a.name as activityName " +
            "from tc_auto_activities act join tc_activities a on aac.id_activity = a.id_activity " +
            "join tc_players p on p.id_player = aac.id_player " +
            "join tc_players_groups pg on p.id_player = pg.id_player " +
            "where pg.id_group = :groupId", nativeQuery = true)
    Page<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(Pageable pageable, @Param("groupId") Long groupId);

    @Query(value = "select p.id_player as idPlayer, p.nick_name as nickName, p.gender as gender, p.player_type as playerType, aac.a_first as firstElement, aac.ratio as ratio, " +
            "aac.save_date as saveDate, pg.active_status as activeStatus, aac.random as randomValue, aac.bonus as bonusValue, aac.days_factor as daysFactor, " +
            "aac.id_activity as idActivity, a.name as activityName " +
            "from tc_auto_activities aac join tc_activities a on aac.id_activity = a.id_activity " +
            "join tc_players p on p.id_player = aac.id_player " +
            "join tc_players_groups pg on p.id_player = pg.id_player " +
            "where pg.id_group = :groupId " +
            "order by firstElement desc", nativeQuery = true)
    List<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(@Param("groupId") Long groupId);

    @Query(value = "select p.id_player as idPlayer, p.nick_name as nickName, p.gender as gender, p.player_type as playerType, aac.a_first as firstElement, aac.ratio as ratio, " +
            "aac.save_date as saveDate, pg.active_status as activeStatus, aac.random as randomValue, aac.bonus as bonusValue, aac.days_factor as daysFactor, " +
            "aac.id_activity as idActivity, a.name as activityName " +
            "from tc_auto_activities aac join tc_activities a on aac.id_activity = a.id_activity " +
            "join tc_players p on p.id_player = aac.id_player " +
            "join tc_players_groups pg on p.id_player = pg.id_player " +
            "where pg.id_group = :groupId " +
            "and aac.id_activity = :activityId " +
            "order by firstElement desc", nativeQuery = true)
    List<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(@Param("groupId") Long groupId, @Param("activityId") Long activityId);

    @Query(value = "select p.id_player as idPlayer, p.nick_name as nickName, p.first_name as firstName, " +
            "p.last_name as lastName, p.secret_name as secretName, p.gender, p.player_type as playerType, " +
            "p.age, p.code, p.description, p.is_free as isFree " +
            "from tc_players p join tc_players_groups pg on pg.id_player = p.id_player " +
            "where pg.id_group = :groupId " +
            "and p.gender = :gender " +
            "and pg.is_hero = 0", nativeQuery = true)
    List<PlayersForGeneration> getPlayersFromGroupByGenderWithoutHero(@Param("groupId") Long groupId, @Param("gender") int gender);


}

