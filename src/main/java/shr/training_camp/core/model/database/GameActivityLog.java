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
@Table(name = "tc_game_activity_log")
@NoArgsConstructor
@Data
public class GameActivityLog {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_gal")
    private Long idGal;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_activity")
    private Long idActivity;
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "save_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate saveDate;
    @Column(name = "value")
    private Double value;
    @Column(name = "random_value")
    private Double randomValue;
    @Column(name = "bonus_value")
    private Double bonusValue;
    @Column(name = "first_element")
    private Double firstElement;
    @Column(name = "days_factor")
    private Integer daysFactor;
    @Column(name = "next_element")
    private Double nextElement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_activity", nullable = false, insertable = false, updatable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;


    public GameActivityLog(Player player, Activity activity, GrowGroup growGroup, LocalDate saveDate,
                           Double value, Double randomValue, Double bonusValue,
                           Double firstElement, Integer daysFactor, Double nextElement) {
        this.player = player;
        this.activity = activity;
        this.growGroup = growGroup;
        this.saveDate = saveDate;
        this.value = value;
        this.randomValue = randomValue;
        this.bonusValue = bonusValue;
        this.firstElement = firstElement;
        this.daysFactor = daysFactor;
        this.nextElement = nextElement;
    }

    public GameActivityLog(Long idPlayer, Long idActivity, Long idGroup, LocalDate saveDate,
                           Double value, Double randomValue, Double bonusValue,
                           Double firstElement, Integer daysFactor, Double nextElement) {
        this.idPlayer = idPlayer;
        this.idActivity = idActivity;
        this.idGroup = idGroup;
        this.saveDate = saveDate;
        this.value = value;
        this.randomValue = randomValue;
        this.bonusValue = bonusValue;
        this.firstElement = firstElement;
        this.daysFactor = daysFactor;
        this.nextElement = nextElement;
    }




}
