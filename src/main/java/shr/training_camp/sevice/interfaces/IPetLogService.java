package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.PetLog;

public interface IPetLogService <T> extends AbstractEntityService<T> {

    void save(PetLog petLog);

}
