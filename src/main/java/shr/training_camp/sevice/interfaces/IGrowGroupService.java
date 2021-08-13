package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.GrowGroup;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;

import java.util.List;

public interface IGrowGroupService<T> extends AbstractEntityService<T> {

    void save(GrowGroup growGroup);

    GrowGroup getGrowGroupById(long id);

}
