package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_costs")
@NoArgsConstructor
@Data
public class Costs {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_cost")
    private Long idCost;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    public Costs(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

}
