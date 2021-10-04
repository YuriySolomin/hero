package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tc_grow_groups")
@NoArgsConstructor
@Data
public class GrowGroup {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "name")
    private String name;
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "group_type")
    private Integer groupType;
    @Column(name = "levels_count")
    private Integer levelsCount;
    @Column(name = "max_height")
    private Double maxHeight;
    @Column(name = "min_height")
    private Double minHeight;
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Column(name = "fail_out_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate failOutDate;


    public GrowGroup(final String name, final LocalDate startDate, final Integer groupType, final Integer levelsCount,
                     final Double maxHeight, final Double minHeight, final LocalDate endDate, final LocalDate failOutDate) {
        this.name = name;
        this.startDate = startDate;
        this.groupType = groupType;
        this.levelsCount = levelsCount;
        this.maxHeight = maxHeight;
        this.endDate = endDate;
        this.minHeight = minHeight;
        this.failOutDate = failOutDate;
    }
}
