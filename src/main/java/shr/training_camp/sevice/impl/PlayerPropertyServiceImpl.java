package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.PlayerProperty;
import shr.training_camp.repository.PlayerPropertyRepository;
import shr.training_camp.sevice.interfaces.IPlayerPropertyService;

@Service
public class PlayerPropertyServiceImpl extends AbstractEntityServiceImpl implements IPlayerPropertyService {

    @Autowired
    private PlayerPropertyRepository playerPropertyRepository;

    @Override
    public Page<PlayerProperty> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.playerPropertyRepository.findAll(pageable);
    }

    @Override
    public void save(PlayerProperty playerProperty) {
        playerPropertyRepository.save(playerProperty);
    }

    @Override
    public PlayerProperty getPPByPlayerAndProperty(Long idPlayer, Long idProperty) {
        return playerPropertyRepository.getPPByPlayerAndProperty(idPlayer, idProperty);
    }
}
