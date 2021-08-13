package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.CostValues;

@Repository
public interface CostValueRepository extends JpaRepository<CostValues, Long> {
}
