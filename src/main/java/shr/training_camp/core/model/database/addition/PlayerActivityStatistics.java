package shr.training_camp.core.model.database.addition;

import java.time.LocalDate;

public interface PlayerActivityStatistics {

    Double getQuantity();
    Long getIdPlayer();
    Long getIdActivity();
    LocalDate getStartDate();
    Long getPeriod();
    String getActivityName();
    String getPlayerNickName();
}
