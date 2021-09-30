package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tc_group_properties")
@NoArgsConstructor
@Data
public class GroupProperties {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_gp")
    private Long idGp;
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "code")
    private String code;
    @Column(name = "value")
    private Double value;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;

    public GroupProperties(Long idGroup, String code, Double value, LocalDate startDate, String description) {
        this.idGroup = idGroup;
        init(code, value, startDate, description);
    }

    public GroupProperties(GrowGroup growGroup, String code, Double value, LocalDate startDate, String description) {
        this.growGroup = growGroup;
        init(code, value, startDate, description);
    }

    private void init(String code, Double value, LocalDate startDate, String description) {
        this.code = code;
        this.value = value;
        this.startDate = startDate;
        this.description = description;
    }

}
