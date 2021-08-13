package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tc_long_resource_activities")
@NoArgsConstructor
@Data
public class LongResourceActivities {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_activity")
    private Long idActivity;
    @Column(name = "id_resource")
    private Long idResource;
    @Column(name = "act_level")
    private Long actLevel;
    @Column(name = "quantity")
    private Double quantity;
    @Column(name = "start_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @Column(name = "end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    @Column(name = "storage_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime storageDate;
    @Column(name = "on_storage")
    private Double onStorage;
    @Column(name = "isActive")
    private Integer isActive;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_activity", nullable = false, insertable = false, updatable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_resource", nullable = false, insertable = false, updatable = false)
    private Resources resource;


    public LongResourceActivities(Long idPlayer, Long idActivity, Long idResource, Long actLevel, Double quantity, LocalDateTime startDate,
                                  LocalDateTime endDate, LocalDateTime storageDate, Double onStorage, Integer isActive) {
        this.idPlayer = idPlayer;
        this.idActivity = idActivity;
        this.idResource = idResource;
        this.actLevel = actLevel;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.storageDate = storageDate;
        this.onStorage = onStorage;
        this.isActive = isActive;
    }

}
