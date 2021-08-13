package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_players_resources")
@NoArgsConstructor
@Data
public class PlayersResources {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_pr")
    private Long id;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_resource")
    private Long idResource;
    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_resource", nullable = false, insertable = false, updatable = false)
    private Resources resource;

    public PlayersResources(Player player, Resources resources, Long quantity) {
        this.player = player;
        this.idPlayer = player.getId_player();
        this.resource = resources;
        this.idResource = resources.getIdResource();
        this.quantity = quantity;
    }
}
