package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Costs;
import shr.training_camp.repository.CostRepository;
import shr.training_camp.sevice.interfaces.ICostService;

import java.util.Optional;

@Service
public class CostServiceImpl extends AbstractEntityServiceImpl implements ICostService {

    @Autowired
    private CostRepository costRepository;

    @Override
    public void saveCost(Costs cost) {
        costRepository.save(cost);
    }

    @Override
    public Page findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return costRepository.findAll(pageable);
    }

    @Override
    public Costs getCostById(Long id) {
        Optional<Costs> optional = costRepository.findById(id);
        Costs cost = null;
        if(optional.isPresent()) {
            cost = optional.get();
        } else {
            throw new RuntimeException("Cost not found for id :: "  + id);
        }
        return cost;
    }
}
