package shr.training_camp.core.model.database;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_resources")
@NoArgsConstructor
@Data
public class Resources {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_resource")
    private Long idResource;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "r_type")
    private Integer rType;
    @Column(name = "s_value")
    private Integer sValue;

    public Resources(final String name) {
        this.name = name;
    }

    public Resources(final String name, final Integer rType, final Integer sValue) {
        this.name = name;
        this.rType = rType;
        this.sValue = sValue;
    }

}
