package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.AutoActivity;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.addition.AutoActivitiesPlayerGroups;
import shr.training_camp.core.model.database.addition.HeroActivityResult;
import shr.training_camp.core.model.database.addition.PlayerActivityStatistics;
import shr.training_camp.core.model.database.calculation.AutoActivitiesCalculation;
import shr.training_camp.core.model.database.calculation.PlaceRecommendations;
import shr.training_camp.core.model.objects.ActivityStatistic;
import shr.training_camp.core.model.objects.RecommendationSetup;
import shr.training_camp.repository.ActivityRepository;
import shr.training_camp.repository.PlayerActivityRepository;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.sevice.interfaces.IAutoActivityService;
import shr.training_camp.sevice.interfaces.IPlayerActivityService;
import shr.training_camp.sevice.interfaces.IPlayerGroupService;
import shr.training_camp.sevice.interfaces.PlayerService;
import shr.training_camp.util.MathUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
public class AutoActivityController extends AbstractEntityController {

    @Autowired
    private IAutoActivityService<AutoActivity> autoActivityService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private IPlayerActivityService playerActivityService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PlayerActivityRepository playerActivityRepository;

    @Autowired
    private AutoActivitiesCalculation autoActivitiesCalculation;

    @Autowired
    private IPlayerGroupService<PlayersGroups> playerGroupService;

    private Long groupFilterId;

    private Long autoActivityFilterId;

    private Long playerFilterId;

    @GetMapping("/listOfAutoActivities")
    public String showListOfThePlayers(Model model) {
        return findPaginated(1, "idPlayer", "asc", model);
    }

    @GetMapping("/showNewAutoActivityForm")
    public String showNewAutoActivityForm(Model model) {
        model.addAttribute("autoActivity", new AutoActivity());
        model.addAttribute("player", new Player());
        model.addAttribute("activity", new Activity());
        model.addAttribute("players", playerRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        return "catalogues/activities/new_auto_activity";
    }

    @GetMapping("/activities/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 2500;
        Page<AutoActivity> page;;
        if (Objects.isNull(autoActivityFilterId) || autoActivityFilterId == 0L) {
            page = autoActivityService.findPaginated(pageNo, pageSize, sortField, sortdir);
        } else {
            Sort sort = Sort.by(sortField).descending();
            page = autoActivityService.findAutoActivitiesByGroupAndActivity(PageRequest.of(pageNo - 1, pageSize, sort), groupFilterId, autoActivityFilterId);
        }
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listOfAutoActivities", page.getContent());
        return "catalogues/activities/list_auto_activities";
    }

    @GetMapping("/listOfFilteredAutoActivities")
    public String showListOfFilteredAA(Model model, @RequestParam(value = "groupId", required = false) Long groupId,
                                       @RequestParam(value = "autoActivityId", required = false) Long autoActivityId,
                                       @RequestParam(value = "playerId", required = false) Long playerId) {
        groupFilterId = Objects.nonNull(groupId) ? groupId : 4L;
        autoActivityFilterId = Objects.nonNull(autoActivityId) ? autoActivityId : 0L;
        playerFilterId = Objects.nonNull(playerId) ? playerId : 0L;
        return findPaginated(1, "a_first", "desc", model);
    }

    @GetMapping("/listWithAutoActivitiesInfo/{id}")
    public String showListOfAutoActivitiesInfo(@PathVariable("id") Long id, @RequestParam(value = "idActivity", required = false) Long idActivity, Model model) {
        return findPaginated(1, "nickName", "asc", id, idActivity, model);
    }

    @GetMapping("/getRecommendationForPlace")
    public String getRecommendationForPlace(@RequestParam("place") int place, @RequestParam("days") int days
            , @RequestParam(value = "idGroup", required = false) Long idGroup, Model model) {
        Long groupId = Objects.isNull(idGroup) ? 4L : idGroup;
        PlayersGroups heroPlayer = playerGroupService.findPlayersGroupsByGroupId(groupId, 0).get(0);
        List<AutoActivity> groupActivity = autoActivityService.getAllAutoActivitiesByPlayerId(heroPlayer.getIdPlayer());
        List<PlaceRecommendations> placeRecommendationsList = new ArrayList<>();
        for (AutoActivity autoActivity : groupActivity) {
            List<AutoActivitiesPlayerGroups> list = playerGroupService.findAutoActivitiesForPlayerGroups(groupId, autoActivity.getIdActivity());
            AutoActivitiesPlayerGroups playerInfo = list.stream().skip(place - 1).findFirst().get();
            placeRecommendationsList.add(PlaceRecommendations.of(playerInfo.getNickName(),
                    autoActivity.getActivity().getName(),
                    playerInfo.getFirstElement(),
                    playerInfo.getRatio(),
                    playerInfo.getBonusValue(),
                    playerInfo.getRandomValue(),
                    playerInfo.getDaysFactor()));

        }
        model.addAttribute("listOfGroupedAutoActivities", playerGroupService.findAutoActivitiesForPlayerGroups(groupId));
        model.addAttribute("listRecommendations", placeRecommendationsList);
        return "catalogues/activities/list_grouped_auto_activities";
    }

    @GetMapping("/listOfGroupedAndFilteredAutoActivities")
    public String showListOfAutoActivitiesInfoFilteredByActivity(@RequestParam("idActivity") Long idActivity, Model model) {
        // ToDo replace after adding criteria
        return findPaginated(1, "nickName", "asc", 4L, idActivity, model);
    }


    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                @RequestParam("id") Long groupId,
                                @RequestParam(value = "idActivity", required = false) Long idActivity,
                                Model model) {
        int pageSize = 2500;
        //Page<AutoActivitiesPlayerGroups> page = playerGroupService.findAutoActivitiesForPlayerGroups(PageRequest.of(pageNo - 1, pageSize, Sort.by(sortField)), groupId);
        List<AutoActivitiesPlayerGroups> list = null;
        if (Objects.isNull(idActivity)) {
            list = playerGroupService.findAutoActivitiesForPlayerGroups(groupId);
        } else {
            list = playerGroupService.findAutoActivitiesForPlayerGroups(groupId, idActivity);
        }


        //Page<AutoActivitiesPlayerGroups> page = new PageImpl<AutoActivitiesPlayerGroups>()
        //setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        //model.addAttribute("listOfGroupedAutoActivities", page.getContent());
        model.addAttribute("listOfGroupedAutoActivities", list);
        return "catalogues/activities/list_grouped_auto_activities";


    }


