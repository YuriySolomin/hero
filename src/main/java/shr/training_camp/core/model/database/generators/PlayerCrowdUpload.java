package shr.training_camp.core.model.database.generators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCrowdUpload {

    private String nickNameMask;
    private String flNameMask;
    private int gender;
    private int type;
    private int count;
    private Long idGroup;


}
