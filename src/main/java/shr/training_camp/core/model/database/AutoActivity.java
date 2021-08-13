package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "tc_auto_activities")
@NoArgsConstructor
@Data
public class AutoActivity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_activity")
    private Long idActivity;
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "a_first")
    private Double firstElement;
    @Column(name = "ratio")
    private Double ratio;
    @Column(name = "bonus")
    private Double bonus;
    @Column(name = "random")
    private Double randomBonus;
    @Column(name = "summary")
    private Double summary;
    @Column(name = "save_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate saveDate;
    @Column(name = "init_value")
    private Double initValue;
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Column(name = "days_factor")
    private Integer daysFactor;
    @Column(name = "id_group")
    private Long idGroup;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_activity", nullable = false, insertable = false, updatable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;

    public AutoActivity(final Player player, final Activity activity, final LocalDate startDate, final Double firstElement,
                        final Double ratio, final Double bonus, final Double randomBonus, final Double summary, final LocalDate saveDate,
                        final Double initValue, final LocalDate endDate, final Integer daysFactor, final GrowGroup growGroup) {
        this.player = player;
        this.activity = activity;
        this.startDate = startDate;
        this.firstElement = firstElement;
        this.ratio = ratio;
        this.bonus = bonus;
        this.randomBonus = randomBonus;
        this.summary = summary;
        this.saveDate = saveDate;
        this.initValue = initValue;
        this.endDate = endDate;
        this.daysFactor = daysFactor;
        this.growGroup = growGroup;
    }

}
