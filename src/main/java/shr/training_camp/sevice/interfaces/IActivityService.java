package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shr.training_camp.core.model.database.Activity;

import java.util.List;

public interface IActivityService<T> extends AbstractEntityService<T> {

    void save(Activity activity);
    Page<Activity> findAllRealActivities(Pageable pageable);
    Activity getActivityById(Long id);
    List<Activity> findActivitiesByType(final Integer activityType);
}
