package shr.training_camp.sevice.interfaces;

import shr.training_camp.core.model.database.GameActivityEventsLog;

public interface IGameActivityEventsLogService {

    void save(GameActivityEventsLog gameActivityEventsLog);
}