    @PostMapping("/saveAutoActivity")
    public String savePlayer(@ModelAttribute("autoActivity") AutoActivity autoActivity ) {
        //autoActivity.setStartDate(LocalDate.now());
        autoActivity.setIdPlayer(autoActivity.getPlayer().getId_player());
        autoActivity.setIdActivity(autoActivity.getActivity().getId());
        autoActivityService.save(autoActivity);
        return "catalogues/activities/list_auto_activities";
    }

    @GetMapping("/showStatistics/idPlayer/{idPlayer}")
    public String showStatisticByPlayer(@PathVariable (value = "idPlayer") long idPlayer, Model model) {
        Player player = playerService.getPlayerById(idPlayer);
        List<AutoActivity> autoActivities = autoActivityService.getAllAutoActivitiesByPlayerId(idPlayer);
        List<ActivityStatistic> activityStatistics = new ArrayList<>();
        for (AutoActivity autoActivity: autoActivities) {
            ActivityStatistic activityStatistic = new ActivityStatistic();
            //HeroActivityResult heroActivityResult =
            List<PlayerActivityStatistics> heroActivityResult =
                    playerActivityService.getHeroActivityResultByDate2(autoActivity.getIdActivity(), autoActivity.getStartDate());
                    //playerActivityService.getHeroActivityResult(autoActivity.getIdActivity());
            activityStatistic.setPlayer(player);
            activityStatistic.setAutoActivity(autoActivity);
            activityStatistic.setLocalDate(LocalDate.now());
            long days = ChronoUnit.DAYS.between(autoActivity.getStartDate(), LocalDate.now());
            activityStatistic.setSummary(autoActivity.getInitValue() + MathUtils.getSumOfAP(autoActivity.getFirstElement(), autoActivity.getRatio(), days));
            //activityStatistic.setHeroActivityResult(heroActivityResult);
            activityStatistic.setQuantity(heroActivityResult.size() > 0 ? heroActivityResult.get(0).getQuantity() : 0.0);
            activityStatistics.add(activityStatistic);
        }
        double factor = 0.00;
        /*for (ActivityStatistic statistic: activityStatistics) {
            factor+= statistic.getHeroActivityResult().getQuantity() / statistic.getSummary();
        }*/
        factor = 100*factor / activityStatistics.size();
        model.addAttribute("autoActivities", activityStatistics);
        model.addAttribute("factor", factor);
        model.addAttribute("idPlayer", idPlayer);
        return "statistic/activity";
    }

    @PostMapping("/showRecommendation")
    public String showRecommendationByPlayer(@ModelAttribute("recommendationSetup") RecommendationSetup recommendationSetup,
                                             Model model) {
        Map<String, Double> recommendations =
                autoActivitiesCalculation.getHeroRecommendedValuesNamesByActivities(recommendationSetup.getIdPlayer(),
                        recommendationSetup.getShiftDays());
        model.addAttribute("recommendedMap", recommendations);
        return "statistic/recommendation";
    }

    @GetMapping("recommendationActivityDebt/idPlayer/{id}")
    public String openRecommendationActivityDebt(@PathVariable (value = "id") long idPlayer,
                                                 Model model) {
        model.addAttribute("idPlayer", idPlayer);
        return "statistic/recommendation";
    }


}
