package shr.training_camp.core.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shr.training_camp.core.model.database.AutoActivity;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayerActivities;
import shr.training_camp.core.model.database.addition.HeroActivityResult;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStatistic {

    private Player player;
    private AutoActivity autoActivity;
    private LocalDate localDate;
    private Double summary;
    private HeroActivityResult heroActivityResult;
    private Double quantity;
}
