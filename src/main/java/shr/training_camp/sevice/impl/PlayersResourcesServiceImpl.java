package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.PlayersResources;
import shr.training_camp.core.model.database.addition.PlayerResourceStatistics;
import shr.training_camp.core.model.objects.dto.PlayersResourcesDTO;
import shr.training_camp.repository.PlayerResourceRepository;
import shr.training_camp.sevice.interfaces.PlayersResourcesService;

import java.util.List;
import java.util.Objects;

@Service
public class PlayersResourcesServiceImpl implements PlayersResourcesService {

    @Autowired
    private PlayerResourceRepository playerResourceRepository;

    @Override
    public List<PlayersResources> getAllPlayersResources() {
        return playerResourceRepository.findAll();
    }

    @Override
    public Page<PlayersResources> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.playerResourceRepository.findAll(pageable);
    }

    @Override
    public void save(PlayersResources playersResources) {
        playerResourceRepository.save(playersResources);
    }

    @Override
    public void save(PlayersResourcesDTO playersResourcesDTO) {
        PlayersResources existingObject = getPRByPlayerAndResource(
                playersResourcesDTO.getPlayer().getId_player(),
                playersResourcesDTO.getResources().getIdResource());
        if (Objects.nonNull(existingObject)) {
            existingObject.setQuantity(existingObject.getQuantity() + playersResourcesDTO.getQuantity());
            playerResourceRepository.save(existingObject);
        } else {
            //playersResourcesDTO.setIdPlayer(playersResources.getPlayer().getId_player());
            //playersResourcesD.setIdResource(playersResources.getResource().getIdResource());
            //playerResourceRepository.save(playersResources);
            //save(playersResources);
        }

    }

    @Override
    public PlayersResources getPRByPlayerAndResource(long idPlayer, long idResource) {
        return playerResourceRepository.getPRByPlayerAndResource(idPlayer, idResource);
    }

    @Override
    public List<PlayerResourceStatistics> getPRGroupedByPlayerAndResource(final long idPlayer) {
        return playerResourceRepository.getPRGroupedByPlayerAndResource(idPlayer);
    }
}
