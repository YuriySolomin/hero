package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.CostValues;
import shr.training_camp.repository.CostValueRepository;
import shr.training_camp.sevice.interfaces.ICostValuesService;

@Service
public class CostValuesServiceImpl extends AbstractEntityServiceImpl implements ICostValuesService {

    @Autowired
    private CostValueRepository costValueRepository;

    @Override
    public void saveCostValues(CostValues costValues) {
        costValueRepository.save(costValues);
    }

    @Override
    public Page findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return costValueRepository.findAll(pageable);
    }
}
