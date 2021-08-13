package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "tc_game_activity_events_log")
@NoArgsConstructor
@Data
public class GameActivityEventsLog {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_gael")
    private Long idGael;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_activity")
    private Long idActivity;
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "event_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;
    @Column(name = "event_type")
    private Integer eventType;
    @Column(name = "new_value")
    private Double newValue;
    @Column(name = "old_value")
    private Double oldValue;
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_activity", nullable = false, insertable = false, updatable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;

    public GameActivityEventsLog(Long idPlayer, Long idActivity, Long idGroup, LocalDate eventDate,
                                 Integer eventType, Double newValue, Double oldValue, String description) {
        this.idPlayer = idPlayer;
        this.idActivity = idActivity;
        this.idGroup = idGroup;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.description = description;
    }


}
