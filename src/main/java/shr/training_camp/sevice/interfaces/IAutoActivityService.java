package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.AutoActivity;

import java.util.List;

public interface IAutoActivityService<T> extends AbstractEntityService<T> {

    void save(AutoActivity autoActivity);

    List<AutoActivity> getAllAutoActivitiesByPlayerId(final Long playerId);

    List<AutoActivity> getAllAutoActivitiesByPlayerId(final Long playerId, final Long groupId);

    AutoActivity findAutoActivityForPlayer(final Long playerId, final Long activityId);

    List<Activity> getActivitiesArePresentInTheAA(final Long playerId);

    Page<AutoActivity> findAutoActivitiesByGroup(Pageable pageable, @Param("groupId") Long groupId);

    Page<AutoActivity> findAutoActivitiesByGroupAndActivity(Pageable pageable, @Param("groupId") Long groupId, @Param("activityId") Long activityId);

}
