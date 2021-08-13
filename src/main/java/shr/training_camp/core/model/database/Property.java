package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_properties")
@NoArgsConstructor
@Data
public class Property {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_property")
    private Long idProperty;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "pr_type")
    private int prType;
    @Column(name = "ratio")
    private Integer ratio;


    public Property(final Long idProperty, final String name, final String description, final Integer ratio) {
        this.idProperty = idProperty;
        this.name = name;
        this.description = description;
        this.ratio = ratio;
    }
}
