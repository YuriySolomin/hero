package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.GameActivityLog;
import shr.training_camp.core.model.database.addition.GameActivityStatistics;
import shr.training_camp.repository.GameActivityLogRepository;
import shr.training_camp.sevice.interfaces.IGameActivityLogService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class GameActivityLogServiceImpl implements IGameActivityLogService {

    @Autowired
    private GameActivityLogRepository gameActivityLogRepository;

    @Override
    public void save(GameActivityLog gameActivityLog) {
        gameActivityLogRepository.save(gameActivityLog);
    }

    @Override
    public List<GameActivityStatistics> getGameActivityStatistics(LocalDate endDate) {
        return gameActivityLogRepository.getGameActivityStatistics(Date.valueOf(endDate));
    }

    @Override
    public List<GameActivityStatistics> getGameActivityStatistics(LocalDate endDate, Long idPlayer) {
        return gameActivityLogRepository.getGameActivityStatistics(Date.valueOf(endDate), idPlayer);
    }

    @Override
    public List<GameActivityStatistics> getGameActivityStatisticsByActivity(LocalDate endDate, Long idActivity) {
        return gameActivityLogRepository.getGameActivityStatisticsByActivity(Date.valueOf(endDate), idActivity);
    }

    @Override
    public List<GameActivityStatistics> getGameActivityStatistics(Long groupId) {
        return gameActivityLogRepository.getGameActivityStatisticsByActivity(groupId);
    }
}
