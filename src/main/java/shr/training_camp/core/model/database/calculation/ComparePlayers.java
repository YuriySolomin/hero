package shr.training_camp.core.model.database.calculation;

import lombok.Data;
import lombok.NoArgsConstructor;
import shr.training_camp.core.model.database.Player;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
public class ComparePlayers {

    private Player player;
    private Long idPlayer;
    private Map<Long, Double> activityValues;
    private Map<String, Double> activityValueNames;
    private boolean isHero;
    private Double growFactor;
    private Double growFactor2;
    private LocalDate currentDate;
    private Long idActivity;
    private Map<Long, Double> playerValues;
    private int activeStatus;

}
