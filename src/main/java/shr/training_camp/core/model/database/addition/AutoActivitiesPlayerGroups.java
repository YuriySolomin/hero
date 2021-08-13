package shr.training_camp.core.model.database.addition;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AutoActivitiesPlayerGroups {

    Long getIdPlayer();
    String getNickName();
    int getGender();
    int getPlayerType();
    Double getFirstElement();
    Double getRatio();
    LocalDateTime getSaveDate();
    int getActiveStatus();
    Double getRandomValue();
    Double getBonusValue();
    int getDaysFactor();
    Long getIdActivity();
    String getActivityName();

}
