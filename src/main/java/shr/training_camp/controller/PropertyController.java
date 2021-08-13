package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Player;
import shr.training_camp.core.model.database.Property;
import shr.training_camp.sevice.interfaces.PropertyService;

import java.util.List;

@Controller
public class PropertyController {

    @Autowired
    private PropertyService<Property> propertyService;

    @GetMapping("/listOfProperties")
    public String showListOfTheProperties(Model model) {
        return findPaginated(1, "name", "asc", model);
    }

    @PostMapping("/saveProperty")
    public String savePlayer(@ModelAttribute("property") Property property ) {
        propertyService.saveProperty(property);
        return "redirect:/";
    }

    @GetMapping("/showNewPropertyForm")
    public String showNewPropertyForm(Model model) {
        Property property = new Property();
        model.addAttribute("property", property);
        return "catalogues/properties/new_property";
    }


    @GetMapping("/properties/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 5;
        Page<Property> page = propertyService.findPaginated(pageNo, pageSize, sortField, sortdir);
        List<Property> listProperties = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortdir);
        model.addAttribute("reverseSortDir", sortdir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProperties", listProperties);

        return "catalogues/properties/list_properties";
    }

}
