package shr.training_camp.controller.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.PlayerActivities;
import shr.training_camp.core.model.database.PlayersResources;
import shr.training_camp.games.search.SearchTemp;
import shr.training_camp.repository.ActivityRepository;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.sevice.interfaces.IActivityService;
import shr.training_camp.sevice.interfaces.IPlayerActivityService;
import shr.training_camp.sevice.interfaces.PlayerService;
import shr.training_camp.sevice.interfaces.PlayersResourcesService;
import shr.training_camp.sevice.interfaces.ResourceService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class SearchController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private IActivityService<Activity> activityService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private IPlayerActivityService<PlayerActivities> playerActivityService;

    @Autowired
    private PlayersResourcesService playersResources;

    @Autowired
    private PlayersResourcesService playersResourcesService;

    private static final String rangerProperty = "Следопыт";
    private static final String petProperty = "Питомец";


    @PostMapping("/startSearch")
    public String startSearch(@ModelAttribute("player") PlayerActivities playerActivities, Model model) {
        model.addAttribute("possibleActivities", activityService.findActivitiesByType(11));
        model.addAttribute("players", playerService.getAllPlayers());
        PlayerActivities playerActivities1 = new PlayerActivities();
        playerActivities1.setIdPlayer(playerActivities.getPlayer().getId_player());
        playerActivities1.setIdActivity(playerActivities.getActivity().getId());
        playerActivities1.setQuantity(1D);
        playerActivities1.setActive(0);
        playerActivities1.setStartDate(LocalDate.now());
        playerActivities1.setActType(11);
        playerActivities1.setIsUsed(1);
        playerActivities1.setStartDuration(LocalDateTime.now());
        playerActivityService.save(playerActivities1);
        model.addAttribute("allUsedActivities", playerActivityService.getPlayersActivitiesFilteredByUsed(1));
        return "catalogues/games/search_game";
    }

    @GetMapping("/startSearchGame")
    public String showSearchForm(@ModelAttribute("player") PlayerActivities playerActivities, Model model) {
        model.addAttribute("possibleActivities", activityService.findActivitiesByType(11));
        model.addAttribute("players", playerService.getAllPlayers());
        model.addAttribute("allUsedActivities", playerActivityService.getPlayersActivitiesFilteredByUsed(1));
        return "catalogues/games/search_game";
    }

    @GetMapping("/calcSearchResult/{id}")
    public String showFormForCalculationSearch(@PathVariable(value = "id") long id, Model model) {
        PlayerActivities playerActivities = playerActivityService.getPAById(id);
        model.addAttribute("stopDate", LocalDateTime.now());
        model.addAttribute("playerActivity", playerActivities);
        return "catalogues/games/search_results";
    }

    @PostMapping("/createSearchResults")
    public String calculateSearchReward(@ModelAttribute("playerActivities") PlayerActivities playerActivities, Model model) {
        //ToDo - вспомнить как с формы получить идентификатор
        //ToDo - получить объект активность по идентификатору или взять из модели
        //ToDo - На основании параметра стоп дата и базы данных рассчитать длительность, и если она превышает указанное значение, то запустить механихм поиска
        //ToDo - если меньше значения, то установить параметр isUsed = 3 (fail)
        //ToDo - первый механизм поиска будет работать лишь для одной активности и сбора пота хозяйки
        Duration duration = Duration.between(playerActivities.getStartDuration(), LocalDateTime.now());
        long minutes = duration.toMinutes();
        playerActivities.setDuration(minutes);
        if (minutes>= 60) {
            playerActivities.setIsUsed(2);
            int count = (int)minutes/5;
            SearchTemp searchTemp = new SearchTemp(0.7, 9L, count);
            Map<Long, Integer> map = searchTemp.search();
            for (Map.Entry<Long, Integer> m: map.entrySet()) {
                PlayersResources playersResources = new PlayersResources(
                        playerService.getPlayerById(1L),
                        resourceService.getResourceById(m.getKey()),
                        (long)m.getValue()
                );
                playersResourcesService.save(playersResources);
            }
        } else {
            playerActivities.setIsUsed(3);
        }
        playerActivityService.save(playerActivities);
        model.addAttribute("stopDate", LocalDateTime.now());
        model.addAttribute("playerActivity", playerActivities);

        return "catalogues/games/search_results";

    }

}
