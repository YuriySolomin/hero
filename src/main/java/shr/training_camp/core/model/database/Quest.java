package shr.training_camp.core.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tc_quests")
@NoArgsConstructor
@Data
public class Quest {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_quest")
    private Long idQuest;
    @Column(name = "id_parent")
    private Long idParent;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "stage")
    private int stage;
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Column(name = "deadline")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    @Column(name = "quest_type")
    private int questType;

    /*@OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_parent", insertable = false, updatable = false)
    private Quest quest;
*/

    public Quest(final Long idParent, final String name, final String description, final LocalDate startDate,
                 final int stage, final LocalDate endDate, final LocalDate deadline, final int questType) {
        this.idParent = idParent;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.stage = stage;
        this.endDate = endDate;
        this.deadline = deadline;
        this.questType = questType;
    }
}
