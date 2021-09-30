package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.GroupActivities;

import java.util.List;

@Repository
public interface GroupActivitiesRepository extends JpaRepository<GroupActivities, Long> {

    @Query(value = "select * from tc_group_activities " +
            "where id_group = :idGroup", nativeQuery = true)
    List<GroupActivities> getActivitiesInTheGroup(@Param("idGroup") Long idGroup);
}
