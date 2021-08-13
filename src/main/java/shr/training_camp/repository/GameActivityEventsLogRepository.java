package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.GameActivityEventsLog;

@Repository
public interface GameActivityEventsLogRepository extends JpaRepository<GameActivityEventsLog, Long> {
}
