package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Quest;
import shr.training_camp.repository.QuestRepository;
import shr.training_camp.sevice.interfaces.QuestService;

import java.time.LocalDate;
import java.util.Objects;

@Controller
public class QuestController extends AbstractEntityController {

    @Autowired
    private QuestService<Quest> questService;

    @Autowired
    private QuestRepository questRepository;

    @GetMapping("/listOfQuests")
    public String showListOfTheQuests(Model model, @RequestParam(value = "stage", required = false) String stage,
                                      @RequestParam(value = "deadlineFilterDate", required = false)  String deadlineFilterDate) {
        //return findPaginated(1, "deadline", "desc", stage, model);
        LocalDate deadlineFilter = null;
        if (Objects.isNull(stage)) {
            stage = "1";
        }
        if (Objects.nonNull(deadlineFilterDate) && !deadlineFilterDate.isEmpty()) {
            deadlineFilter = LocalDate.parse(deadlineFilterDate);
        }
        model.addAttribute("statisticByAllQuestsGroupedByStage", questService.getStatisticGroupedByStageByAllQuests());
        model.addAttribute("statisticByAllQuestsGroupedByType", questService.getStatisticGroupedByQuestTypeByAllQuests());
        model.addAttribute("statisticByAllQuestsGroupedByStageAndType", questService.getStatisticGroupedByStageAndType());
        return  findPaginated(1, "deadline", "asc", stage, deadlineFilter, model);
    }


    @GetMapping("/quests/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                String stage,
                                LocalDate deadlineFilter,
                                Model model) {
        int pageSize = 50;
        Page<Quest> page;
        if (stage == null) {
            page = questService.findPaginated(pageNo, pageSize, sortField, sortdir);
        } else {
            Sort sort = Sort.by(sortField);
            if (Objects.isNull(deadlineFilter)) {
                page = questService.findQuestByStagePage(PageRequest.of(pageNo - 1, pageSize, sort), stage);
            } else {
                page = questService.findQuestByStageAndDeadlinePage(PageRequest.of(pageNo - 1, pageSize, sort), stage, deadlineFilter);
            }
        }

        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listQuests", page.getContent());
        return "catalogues/quests/list_quests";
    }


    @GetMapping("/showNewQuestForm")
    public String showNewPQuestForm(Model model) {
        Quest quest = new Quest();
        model.addAttribute("quest", quest);
        model.addAttribute("defaultDate", LocalDate.now());
        model.addAttribute("parent", questRepository.findAll());
        return "catalogues/quests/new_quest";
    }

    @PostMapping("/saveQuest")
    public String saveQuest(@ModelAttribute("quest") Quest quest,
                            Model model) {
        //quest.setIdParent(quest.getQuest().getIdParent());
        questService.save(quest);
        findPaginated(1, "deadline", "asc", "1", null, model);
        return "catalogues/quests/list_quests";
    }

    @GetMapping("showFormForQuestUpdate/{id}")
    public String updateQuest(@PathVariable (value = "id") long id, Model model) {
        Quest quest = questService.getQuestById(id);
        model.addAttribute("quest", quest);
        return "catalogues/quests/update_quest";
    }

}
