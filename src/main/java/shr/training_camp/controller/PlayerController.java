package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSOutput;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.PlayersGroups;
import shr.training_camp.core.model.database.addition.PlayerResourceStatistics;
import shr.training_camp.sevice.interfaces.PlayerService;
import shr.training_camp.sevice.interfaces.PlayersResourcesService;
import shr.training_camp.util.RandomUtils;
import shr.training_camp.util.generation.Upload;

import java.util.List;

@Controller
public class PlayerController {

    // display list of players

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayersResourcesService playersResourcesService;

    @Autowired
    private Upload upload;

    /*@GetMapping("/")
    public String viewHomePage(Model model) {
        //model.addAttribute("listPlayers", playerService.getAllPlayers());
        //return "index";
        // ToDo add link on players_page
        //return findPaginated(1, "nickName", "asc", model);
        return "index";
    }*/

    @GetMapping("/showNewPlayerForm")
    public String showNewPlayerForm(Model model) {
        Player player = new Player();
        model.addAttribute("player", player);
        return "catalogues/players/new_player";
    }

    @PostMapping("/saveAllPlayers")
    public String saveAllPlayer() {
        List<Player> allPlayers = upload.getPlayersFromFile();
        playerService.saveAllPlayers(allPlayers);
        return "redirect:/";
    }

    @PostMapping("/generatedShadowPlayers")
    public String generatedShadowPlayers() {
        List<Player> allRealPlayers = playerService.getAllPlayers(2);
        for (Player player: allRealPlayers) {
            int shadowCount = 3 + RandomUtils.getRandom(2);
            for (int i=1; i<= shadowCount; i++) {
                Player shadow = new Player();
                shadow.setNickName(player.getNickName() + "-Тень" + i);
                shadow.setFirstName(player.getFirstName() + "-Тень" + i);
                shadow.setLastName(player.getLastName() + "-Тень" + i);
                shadow.setSecretName(RandomUtils.getReadableString(8, 12, true));
                shadow.setGender(player.getGender());
                shadow.setPlayerType(20);
                playerService.savePlayer(shadow);
            }
        }
        return "redirect:/";
    }

    @GetMapping("/selectMistressShadow")
    public String selectMistressShadow(Model model) {
        List<Player> allRealPlayers = playerService.getAllPlayers(20);
        Player player = allRealPlayers.get(RandomUtils.getRandom(allRealPlayers.size()) - 1);
        model.addAttribute("player", player);
        return "games/first/mistress_info";
    }

    @GetMapping("/selectFirstMistress")
    public String selectFirstMistress() {
        List<Player> allPlayers = RandomUtils.getPlayersAndGroups(playerService.getAllPlayers(2), 30);
        allPlayers.forEach(p -> System.out.println(p.getNickName()));
        return "redirect:/";
    }


    @GetMapping("/listOfPlayers")
    public String showListOfThePlayers(Model model) {
        return findPaginated(1, "nickName", "asc", model);
    }

    @PostMapping("/savePlayer")
    public String savePlayer(@ModelAttribute("player") Player player ) {
        player.setSecretName(RandomUtils.getReadableString(8, 12, true));
        playerService.savePlayer(player);
        return "redirect:/";
    }

    @GetMapping("/showPlayerCard/idPlayer/{idPlayer}")
    public String showPlayerCard(@PathVariable (value = "idPlayer") long idPlayer, Model model) {
        Player player = playerService.getPlayerById(idPlayer);
        List<PlayerResourceStatistics> playerResourceStatistics = playersResourcesService.getPRGroupedByPlayerAndResource(idPlayer);
        model.addAttribute("groupedPlayerResource", playerResourceStatistics);
        model.addAttribute("nickName", player.getNickName());
        return "games/player_card";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
        // get player from the service
        Player player = playerService.getPlayerById(id);
        // set employee as a model attribute to pre-populate the form
        model.addAttribute("player", player);
        return "catalogues/players/update_player";
    }

    @GetMapping("/deletePlayer/{id}")
    public String deletePlayer(@PathVariable(value = "id") long id) {
        // call delete player method
        this.playerService.deletePlayerById(id);
        return "redirect:/";
    }

    // I can use: /page/{pageNo}/{pageSize} instead of pageSize = 5
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 25;
        Page<Player> page = playerService.findPaginated(pageNo, pageSize, sortField, sortdir);
        List<Player> listPlayers = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortdir);
        model.addAttribute("reverseSortDir", sortdir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listPlayers", listPlayers);
        //return "index";
        return "catalogues/players/list_players";
    }

}
