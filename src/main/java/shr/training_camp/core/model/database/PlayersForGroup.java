package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_players_for_group")
@NoArgsConstructor
@Data
public class PlayersForGroup {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_pfg")
    private Long idP;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_group")
    private Long idGroup;
    @Column(name = "player_status")
    private int playerStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false, insertable = false, updatable = false)
    private GrowGroup growGroup;

    public PlayersForGroup(Long idPlayer, Long idGroup, int playerStatus) {
        this.idPlayer = idPlayer;
        this.idGroup = idGroup;
        this.playerStatus = playerStatus;
    }

    public PlayersForGroup(Player player, GrowGroup group, int playerStatus) {
        this.player = player;
        this.growGroup = group;
        this.playerStatus = playerStatus;
    }

}
