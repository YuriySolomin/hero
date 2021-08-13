package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tc_players_properties")
@NoArgsConstructor
@Data
public class PlayerProperty {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_pp")
    private Long id;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_property")
    private Long idProperty;
    @Column(name = "value")
    private Double value;
    @Column(name = "description")
    private String description;
    @Column(name = "experience")
    private Integer experience;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_player", nullable = false, insertable = false, updatable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_property", nullable = false, insertable = false, updatable = false)
    private Property property;

    public PlayerProperty(final Long idPlayer, final Long idProperty, final Double value, final String description,
                          final Integer experience) {
        this.idPlayer = idPlayer;
        this.idProperty = idProperty;
        this.value = value;
        this.description = description;
        this.experience = experience;
    }


}
