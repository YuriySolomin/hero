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
import java.time.LocalDateTime;

@Entity
@Table(name = "tc_long_resource_activities")
@NoArgsConstructor
@Data
public class PetLog {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long idLog;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "start_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @Column(name = "duration")
    private Long duration;
    @Column(name = "message")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    public PetLog(Long idPlayer, LocalDateTime startDate, Long duration, String message) {
        this.idPlayer = idPlayer;
        this.startDate = startDate;
        this.duration = duration;
        this.message = message;
    }

}
