package shr.training_camp.core.model.database.addition;

import java.time.LocalDate;

public interface PlayerRandomChoice {

    Long getIdPlayer();
    String getNickName();
    Integer getGender();
    Integer getPlayerType();
    Integer getActiveStatus();
    LocalDate getActualDate();

}
