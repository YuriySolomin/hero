package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.GameActivityGrowLog;
import shr.training_camp.core.model.database.addition.GameActivityGrowStatistics;
import shr.training_camp.repository.GameActivityGrowLogRepository;
import shr.training_camp.sevice.interfaces.IGameActivityGrowLogService;

import java.util.List;

@Service
public class GameActivityGrowLogServiceImpl implements IGameActivityGrowLogService {

    @Autowired
    private GameActivityGrowLogRepository gameActivityGrowLogRepository;

    @Override
    public void save(GameActivityGrowLog gameActivityGrowLog) {
        gameActivityGrowLogRepository.save(gameActivityGrowLog);
    }

    @Override
    public List<GameActivityGrowStatistics> getGrowLastDateStatistic() {
        return gameActivityGrowLogRepository.getGrowLastDateStatistic();
    }
}
