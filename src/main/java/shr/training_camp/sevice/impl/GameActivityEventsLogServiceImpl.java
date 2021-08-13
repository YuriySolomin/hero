package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.GameActivityEventsLog;
import shr.training_camp.repository.GameActivityEventsLogRepository;
import shr.training_camp.sevice.interfaces.IGameActivityEventsLogService;
import shr.training_camp.sevice.interfaces.IGameActivityGrowLogService;

@Service
public class GameActivityEventsLogServiceImpl implements IGameActivityEventsLogService {

    @Autowired
    private GameActivityEventsLogRepository gameActivityEventsLogRepository;

    @Override
    public void save(GameActivityEventsLog gameActivityEventsLog) {
        gameActivityEventsLogRepository.save(gameActivityEventsLog);
    }
}
