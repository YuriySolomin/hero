package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_cost_values")
@NoArgsConstructor
@Data
public class CostValues {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_cost")
    private Long idCost;
    @Column(name = "id_resource")
    private Long idResource;
    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_resource", nullable = false, insertable = false, updatable = false)
    private Resources resource;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cost", nullable = false, insertable = false, updatable = false)
    private Costs cost;

    public CostValues(final Long idCost, final Long idResource, final Long quantity) {
        this.idCost = idCost;
        this.idResource = idResource;
        this.quantity = quantity;
    }

}
