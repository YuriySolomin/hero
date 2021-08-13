package shr.training_camp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shr.training_camp.core.model.database.Quest;
import shr.training_camp.core.model.database.addition.QuestStatistics;

import java.sql.Date;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {


    @Query(value = "select * from tc_quests where stage in (:stage)", nativeQuery = true)
    List<Quest> findQuestByStage(@Param("stage") String stage);

    @Query(value = "select * from tc_quests where stage = :stage", nativeQuery = true)
    Page<Quest> findQuestByStagePage(Pageable pageable, @Param("stage") String stage);

    @Query(value = "select * from tc_quests where stage = :stage " +
            "and deadline <= :deadline", nativeQuery = true)
    Page<Quest> findQuestByStageAndDeadlinePage(Pageable pageable, @Param("stage") String stage, @Param("deadline") Date deadline);
    @Query(value = "select stage, count(*) as quantity from tc_quests group by stage", nativeQuery = true)
    List<QuestStatistics> getStatisticGroupedByStageByAllQuests();

    @Query(value = "select quest_type as questType, count(*) as quantity from tc_quests group by quest_type", nativeQuery = true)
    List<QuestStatistics> getStatisticGroupedByQuestTypeByAllQuests();

    @Query(value="select stage, count(*) as quantity from tc_quests where start_date >= ':startDate' group by stage", nativeQuery = true)
    List<QuestStatistics> getStatisticGroupedByStageByAllQuests(@Param("startDate") Date startDate);

    @Query(value = "select stage, count(stage) as quantity, quest_type, count(quest_type) as typeQuantity from tc_quests group by stage, quest_type", nativeQuery = true)
    List<QuestStatistics> getStatisticGroupedByStageAndType();

    @Query(value = "select stage, count(stage) as quantity, quest_type, count(quest_type) as typeQuantity " +
            "from tc_quests " +
            "where start_date between :startDate and :endDate " +
            "group by stage, quest_type", nativeQuery = true)
    List<QuestStatistics> getStatisticGroupedByStageAndTypeFromDiapason(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
