package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.GroupActivities;
import shr.training_camp.repository.GroupActivitiesRepository;
import shr.training_camp.sevice.interfaces.IGroupActivitiesService;

import java.util.List;

@Service
public class GroupActivitiesImpl implements IGroupActivitiesService {

    @Autowired
    private GroupActivitiesRepository groupActivitiesRepository;

    @Override
    public void save(GroupActivities groupActivities) {
        groupActivitiesRepository.save(groupActivities);
    }

    @Override
    public List<GroupActivities> getActivitiesInTheGroup(Long idGroup) {
        return groupActivitiesRepository.getActivitiesInTheGroup(idGroup);
    }
}
