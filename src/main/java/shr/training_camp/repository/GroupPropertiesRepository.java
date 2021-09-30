package shr.training_camp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.GroupProperties;

import java.sql.Date;
import java.util.List;

@Repository
public interface GroupPropertiesRepository extends JpaRepository<GroupProperties, Long> {

    @Query(value = "select * from tc_group_properties " +
            "where code = :code " +
            "and id_group = :idGroup", nativeQuery = true)
    GroupProperties getPropertyByGroupIdAndCode(@Param("code") String code, @Param("idGroup") Long idGroup);

    @Query(value = "select * from tc_group_properties " +
            "where code like :code " +
            "and id_group = :idGroup",
            nativeQuery = true)
    List<GroupProperties> getAllPropertiesByGroupIdAndCodeTemplate(@Param("code") String code, @Param("idGroup") Long idGroup);

    @Query(value = "select * from tc_group_properties " +
            "where code like :code " +
            "and id_group = :idGroup " +
            "and start_date between :startDate and :endDate " +
            "order by start_date", nativeQuery = true)
    List<GroupProperties> getAllPropertiesFromDiapason(@Param("code") String code, @Param("idGroup") Long idGroup,
                                                       @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
