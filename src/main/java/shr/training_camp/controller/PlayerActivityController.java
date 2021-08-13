package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.AutoActivity;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayerActivities;
import shr.training_camp.core.model.database.addition.PlayerActivityStatistics;
import shr.training_camp.core.model.database.calculation.AutoActivitiesCalculation;
import shr.training_camp.repository.ActivityRepository;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.sevice.interfaces.IActivityService;
import shr.training_camp.sevice.interfaces.IAutoActivityService;
import shr.training_camp.sevice.interfaces.IPlayerActivityService;
import shr.training_camp.sevice.interfaces.PlayerService;

import java.time.LocalDate;
import java.util.List;


@Controller
public class PlayerActivityController extends AbstractEntityController {

    @Autowired
    private IPlayerActivityService<PlayerActivities> playerActivitiesService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private AutoActivitiesCalculation autoActivitiesCalculation;

    @Autowired
    private IAutoActivityService<AutoActivity> autoActivityService;


    @GetMapping("/listOfPlayerActivities")
    public String showListOfThePlayers(Model model) {
        return findPaginated(1, "idPlayer", "asc", model);
    }

    @GetMapping("/showNewPlayerActivityForm")
    public String showNewAutoActivityForm(Model model) {
        model.addAttribute("playerActivity", new PlayerActivities());
        model.addAttribute("player", new Player());
        model.addAttribute("activity", new Activity());
        model.addAttribute("players", playerRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("defaultDate", LocalDate.now());
        model.addAttribute("listPlayerActivitiesForToday", playerActivitiesService.getGroupedActivitiesForToday(1L));
        LocalDate filterDate = LocalDate.parse("2021-05-17");
        model.addAttribute("listPlayerActivitiesForCurrentGroup", playerActivitiesService.getHeroActivityResultByDate(1L, filterDate));
        model.addAttribute("listPlayerActivitiesForWeek",
                playerActivitiesService.getHeroActivityResultByDate(1L, LocalDate.now().minusDays(7)));

        return "catalogues/players/new_player_activities";
    }

    @GetMapping("/showNewPlayerActivityForm2/player/{playerId}/activity/{activityId}")
    public String showNewPlayersActivityForm(@PathVariable("playerId") Long playerId,
                                             @PathVariable("activityId") Long activityId,
                                             Model model) {
        Player player = playerService.getPlayerById(playerId);
        Activity activity = activityService.getActivityById(activityId);
        model.addAttribute("playerS", player.getId_player());
        model.addAttribute("activityS", activity.getId());
        model.addAttribute("playerActivity", new PlayerActivities());
        model.addAttribute("player", new Player());
        model.addAttribute("activity", new Activity());
        model.addAttribute("players", playerRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("defaultDate", LocalDate.now());
        model.addAttribute("listPlayerActivitiesForToday", playerActivitiesService.getGroupedActivitiesForToday(playerId));
        LocalDate filterDate = LocalDate.parse("2021-05-17");
        model.addAttribute("listPlayerActivitiesForCurrentGroup", playerActivitiesService.getHeroActivityResultByDate(1L, filterDate));
        model.addAttribute("listPlayerActivitiesForWeek",
                playerActivitiesService.getHeroActivityResultByDate(playerId, LocalDate.now().minusDays(7)));
        return "catalogues/players/new_player_activities";

    }

    @GetMapping("/players_activities/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 50;
        Page<PlayerActivities> page = playerActivitiesService.findPaginated(pageNo, pageSize, sortField, sortdir);
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listOfPlayerActivities", page.getContent());
        return "catalogues/players/list_players_activities";
    }

    @GetMapping("players_activity/grouped/idPlayer={id}")
    public String findGroupedByAll(@PathVariable (value = "id") long id, Model model) {
        List<PlayerActivityStatistics> playerActivities = playerActivitiesService.findGroupedByAll(id);
        model.addAttribute("listOfPlayerActivitiesGroupedByAll", playerActivities);
        return "catalogues/players/list_players_grouped_activities";
    }



    @GetMapping("addQuickActivity")
    public String addActivityQuickMode(@ModelAttribute("playerActivities") PlayerActivities playerActivities) {
        playerActivitiesService.save(playerActivities);
        return "catalogues/players/list_players_grouped_activities";
    }

    @PostMapping("/savePlayerActivity")
    public String savePlayer(@ModelAttribute("player") PlayerActivities playerActivities, Model model ) {
        playerActivities.setIdPlayer(playerActivities.getPlayer().getId_player());
        playerActivities.setIdActivity(playerActivities.getActivity().getId());
        playerActivitiesService.save(playerActivities);
        model.addAttribute("listOfPlayerActivities", playerActivitiesService.getAllPlayerActivities());
        model.addAttribute("playerActivity", new PlayerActivities());
        model.addAttribute("player", new Player());
        model.addAttribute("activity", new Activity());
        model.addAttribute("players", playerRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("defaultDate", LocalDate.now());
        model.addAttribute("listPlayerActivitiesForToday",
                playerActivitiesService.getGroupedActivitiesForToday(playerActivities.getPlayer().getId_player()));
        LocalDate filterDate = LocalDate.parse("2021-05-17");
        model.addAttribute("listPlayerActivitiesForCurrentGroup", playerActivitiesService.getHeroActivityResultByDate(1L, filterDate));
        model.addAttribute("listPlayerActivitiesForWeek",
                playerActivitiesService.getHeroActivityResultByDate(playerActivities.getPlayer().getId_player(),
                        LocalDate.now().minusDays(7)));

        return "catalogues/players/new_player_activities";
    }

    @PostMapping("/showPAForPeriod")
    public String showPAFilteredByDate(@RequestParam(value = "startFilterDate") String filterDateParam, Model model) {
        LocalDate filterDate = LocalDate.parse(filterDateParam);
        model.addAttribute("listPlayerActivitiesForPeriod", playerActivitiesService.getHeroActivityResultByDate(1L, filterDate));
        model.addAttribute("playerActivity", new PlayerActivities());
        model.addAttribute("player", new Player());
        model.addAttribute("activity", new Activity());
        model.addAttribute("players", playerRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("defaultDate", LocalDate.now());
        return "catalogues/players/new_player_activities";
    }

    @PostMapping("/calculateActivity")
    public String showCalculateForm(@ModelAttribute (value = "player") Player player,
                                    //@RequestParam(value = "idActivity") long idActivity,
                                    @RequestParam(value = "actualDate") String actualDateParam,
                                    Model model) {

        LocalDate actualDate = LocalDate.parse(actualDateParam);
        //model.addAttribute("activitiesAreInAuto", autoActivityService.getActivitiesArePresentInTheAA(idPlayer));
        //model.addAttribute("idPlayer", idPlayer);
        //Long idPlayer = player.getId_player();
        Long idPlayer = 330L;
        Long idActivity = 13L;
        AutoActivity autoActivity = autoActivitiesCalculation.getActivityValueForPlayer(idPlayer, idActivity, actualDate);
        autoActivityService.save(autoActivity);
        return "statistic/calculation_activity";
    }

    @GetMapping("/showCalculateActivityForm/idPlayer/{idPlayer}")
    public String calculateActivityForPlayer(@PathVariable(value = "idPlayer") Long idPlayer,
                                             Model model) {
        model.addAttribute("player", playerService.getPlayerById(idPlayer));
        model.addAttribute("autoActivity", new AutoActivity());
        model.addAttribute("activitiesAreInAuto", activityRepository.findAll());

        //model.addAttribute("activitiesAreInAuto", autoActivityService.getActivitiesArePresentInTheAA(idPlayer));
        return "statistic/calculation_activity";
    }

}
