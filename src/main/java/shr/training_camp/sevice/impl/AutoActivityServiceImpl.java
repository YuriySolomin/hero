package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.AutoActivity;
import shr.training_camp.repository.AutoActivityRepository;
import shr.training_camp.sevice.interfaces.IAutoActivityService;

import java.util.List;

@Service
public class AutoActivityServiceImpl extends AbstractEntityServiceImpl implements IAutoActivityService {

    @Autowired
    private AutoActivityRepository autoActivityRepository;

    @Override
    public Page<AutoActivity> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.autoActivityRepository.findAll(pageable);
    }

    @Override
    public Page<AutoActivity> findAutoActivitiesByGroup(Pageable pageable, @Param("groupId") Long groupId) {
        return null;
    }

    @Override
    public Page<AutoActivity> findAutoActivitiesByGroupAndActivity(Pageable pageable, @Param("groupId") Long groupId, @Param("activityId") Long activityId) {
        return autoActivityRepository.findAllPlayerAutoActivitiesByGroupAndActivity(pageable, groupId, activityId);
    }

    @Override
    public void save(AutoActivity activity) {
        this.autoActivityRepository.save(activity);
    }

    @Override
    public List<AutoActivity> getAllAutoActivitiesByPlayerId(Long playerId) {
        return this.autoActivityRepository.findAllPlayerAutoActivities(playerId);
    }

    @Override
    public AutoActivity findAutoActivityForPlayer(Long playerId, Long activityId) {
        return this.autoActivityRepository.findAutoActivityForPlayer(playerId, activityId);
    }

    @Override
    public List<Activity> getActivitiesArePresentInTheAA(Long playerId) {
        return this.autoActivityRepository.getActivitiesArePresentInTheAA(playerId);
    }
}
