package shr.training_camp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.Activity;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query(value = "select * from tc_activities where activity_filter = 0", nativeQuery = true)
    Page<Activity> findAllRealActivities(Pageable pageable);

    @Query(value = "select * from tc_activities where activity_type = :activityType", nativeQuery = true)
    List<Activity> findActivitiesByType(@Param("activityType") Integer activityType);

}
