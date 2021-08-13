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
@Table(name = "tc_game_activity_grow_log")
@NoArgsConstructor
@Data
public class GameActivityGrowLog {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_gagl")
    private Long idGal;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "convert_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate convertDate;
    @Column(name = "grow")
    private Double grow;
    @Column(name = "factor")
    private Double factor;
    @Column(name = "factor2")
    private Double factor2;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;

    public GameActivityGrowLog(Player player, GrowGroup growGroup, LocalDate convertDate, Double grow, Double factor) {
        this.player = player;
        this.growGroup = growGroup;
        this.convertDate = convertDate;
        this.grow = grow;
        this.factor = factor;
    }

    public GameActivityGrowLog(Long idPlayer, Long idGroup, LocalDate convertDate, Double grow, Double factor, Double factor2) {
        this.idPlayer = idPlayer;
        this.idGroup = idGroup;
        this.convertDate = convertDate;
        this.grow = grow;
        this.factor = factor;
        this.factor = factor2;
    }

}
