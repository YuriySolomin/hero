package shr.training_camp.core.model.database.calculation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value(staticConstructor = "of")
public class PlaceRecommendations {

    String nickName;
    String activityName;
    Double firstElement;
    Double ratio;
    Double bonusValue;
    Double randomValue;
    int daysFactor;

}
