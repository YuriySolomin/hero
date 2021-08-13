package shr.training_camp.core.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationSetup {

    private Long idPlayer;
    private int shiftDays;
}
