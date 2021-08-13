package shr.training_camp.sevice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import shr.training_camp.core.model.database.Quest;
import shr.training_camp.core.model.database.addition.QuestStatistics;
import shr.training_camp.repository.QuestRepository;
import shr.training_camp.sevice.interfaces.QuestService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QuestServiceImpl extends AbstractEntityServiceImpl implements QuestService {

    @Autowired
    private QuestRepository questRepository;

    @Override
    public Page<Quest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.questRepository.findAll(pageable);
    }

    @Override
    public Page<Quest> findQuestByStagePage(Pageable pageable, @Param("stage") String stage) {
        return this.questRepository.findQuestByStagePage(pageable, stage);
    }

    @Override
    public Page<Quest> findQuestByStageAndDeadlinePage(Pageable pageable, String stage, LocalDate deadlineFilter) {
        return this.questRepository.findQuestByStageAndDeadlinePage(pageable, stage, Date.valueOf(deadlineFilter));
    }

    @Override
    public Page<Quest> findPaginatedStage(int pageNo, int pageSize, String sortField, String sortDirection, String stage) {
        setPaginated(pageNo, pageSize, sortField, sortDirection);
        return this.questRepository.findQuestByStagePage(pageable, stage);
    }

    @Override
    public void save(Quest quest) {
        this.questRepository.save(quest);
    }

    @Override
    public Quest getQuestById(long id) {
        Optional<Quest> optional = questRepository.findById(id);
        Quest quest = null;
        if(optional.isPresent()) {
            quest = optional.get();
        } else {
            throw new RuntimeException("Quest not found for id :: "  + id);
        }
        return quest;
    }

    @Override
    public List<Quest> findQuestByStage(String stage) {
        return this.questRepository.findQuestByStage(stage);
    }

    @Override
    public List<QuestStatistics> getStatisticGroupedByStageByAllQuests() {
        return questRepository.getStatisticGroupedByStageByAllQuests();
    }

    @Override
    public List<QuestStatistics> getStatisticGroupedByStageByAllQuests(LocalDate date) {
        return questRepository.getStatisticGroupedByStageByAllQuests(Date.valueOf(date));
    }

    @Override
    public List<QuestStatistics> getStatisticGroupedByQuestTypeByAllQuests() {
        return questRepository.getStatisticGroupedByQuestTypeByAllQuests();
    }

    @Override
    public List<QuestStatistics> getStatisticGroupedByStageAndType() {
        return questRepository.getStatisticGroupedByStageAndType();
    }
}
