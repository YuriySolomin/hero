package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import shr.training_camp.core.model.database.Player;

import java.util.List;

public interface PlayerService {

    List<Player> getAllPlayers();
    void savePlayer(Player player);
    void saveAllPlayers(List<Player> allPlayers);
    List<Player> getAllPlayers(final int plType);
    Player getPlayerById(long id);
    void deletePlayerById(long id);
    Page<Player> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
    Player getPlayerByNickName(final String nickName);
    List<Player> findAllPlayersByNickNameMask(final String nickMask);
}
