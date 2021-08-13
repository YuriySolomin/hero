package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_achievements")
@NoArgsConstructor
@Data
public class Achievements {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_achievement")
    private Long idAchievement;
    @Column(name = "name")
    private String name;
    @Column(name = "stage")
    private Integer stage;
    @Column(name = "a_type")
    private Integer aType;

    public Achievements(final String name, final Integer stage, final Integer aType) {
        this.name = name;
        this.stage = stage;
        this.aType = aType;
    }
}
