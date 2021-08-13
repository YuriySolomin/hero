package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.GameActivityGrowLog;
import shr.training_camp.core.model.database.addition.GameActivityGrowStatistics;

import java.util.List;

public interface IGameActivityGrowLogService {

    void save(GameActivityGrowLog gameActivityGrowLog);

    List<GameActivityGrowStatistics> getGrowLastDateStatistic();
}
