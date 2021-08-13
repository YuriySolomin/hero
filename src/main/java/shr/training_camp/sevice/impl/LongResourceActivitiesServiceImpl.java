package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.LongResourceActivities;
import shr.training_camp.repository.LongResourceActivitiesRepository;
import shr.training_camp.sevice.interfaces.ILongResourceActivitiesService;

import java.util.Optional;

@Service
public class LongResourceActivitiesServiceImpl extends AbstractEntityServiceImpl implements ILongResourceActivitiesService {

    @Autowired
    private LongResourceActivitiesRepository longResourceActivitiesRepository;

    @Override
    public void save(LongResourceActivities longResourceActivities) {
        longResourceActivitiesRepository.save(longResourceActivities);
    }

    @Override
    public Page findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.longResourceActivitiesRepository.findAll(pageable);
    }

    @Override
    public LongResourceActivities getLongResourceActivitiesById(long id) {
        Optional<LongResourceActivities> optional = longResourceActivitiesRepository.findById(id);
        LongResourceActivities longResourceActivities = null;
        if(optional.isPresent()) {
            longResourceActivities = optional.get();
        } else {
            throw new RuntimeException("LRA not found for id :: "  + id);
        }
        return longResourceActivities;

    }
}
