package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.Resources;
import shr.training_camp.sevice.interfaces.ResourceService;

import java.util.List;

@Controller
public class ResourceController  {

    @Autowired
    public ResourceService resourceService;

    @GetMapping("/listOfResources")
    public String showListOfThePlayers(Model model) {

        return findPaginated(1, "name", "asc", model);
    }

    @PostMapping("/saveResource")
    public String saveResource(@ModelAttribute("resource") Resources resource ) {
        resourceService.saveResource(resource);
        return "redirect:/";
    }

    @GetMapping("/showNewResourceForm")
    public String showNewPlayerForm(Model model) {
        Resources resource = new Resources();
        model.addAttribute("resource", resource);
        return "catalogues/resources/new_resource";
    }

    @GetMapping("/resources/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 50;
        Page<Resources> page = resourceService.findPaginated(pageNo, pageSize, sortField, sortdir);
        List<Resources> listResources = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortdir);
        model.addAttribute("reverseSortDir", sortdir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listResources", listResources);
        //return "index";
        return "catalogues/resources/list_resources";
    }

    @GetMapping("/showFormForUpdateResource/{id}")
    public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
        Resources resource = resourceService.getResourceById(id);
        // set employee as a model attribute to pre-populate the form
        model.addAttribute("resource", resource);
        return "catalogues/resources/update_resource";
    }

}
