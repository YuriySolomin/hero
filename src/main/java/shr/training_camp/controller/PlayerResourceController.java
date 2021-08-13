package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersResources;
import shr.training_camp.core.model.database.Resources;
import shr.training_camp.repository.PlayerRepository;
import shr.training_camp.repository.ResourceRepository;
import shr.training_camp.sevice.interfaces.PlayerService;
import shr.training_camp.sevice.interfaces.PlayersResourcesService;
import shr.training_camp.sevice.interfaces.ResourceService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class PlayerResourceController {

    @Autowired
    private PlayersResourcesService playersResourcesService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ResourceService resourceService;


    @GetMapping("/listOfPlayersResources")
    public String showListOfThePlayers(Model model) {

        return findPaginated(1, "idPlayer", "asc", model);
    }

    @GetMapping("/showNewPlayerResourceForm")
    public String showNewAutoActivityForm(Model model) {
        model.addAttribute("playerResource", new PlayersResources());
        model.addAttribute("player", new Player());
        model.addAttribute("resource", new Resources());
        model.addAttribute("players", playerRepository.findAll());
        model.addAttribute("resources", resourceRepository.findAll());
        return "catalogues/players/new_player_resources";
    }

    @GetMapping("/showFormForUpdatePlayersResource22/player/{idPlayer}/resource/{idResource}")
    public String showPlayerResourceFormForUpdate(@PathVariable("idPlayer") Long idPlayer,
                                                  @PathVariable("idResource") Long idResource,
                                                  Model model) {
        Player player = playerService.getPlayerById(idPlayer);
        Resources resources = resourceService.getResourceById(idResource);
        model.addAttribute("playerResource", new PlayersResources());
        model.addAttribute("savedPlayer", player);
        model.addAttribute("savedResource", resources);
        return "";
    }

    @PostMapping("/savePlayerResource")
    public String savePlayer(@ModelAttribute("player") PlayersResources playersResources, Model model ) {
        /*PlayersResources existingObject = playersResourcesService
                .getPRByPlayerAndResource(playersResources.getPlayer().getId_player(), playersResources.getResource().getIdResource());
        if (Objects.nonNull(existingObject)) {
            existingObject.setQuantity(existingObject.getQuantity() + playersResources.getQuantity());
            playersResourcesService.save(existingObject);
        } else {
            playersResources.setIdPlayer(playersResources.getPlayer().getId_player());
            playersResources.setIdResource(playersResources.getResource().getIdResource());
            playersResourcesService.save(playersResources);
        }*/
        playersResourcesService.save(playersResources);
        return "catalogues/players_resources/list_players_resources";
    }


    @GetMapping("/players_resources/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 5;
        Page<PlayersResources> page = playersResourcesService.findPaginated(pageNo, pageSize, sortField, sortdir);
        List<PlayersResources> listResources = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortdir);
        model.addAttribute("reverseSortDir", sortdir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listOfPlayersResources", listResources);
        return "catalogues/players_resources/list_players_resources";
    }

}
