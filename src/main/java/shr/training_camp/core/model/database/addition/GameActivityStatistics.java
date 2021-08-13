package shr.training_camp.core.model.database.addition;

import java.time.LocalDate;

public interface GameActivityStatistics {

    Long getIdPlayer();
    Long getIdActivity();
    Double getValue();
    Double getBonusValue();
    Double getRandomValue();
    String getActivityName();
    String getNickName();

}
