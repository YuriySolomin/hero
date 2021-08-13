package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.CostValues;

public interface ICostValuesService<T> extends AbstractEntityService<T> {

    void saveCostValues(CostValues costValues);
}
