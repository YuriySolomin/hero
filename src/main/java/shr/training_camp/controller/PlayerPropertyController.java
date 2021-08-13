package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shr.training_camp.core.model.database.PlayerProperty;
import shr.training_camp.sevice.interfaces.IPlayerPropertyService;

@Controller
public class PlayerPropertyController extends AbstractEntityController {

    @Autowired
    private IPlayerPropertyService<PlayerProperty> playerPropertyService;

    @GetMapping("/listOfPlayersProperties")
    public String showListOfThePlayers(Model model) {
        return findPaginated(1, "idPlayer", "asc", model);
    }

    @GetMapping("/players_properies/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 50;
        Page<PlayerProperty> page = playerPropertyService.findPaginated(pageNo, pageSize, sortField, sortdir);
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listOfPlayersProperties", page.getContent());
        return "catalogues/players_properties/list_players_properties";
    }

}
