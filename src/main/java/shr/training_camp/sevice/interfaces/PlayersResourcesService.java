package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import shr.training_camp.core.model.database.PlayersResources;
import shr.training_camp.core.model.database.addition.PlayerResourceStatistics;
import shr.training_camp.core.model.objects.dto.PlayersResourcesDTO;

import java.util.List;

public interface PlayersResourcesService {

    List<PlayersResources> getAllPlayersResources();

    Page<PlayersResources> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    void save(PlayersResources playersResources);

    void save(PlayersResourcesDTO playersResourcesDTO);

    PlayersResources getPRByPlayerAndResource(long idPlayer, long idResource);

    List<PlayerResourceStatistics> getPRGroupedByPlayerAndResource(final long idPlayer);
}
