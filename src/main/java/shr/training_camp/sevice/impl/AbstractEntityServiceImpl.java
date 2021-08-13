package shr.training_camp.sevice.impl;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public abstract class AbstractEntityServiceImpl {

    Pageable pageable;

    void setPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        pageable = PageRequest.of(pageNo - 1, pageSize, sort);
    }
}
