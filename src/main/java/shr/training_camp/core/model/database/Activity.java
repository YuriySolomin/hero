package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_activities")
@NoArgsConstructor
@Data
public class Activity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_activity")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "activity_type")
    private int activityType;
    @Column(name = "description")
    private String description;
    @Column(name = "activity_filter")
    private int activityFilter;
    @Column(name = "virtual_text")
    private String virtualText;

    public Activity(final String name, final int activityType, final String description, final int activityFilter,
                    final String virtualText) {
        this.activityType = activityType;
        this.name = name;
        this.description = description;
        this.activityFilter = activityFilter;
        this.virtualText = virtualText;
    }
}
