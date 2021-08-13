package shr.training_camp.core.model.database.generators;

import lombok.Data;
import lombok.NoArgsConstructor;
import shr.training_camp.core.model.database.GameActivityLog;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
public class PlayerAutoActivityInfo {

    private Long idPlayer;
    private Double firstElement;
    private Integer daysFactor;
    private LocalDate saveDate;
    private Double summary;
    private List<GameActivityLog> gameActivityLogList;

}
