package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.GroupProperties;
import shr.training_camp.repository.GroupPropertiesRepository;
import shr.training_camp.sevice.interfaces.IGroupPropertiesService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class GroupPropertiesImpl implements IGroupPropertiesService {

    @Autowired
    private GroupPropertiesRepository groupPropertiesRepository;

    @Override
    public void save(GroupProperties groupProperties) {
        groupPropertiesRepository.save(groupProperties);
    }

    @Override
    public GroupProperties getPropertyByGroupIdAndCode(String code, Long idGroup) {
        return groupPropertiesRepository.getPropertyByGroupIdAndCode(code, idGroup);
    }

    @Override
    public List<GroupProperties> getAllPropertiesByGroupIdAndCodeTemplate(String code, Long idGroup) {
        return groupPropertiesRepository.getAllPropertiesByGroupIdAndCodeTemplate(code, idGroup);
    }

    @Override
    public List<GroupProperties> getAllPropertiesFromDiapason(String code, Long idGroup, LocalDate startDate, LocalDate endDate) {
        return groupPropertiesRepository.getAllPropertiesFromDiapason(code, idGroup, Date.valueOf(startDate), Date.valueOf(endDate));
    }
}
