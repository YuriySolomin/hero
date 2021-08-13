package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.Costs;

public interface ICostService<T> extends AbstractEntityService<T> {

    void saveCost(Costs cost);
    Costs getCostById(Long id);
}
