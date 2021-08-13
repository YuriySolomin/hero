package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tc_players_activities")
@NoArgsConstructor
@Data
public class PlayerActivities {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_pa")
    private Long id;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_activity")
    private Long idActivity;
    @Column(name = "quantity")
    private Double quantity;
    @Column(name = "active")
    private int active;
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "act_type")
    private int actType;
    @Column(name = "duration")
    private Long duration;
    @Column(name = "is_used")
    private int isUsed;
    @Column(name = "start_duration")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDuration;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_activity", nullable = false, insertable = false, updatable = false)
    private Activity activity;

    public PlayerActivities(final Long idPlayer, final Long idActivity, final Double quantity, final int activity,
                            final LocalDate startDate, final int actType, final Long duration, final int isUsed,
                            LocalDateTime startDuration) {
        this.idPlayer = idPlayer;
        this.idActivity = idActivity;
        this.quantity = quantity;
        this.active = activity;
        this.startDate = startDate;
        this.actType = actType;
        this.duration = duration;
        this.isUsed = isUsed;
        this.startDuration = startDuration;
    }

}
