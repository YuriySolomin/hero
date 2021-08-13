package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tc_debts")
@NoArgsConstructor
@Data
public class Debts {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_debt")
    private Long idDebt;
    @Column(name = "id_parent")
    private Long idParent;
    @Column(name = "description")
    private String description;
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "stage")
    private Integer stage;
    @Column(name = "id_player")
    private Long idPlayer;
    @Column(name = "id_virtual")
    private Long idVirtual;
    @Column(name = "addition_text")
    private String additionText;

    public Debts(Long idParent, String description, LocalDate startDate, LocalDate deadline,
                 Integer stage, Long idPlayer, Long idVirtual, String additionText) {
        this.idParent = idParent;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline;
        this.stage = stage;
        this.idPlayer = idPlayer;
        this.idVirtual = idVirtual;
        this.additionText = additionText;
    }

}
