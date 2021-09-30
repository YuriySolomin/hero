package shr.training_camp.sevice.interfaces;

import org.springframework.data.repository.query.Param;
import shr.training_camp.core.model.database.GroupProperties;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface IGroupPropertiesService {

    void save(GroupProperties groupProperties);

    GroupProperties getPropertyByGroupIdAndCode(String code, Long idGroup);

    List<GroupProperties> getAllPropertiesByGroupIdAndCodeTemplate(String code, Long idGroup);

    List<GroupProperties> getAllPropertiesFromDiapason(String code, Long idGroup, LocalDate startDate, LocalDate endDate);

}
