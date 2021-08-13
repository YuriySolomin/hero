package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import shr.training_camp.core.model.database.Resources;

import java.util.List;

public interface ResourceService {

    List<Resources> getAllResources();
    Page<Resources> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
    void saveResource(Resources resource);
    Resources getResourceById(Long idResource);

}
