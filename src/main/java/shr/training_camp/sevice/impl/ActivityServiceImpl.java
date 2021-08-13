package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.repository.ActivityRepository;
import shr.training_camp.sevice.interfaces.IActivityService;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl extends AbstractEntityServiceImpl implements IActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Page<Activity> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.activityRepository.findAllRealActivities(pageable);
    }

    @Override
    public void save(Activity activity) {
        this.activityRepository.save(activity);
    }

    @Override
    public Page<Activity> findAllRealActivities(Pageable pageable) {
        return activityRepository.findAllRealActivities(pageable);
    }

    @Override
    public Activity getActivityById(Long id) {
        Optional<Activity> optional = activityRepository.findById(id);
        Activity activity = new Activity();
        if (optional.isPresent()) {
            activity = optional.get();
        } else {
            throw new RuntimeException("There are no any activities with id = " + id);
        }
        return activity;
    }

    @Override
    public List<Activity> findActivitiesByType(Integer activityType) {
        return activityRepository.findActivitiesByType(activityType);
    }
}
