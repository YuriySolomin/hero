package shr.training_camp.core.model.objects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.Resources;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayersResourcesDTO {

    private Long quantity;
    private Player player;
    private Resources resources;
}
