package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.AutoActivity;

import java.util.List;

public interface AbstractEntityService<T> {

    Page<T> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);


}
