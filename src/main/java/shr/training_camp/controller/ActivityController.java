package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.repository.ActivityRepository;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.sevice.interfaces.IActivityService;

import java.util.List;

@Controller
public class ActivityController extends AbstractEntityController{

    @Autowired
    private IActivityService<Activity> activityService;


    @GetMapping("/listOfActivities")
    public String showListOfTheQuests(Model model) {
        return findPaginated(1, "name", "asc", model);
    }

    @GetMapping("/showNewActivityForm")
    public String showNewActivityForm(Model model) {
        Activity activity = new Activity();
        model.addAttribute("activity", activity);
        return "catalogues/activities/new_activity";
    }

    @PostMapping("/saveActivity")
    public String savePlayer(@ModelAttribute("player") Activity activity ) {
        activityService.save(activity);
        return "redirect:/";
    }

    @GetMapping("/auto_activities/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 25;
        Page<Activity> page = activityService.findPaginated(pageNo, pageSize, sortField, sortdir);
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listActivities", page.getContent());
        return "catalogues/activities/list_activities";
    }

}
