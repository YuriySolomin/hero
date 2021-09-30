package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.GroupActivities;

import java.util.List;

public interface IGroupActivitiesService {

    void save(GroupActivities groupActivities);

    List<GroupActivities> getActivitiesInTheGroup(Long idGroup);
}
