package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.PlayersForGroup;
import shr.training_camp.repository.PlayersForGroupRepository;
import shr.training_camp.sevice.interfaces.IPlayersForGroupService;

import java.util.List;

@Service
public class PlayersForGroupImpl implements IPlayersForGroupService {

    @Autowired
    private PlayersForGroupRepository playersForGroupRepository;

    @Override
    public void save(PlayersForGroup playersForGroup) {
        playersForGroupRepository.save(playersForGroup);
    }

    @Override
    public List<PlayersForGroup> getAllPlayersForGroupByStatus(Long idGroup, int playerStatus) {
        return playersForGroupRepository.getAllPlayersForGroupByStatus(idGroup, playerStatus);
    }

    @Override
    public List<PlayersForGroup> getAllPlayersForGroupByStatusAndGender(Long idGroup, int playerStatus, int gender) {
        return playersForGroupRepository.getAllPlayersForGroupByStatusAndGender(idGroup, playerStatus, gender);
    }
}
