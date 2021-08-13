package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.*;
import shr.training_camp.repository.ActivityRepository;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.repository.ResourceRepository;
import shr.training_camp.sevice.interfaces.ILongResourceActivitiesService;
import shr.training_camp.sevice.interfaces.PlayerService;
import shr.training_camp.sevice.interfaces.PlayersResourcesService;
import shr.training_camp.sevice.interfaces.ResourceService;
import shr.training_camp.util.RandomUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class LongResourceActivitiesController extends AbstractEntityController{

    @Autowired
    private ILongResourceActivitiesService<LongResourceActivities> longResourceActivitiesService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PlayersResourcesService playersResourcesService;

    @GetMapping("/listOfLongResourceActivities")
    public String showListOfLongResourceActivities(Model model) {
        return findPaginated(1, "idPlayer", "asc", model);
    }

    @GetMapping("/showNewLongResourceActivityForm")
    public String showNewAutoActivityForm(Model model) {
        model.addAttribute("longResourceActivities", new LongResourceActivities());
        model.addAttribute("player", new Player());
        model.addAttribute("activity", new Activity());
        model.addAttribute("resource", new Resources());
        model.addAttribute("players", playerRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("resources", resourceRepository.findAll());
        model.addAttribute("startDate", LocalDate.now());
        return "catalogues/players/new_long_resource_activities";
    }

    @GetMapping("/showFormForUpdateLongResourceActivity/{id}")
    public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
        LongResourceActivities longResourceActivities = longResourceActivitiesService.getLongResourceActivitiesById(id);
        model.addAttribute("longResourceActivities", longResourceActivities);
        model.addAttribute("currentDate", LocalDateTime.now().withNano(0));
        return "catalogues/players/update_long_resource_activities";
    }

    @GetMapping("/showFormForUpdatePlayersResource/player/{idPlayer}/resource/{idResource}")
    public String showPlayerResourceFormForUpdate(@PathVariable("idPlayer") Long idPlayer,
                                                  @PathVariable("idResource") Long idResource,
                                                  Model model) {
        Player player = playerService.getPlayerById(idPlayer);
        Resources resources = resourceService.getResourceById(idResource);
        PlayersResources playersResources = playersResourcesService.getPRByPlayerAndResource(idPlayer, idResource);

        model.addAttribute("playerResource", new PlayersResources());
        model.addAttribute("savedPlayer", player);
        model.addAttribute("savedResource", resources);
        return "catalogues/players/list_long_resource_activities";
    }

    @PostMapping("/updatePRFromStorage")
    public String uploadPlayerResourceFromStorage(@ModelAttribute("longResourceActivities") LongResourceActivities longResourceActivities) {
        long idPlayer = longResourceActivities.getIdPlayer();
        long idResource = longResourceActivities.getIdResource();
        double storageQty = longResourceActivities.getOnStorage();
        long savedQty = (long) storageQty;
        double rest = storageQty - savedQty;
        PlayersResources playersResources = playersResourcesService.getPRByPlayerAndResource(idPlayer, idResource);
        long updatedQty = savedQty + playersResources.getQuantity();
        playersResources.setQuantity(updatedQty);
        longResourceActivities.setOnStorage(rest);;
        playersResourcesService.save(playersResources);
        longResourceActivitiesService.save(longResourceActivities);
        return "catalogues/players/list_long_resource_activities";
    }

    @PostMapping("/saveNewLRA")
    public String saveNewLRA(@ModelAttribute("longResourceActivities") LongResourceActivities longResourceActivities) {
        longResourceActivities.setIdPlayer(longResourceActivities.getPlayer().getId_player());
        longResourceActivities.setIdActivity(longResourceActivities.getActivity().getId());
        longResourceActivities.setIdResource(longResourceActivities.getResource().getIdResource());

        longResourceActivitiesService.save(longResourceActivities);
        return "catalogues/players/list_long_resource_activities";
    }

    @PostMapping("/updateOnStorageLRA")
    public String savePlayer(@ModelAttribute("longResourceActivities") LongResourceActivities longResourceActivities ) {
        LocalDateTime newStorageDate = LocalDateTime.now();
        Duration duration = Duration.between(longResourceActivities.getStorageDate(), newStorageDate);
        long diff = duration.getSeconds();
        double qty = longResourceActivities.getQuantity()*diff/3600;
        if (Objects.nonNull(longResourceActivities.getOnStorage())) {
            qty = qty + longResourceActivities.getOnStorage();
        }
        qty = Math.min(qty, 1000*longResourceActivities.getActLevel());
        longResourceActivities.setOnStorage(qty);
        longResourceActivities.setStorageDate(newStorageDate);
        longResourceActivitiesService.save(longResourceActivities);
        return "catalogues/players/list_long_resource_activities";
    }


    @GetMapping("/players_resource_activities/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 50;
        Page<LongResourceActivities> page = longResourceActivitiesService.findPaginated(pageNo, pageSize, sortField, sortdir);
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listOfLongResourceActivities", page.getContent());
        return "catalogues/players/list_long_resource_activities";
    }

}
