package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.PetLog;
import shr.training_camp.repository.PetLogRepository;
import shr.training_camp.sevice.interfaces.IPetLogService;

@Service
public class PetLogServiceImpl extends AbstractEntityServiceImpl implements IPetLogService {

    @Autowired
    private PetLogRepository petLogRepository;

    public void save(PetLog petLog) {
        petLogRepository.save(petLog);
    }

    @Override
    public Page<PetLog> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.petLogRepository.findAll(pageable);
    }

}
