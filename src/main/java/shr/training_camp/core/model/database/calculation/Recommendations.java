package shr.training_camp.core.model.database.calculation;

import lombok.Value;

@Value(staticConstructor = "of")
public class Recommendations {

    Long idActivity;
    String activityName;
    Double recommendation;
    int place;
    int period;

}
