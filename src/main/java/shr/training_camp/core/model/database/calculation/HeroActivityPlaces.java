package shr.training_camp.core.model.database.calculation;

import lombok.Data;
import lombok.NoArgsConstructor;
import shr.training_camp.core.model.database.Activity;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class HeroActivityPlaces {

    private Long idActivity;
    private Activity activity;
    private LocalDate compareDate;
    private Double average;
    private Double heroValue;
    private Double factor;
    private long place;
    private Double threeDaysRecommendation;

}
