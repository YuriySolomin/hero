package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tc_players_groups")
@NoArgsConstructor
@Data
public class PlayersGroups {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "description")
    private String description;
    @Column(name = "is_hero")
    private Integer isHero;
    @Column(name = "height")
    private Double height;
    @Column(name = "actual_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualDate;
    @Column(name = "active_status")
    private Integer activeStatus;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;

    public PlayersGroups(final Long idPlayer, final Long idGroup, final String description, final Integer is_hero,
                         final Double height, final LocalDate actualDate) {
        this.idPlayer = idPlayer;
        this.idGroup = idGroup;
        this.description = description;
        this.isHero = is_hero;
        this.height = height;
        this.actualDate = actualDate;
    }
}
