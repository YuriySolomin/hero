package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.GameActivityLog;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;

import java.time.LocalDate;
import java.util.List;

public interface IGameActivityLogService {

    void save(GameActivityLog gameActivityLog);

    List<GameActivityStatistics> getGameActivityStatistics(LocalDate endDate);

    List<GameActivityStatistics> getGameActivityStatistics(LocalDate endDate, Long idPlayer, Long idGroup);

    List<GameActivityStatistics> getGameActivityStatisticsByActivity(LocalDate endDate, Long idActivity);

    List<GameActivityStatistics> getGameActivityStatistics(Long groupId);

    LocalDate getMaxDateGameActivityLogForGroup(Long groupId);

}
