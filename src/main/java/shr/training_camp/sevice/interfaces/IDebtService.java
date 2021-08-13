package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.Debts;

public interface IDebtService<T> extends AbstractEntityService<T> {

    void save(Debts debts);

}
