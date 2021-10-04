package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.addition.AutoActivitiesPlayerGroups;
import shr.training_camp.core.model.database.addition.PlayerRandomChoice;
import shr.training_camp.core.model.database.addition.PlayersForGeneration;
import shr.training_camp.repository.PlayersGroupsRepository;
import shr.training_camp.sevice.interfaces.IPlayerGroupService;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerGroupServiceImpl extends AbstractEntityServiceImpl implements IPlayerGroupService {

    @Autowired
    private PlayersGroupsRepository playersGroupsRepository;

    @Override
    public void save(PlayersGroups playersGroups) {
        this.playersGroupsRepository.save(playersGroups);
    }

    @Override
    public Page<PlayersGroups> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.playersGroupsRepository.findAll(pageable);
    }

    @Override
    public List<PlayersGroups> findPlayersGroupsByGroupId(Long idGroup) {
        return this.playersGroupsRepository.findByGroupId(idGroup);
    }

    @Override
    public List<PlayersGroups> findPlayersGroupsByGroupId(Long idGroup, int isHero) {
        return this.playersGroupsRepository.findByGroupId(idGroup, isHero);
    }

    @Override
    public List<PlayersGroups> findActivePlayersGroupsByGroupId(Long idGroup, int isHero) {
        return this.playersGroupsRepository.findActivePlayersByGroupId(idGroup, isHero);
    }

    @Override
    public PlayersGroups getPlayerGroupById(Long id) {
        Optional<PlayersGroups> optional = playersGroupsRepository.findById(id);
        PlayersGroups playerGroup = null;
        if(optional.isPresent()) {
            playerGroup = optional.get();
        } else {
            throw new RuntimeException("Player not found for id :: "  + id);
        }
        return playerGroup;
    }

    @Override
    public PlayersGroups findByGroupAndPlayerId(Long groupId, Long playerId) {
        return playersGroupsRepository.findByGroupAndPlayerId(groupId, playerId).get(0);
    }

    @Override
    public List<PlayerRandomChoice> findPlayerByGroupAndGender(Long groupId, int gender) {
        return playersGroupsRepository.findPlayerInTheGroupByGender(groupId, gender);
    }

    @Override
    public List<PlayerRandomChoice> findPlayerByGroupAndGenderAndPlayerType(Long groupId, int gender, int playerType) {
        return playersGroupsRepository.findPlayerInTheGroupByGenderAndType(groupId, gender, playerType);
    }

    @Override
    public List<PlayerRandomChoice> findPlayerByGroupAndNickNamePattern(Long groupId, String pattern) {
        return playersGroupsRepository.findPlayerInTheGroupByNickNamePattern(groupId, pattern);
    }

    @Override
    public Page<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(Pageable pageable, Long groupId) {
        return playersGroupsRepository.findAutoActivitiesForPlayerGroups(pageable, groupId);
    }

    @Override
    public List<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(Long groupId) {
        return playersGroupsRepository.findAutoActivitiesForPlayerGroups(groupId);
    }

    @Override
    public List<AutoActivitiesPlayerGroups> findAutoActivitiesForPlayerGroups(Long groupId, Long activityId) {
        return playersGroupsRepository.findAutoActivitiesForPlayerGroups(groupId, activityId);
    }

    @Override
    public List<PlayersForGeneration> getPlayersFromGroupByGenderWithoutHero(Long groupId, int gender) {
        return playersGroupsRepository.getPlayersFromGroupByGenderWithoutHero(groupId, gender);
    }
    /*
    *
        @Override
    public Player getPlayerById(long id) {
        Optional<Player> optional = playerRepository.findById(id);
        Player player = null;
        if(optional.isPresent()) {
            player = optional.get();
        } else {
            throw new RuntimeException("Player not found for id :: "  + id);
        }
        return player;
    }

    * */

}
