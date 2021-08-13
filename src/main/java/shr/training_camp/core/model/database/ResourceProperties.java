package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tc_resource_properties")
@NoArgsConstructor
@Data
public class ResourceProperties {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_rp")
    private Long idRP;
    @Column(name = "id_resource")
    private Long idResource;
    @Column(name = "id_property")
    private Long idProperty;
    @Column(name = "base_qty")
    private Long baseQty;
    @Column(name = "current_qty")
    private Long currentQty;
    @Column(name = "cost_factor")
    private Long costFactor;
    @Column(name = "grow_factor")
    private Double growFactor;
    @Column(name = "assignment")
    private Long assignment;
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_property", nullable = false, insertable = false, updatable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_resource", nullable = false, insertable = false, updatable = false)
    private Resources resource;

    public ResourceProperties(Resources resources, Property property, Long baseQty, Long currentQty,
                              Long costFactor, Double growFactor, Long assignment, String description) {
        this.resource = resources;
        this.idResource = resources.getIdResource();
        this.property = property;
        this.idProperty = property.getIdProperty();
        this.baseQty = baseQty;
        this.currentQty = currentQty;
        this.costFactor = costFactor;
        this.growFactor = growFactor;
        this.assignment = assignment;
        this.description = description;
    }



}
