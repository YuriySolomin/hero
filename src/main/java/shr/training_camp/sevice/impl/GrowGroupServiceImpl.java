package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.GrowGroup;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;
import shr.training_camp.repository.GrowGroupsRepository;
import shr.training_camp.sevice.interfaces.IGrowGroupService;

import java.util.List;
import java.util.Optional;

@Service
public class GrowGroupServiceImpl extends AbstractEntityServiceImpl implements IGrowGroupService {

    @Autowired
    private GrowGroupsRepository growGroupsRepository;

    @Override
    public Page<GrowGroup> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.growGroupsRepository.findAll(pageable);
    }

    @Override
    public void save(GrowGroup growGroup) {
        this.growGroupsRepository.save(growGroup);
    }

    @Override
    public GrowGroup getGrowGroupById(long id) {
        Optional<GrowGroup> optional = growGroupsRepository.findById(id);
        GrowGroup growGroup = null;
        if(optional.isPresent()) {
            growGroup = optional.get();
        } else {
            throw new RuntimeException("Grow group not found for id :: "  + id);
        }
        return growGroup;
    }
}
