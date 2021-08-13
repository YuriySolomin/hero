package shr.training_camp.sevice.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import shr.training_camp.core.model.database.Quest;
import shr.training_camp.core.model.database.addition.QuestStatistics;

import java.time.LocalDate;
import java.util.List;

public interface QuestService<T> extends AbstractEntityService<T> {

    void save(Quest quest);

    Quest getQuestById(long id);

    List<Quest> findQuestByStage(final String stage);

    Page<Quest> findQuestByStagePage(Pageable pageable, @Param("stage") String stage);

    Page<Quest> findQuestByStageAndDeadlinePage(Pageable pageable, @Param("stage") String stage, final LocalDate deadlineFilter);

    Page<Quest> findPaginatedStage(int pageNo, int pageSize, String sortField, String sortDirection, String stage);

    List<QuestStatistics> getStatisticGroupedByStageByAllQuests();

    List<QuestStatistics> getStatisticGroupedByStageByAllQuests(LocalDate date);

    List<QuestStatistics> getStatisticGroupedByQuestTypeByAllQuests();

    List<QuestStatistics> getStatisticGroupedByStageAndType();

}
