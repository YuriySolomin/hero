package shr.training_camp.sevice.interfaces;

import org.springframework.data.repository.query.Param;
import shr.training_camp.core.model.database.PlayersForGroup;

import java.util.List;

public interface IPlayersForGroupService {

    void save(PlayersForGroup playersForGroup);

    List<PlayersForGroup> getAllPlayersForGroupByStatus(Long idGroup, int playerStatus);

    List<PlayersForGroup> getAllPlayersForGroupByStatusAndGender(Long idGroup, int playerStatus, int gender);
}
