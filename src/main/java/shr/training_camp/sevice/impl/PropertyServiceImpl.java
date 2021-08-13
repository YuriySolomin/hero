package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Property;
import shr.training_camp.repository.PropertyRepository;
import shr.training_camp.sevice.interfaces.PropertyService;

@Service
public class PropertyServiceImpl extends AbstractEntityServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Page<Property> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.propertyRepository.findAll(pageable);
    }

    @Override
    public void saveProperty(Property property) {
        this.propertyRepository.save(property);
    }
}
