package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.LongResourceActivities;

public interface ILongResourceActivitiesService<T> extends AbstractEntityService<T> {

    void save(LongResourceActivities longResourceActivities);

    LongResourceActivities getLongResourceActivitiesById(long id);

}
