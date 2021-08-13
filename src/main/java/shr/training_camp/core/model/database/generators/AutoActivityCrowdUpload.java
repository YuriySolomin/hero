package shr.training_camp.core.model.database.generators;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AutoActivityCrowdUpload {

    private Long idActivity;
    private Double firstElement;
    private Double ratio;
    private int randomFirstElement;
    private int randomRatio;
    private int random;
    private int bonus;
    private String nickMask;

}
