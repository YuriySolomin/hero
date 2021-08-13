package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayerActivities;
import shr.training_camp.core.model.database.addition.HeroActivityResult;
import shr.training_camp.core.model.database.addition.PlayerActivityStatistics;
import shr.training_camp.repository.PlayerActivityRepository;
import shr.training_camp.sevice.interfaces.IPlayerActivityService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerActivityServiceImpl extends AbstractEntityServiceImpl implements IPlayerActivityService {

    @Autowired
    private PlayerActivityRepository playerActivityRepository;

    @Override
    public void save(PlayerActivities playerActivities) {
        this.playerActivityRepository.save(playerActivities);
    }

    @Override
    public Page findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.playerActivityRepository.findAll(pageable);
    }

    @Override
    public List<PlayerActivityStatistics> findGroupedByAll(final Long idPlayer) {
        return this.playerActivityRepository.findGroupedByAll(idPlayer);
    }

    @Override
    public HeroActivityResult getHeroActivityResult(Long idPlayer) {
        return playerActivityRepository.getGroupedQuantityByActivity(idPlayer);
    }

    @Override
    public List<PlayerActivityStatistics> getHeroActivityResult(Long idPlayer, LocalDate startDate) {
        return playerActivityRepository.getGroupedQuantityByActivity(idPlayer, Date.valueOf(startDate));
    }

    @Override
    public List<PlayerActivityStatistics> getHeroActivityResultByDate(Long idPlayer, LocalDate startDate) {
        return playerActivityRepository.getGroupedQuantityByActivityAndDate(idPlayer, Date.valueOf(startDate));
    }


    @Override
    public List<PlayerActivityStatistics> getHeroActivityResultByDate2(Long idActivity, LocalDate startDate) {
        return playerActivityRepository.getGroupedQuantityByActivityAndDate2(idActivity, Date.valueOf(startDate));
    }

    @Override
    public List<PlayerActivityStatistics> getGroupedByAllParameters(Long idPlayer, Long idActivity, LocalDate startDate, LocalDate endDate) {
        return playerActivityRepository.getGroupedByAllParameters(idPlayer, idActivity, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    @Override
    public List<PlayerActivityStatistics> getRealPlayerActivityStatistic(Long idPlayer, LocalDate startDate, LocalDate saveDate, final LocalDate endDate) {
        return playerActivityRepository.getRealPlayerActivityStatistic(idPlayer, Date.valueOf(startDate), Date.valueOf(saveDate), Date.valueOf(endDate));
    }

    @Override
    public List<PlayerActivities> getAllPlayerActivities() {
        Sort sort = Sort.by(Sort.Direction.DESC, "startDate");
        return playerActivityRepository.findAll(sort);
    }

    @Override
    public List<PlayerActivityStatistics> getGroupedActivitiesForToday(Long idPlayer) {
        return playerActivityRepository.getGroupedActivitiesForToday(idPlayer);
    }

    @Override
    public List<PlayerActivities> getPlayersActivitiesFilteredByUsed(Integer isUsed) {
        return playerActivityRepository.getPlayersActivitiesFilteredByUsed(isUsed);
    }

    @Override
    public PlayerActivities getPAById(Long id) {
        Optional<PlayerActivities> optional = playerActivityRepository.findById(id);
        PlayerActivities playerActivities = null;
        if(optional.isPresent()) {
            playerActivities = optional.get();
        } else {
            throw new RuntimeException("Player activity not found for id :: "  + id);
        }
        return playerActivities;

    }
}
