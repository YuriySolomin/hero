package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Debts;
import shr.training_camp.repository.DebtRepository;
import shr.training_camp.sevice.interfaces.IDebtService;

@Service
public class DebtServiceImpl extends AbstractEntityServiceImpl implements  IDebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Override
    public Page<Debts> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.debtRepository.findAll(pageable);
    }

    @Override
    public void save(Debts debt) {
        this.debtRepository.save(debt);
    }


}
