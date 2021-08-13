package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.sevice.interfaces.PlayerService;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public void savePlayer(Player player) {
        this.playerRepository.save(player);
    }

    @Override
    public void saveAllPlayers(List<Player> allPlayers) {
        this.playerRepository.saveAll(allPlayers);
    }

    @Override
    public List<Player> getAllPlayers(int plType) {
        return playerRepository.findPlayersByType(plType);
    }

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

    @Override
    public void deletePlayerById(long id) {
        this.playerRepository.deleteById(id);
    }

    @Override
    public Page<Player> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.playerRepository.findAll(pageable);
    }

    @Override
    public Player getPlayerByNickName(String nickName) {
        return playerRepository.findPlayerByNickName(nickName);
    }

    @Override
    public List<Player> findAllPlayersByNickNameMask(String nickMask) {
        return playerRepository.findAllPlayersByNickNameMask(nickMask);
    }
}
