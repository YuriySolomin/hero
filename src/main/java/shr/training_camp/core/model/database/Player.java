package shr.training_camp.core.model.database;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
@Table(name = "tc_players")
@NoArgsConstructor
@Data
public class Player {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id_player;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "secret_name")
    private String secretName;
    private int gender;
    @Column(name = "player_type")
    private int playerType;

    public Player(String nickName, String firstName, String lastName, String secretName, int gender, int playerType) {
        this.nickName = nickName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secretName = secretName;
        this.gender = gender;
        this.playerType = playerType;
    }

}
