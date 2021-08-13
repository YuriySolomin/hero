package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.Property;

public interface PropertyService<T> extends AbstractEntityService<T> {

    void saveProperty(Property property);
}
