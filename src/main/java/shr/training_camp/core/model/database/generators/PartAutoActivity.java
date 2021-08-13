package shr.training_camp.core.model.database.generators;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartAutoActivity {

    private Long idActivity;
    private Long idPlayer;
    private Double firstElement;
    private Double ratio;
    private Double initValue;
    private String type;
    private String parsing;
    private Double bonus;
    private Double random;


}
