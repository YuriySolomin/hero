package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.Costs;

@Repository
public interface CostRepository extends JpaRepository<Costs, Long> {
}
