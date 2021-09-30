package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_group_activities")
@NoArgsConstructor
@Data
public class GroupActivities {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_ga")
    private Long idGa;
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "id_activity")
    private Long idActivity;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "limit_ratio")
    private Double limitRatio;
    @Column(name = "limit_element")
    private Double limitElement;
    @Column(name = "weight_factor")
    private Double weightFactor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_activity", nullable = false, insertable = false, updatable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;

    public GroupActivities(Long idGroup, Long idActivity, Double weight, Double limitRatio, Double limitElement, Double weightFactor) {
        this.idGroup = idGroup;
        this.idActivity = idActivity;
        this.weight = weight;
        this.limitRatio = limitRatio;
        this.limitElement = limitElement;
        this.weightFactor = weightFactor;
    }

    public GroupActivities(GrowGroup growGroup, Activity activity, Double weight, Double limitRatio, Double limitElement, Double weightFactor) {
        this.growGroup = growGroup;
        this.activity = activity;
        this.weight = weight;
        this.limitRatio = limitRatio;
        this.limitElement = limitElement;
        this.weightFactor = weightFactor;
    }

}
