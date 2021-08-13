package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shr.training_camp.core.model.database.GrowGroup;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.addition.AutoActivitiesPlayerGroups;
import shr.training_camp.core.model.database.addition.PlayerRandomChoice;

import java.util.List;

public interface IPlayerGroupService<T> extends AbstractEntityService<T> {

    void save(PlayersGroups playersGroups);

    List<PlayersGroups> findPlayersGroupsByGroupId(Long idPlayer);

    List<PlayersGroups> findPlayersGroupsByGroupId(Long idPlayer, int isHero);

    List<PlayersGroups> findActivePlayersGroupsByGroupId(Long idGroup, int isHero);

    PlayersGroups getPlayerGroupById(Long id);

    PlayersGroups findByGroupAndPlayerId(Long groupId, Long playerId);

    List<PlayerRandomChoice> findPlayerByGroupAndGender(Long groupId, int gender);

    List<PlayerRandomChoice> findPlayerByGroupAndGenderAndPlayerType(Long groupId, int gender, int playerType);

    List<PlayerRandomChoice> findPlayerByGroupAndNickNamePattern(Long groupId, String pattern);

    Page<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(Pageable pageable, Long groupId);

    List<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(Long groupId);

    List<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(Long groupId, Long activityId);

}
