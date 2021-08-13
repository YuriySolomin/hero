package shr.training_camp.core.model.database.addition;

import java.time.LocalDate;

public interface GameActivityGrowStatistics {

    Long getIdPlayer();
    String getNickName();
    int getGender();
    int getPlayerType();
    Double getGrow();
    Double getFactor();
    Double getFactor2();
    LocalDate getConvertDate();
    int getPlace();


}
