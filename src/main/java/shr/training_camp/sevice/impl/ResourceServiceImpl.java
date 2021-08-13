package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Resources;
import shr.training_camp.repository.ResourceRepository;
import shr.training_camp.sevice.interfaces.ResourceService;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    public ResourceRepository resourceRepository;

    @Override
    public List<Resources> getAllResources() {
        return resourceRepository.findAll();
    }

    @Override
    public void saveResource(Resources resource) {
        this.resourceRepository.save(resource);
    }


    @Override
    public Page<Resources> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.resourceRepository.findAll(pageable);
    }

    @Override
    public Resources getResourceById(Long idResource) {
        Optional<Resources> optional = resourceRepository.findById(idResource);
        Resources resource = null;
        if(optional.isPresent()) {
            resource = optional.get();
        } else {
            throw new RuntimeException("Resource not found for id :: "  + idResource);
        }
        return resource;

    }
}
