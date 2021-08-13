package shr.training_camp.controller;

import org.apache.commons.math3.analysis.function.Cos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Activity;
import shr.training_camp.core.model.database.CostValues;
import shr.training_camp.core.model.database.Costs;
import shr.training_camp.repository.ResourceRepository;
import shr.training_camp.sevice.interfaces.ICostService;

@Controller
public class CostController extends AbstractEntityController {

    @Autowired
    private ICostService<Costs> costService;

    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping("/listOfCosts")
    public String getListOfCosts(Model model) {
        return findPaginated(1, "name", "asc", model);
    }

    @PostMapping("/saveCost")
    public String saveCost(@ModelAttribute("cost")Costs costs) {
        costService.saveCost(costs);
        return "catalogues/costs/list_costs";
    }

    @GetMapping("/showNewCostForm")
    public String showNewPlayerForm(Model model) {
        Costs cost = new Costs();
        model.addAttribute("cost", cost);
        return "catalogues/costs/new_cost";
    }

    @GetMapping("/costs/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 25;
        Page<Costs> page = costService.findPaginated(pageNo, pageSize, sortField, sortdir);
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listCosts", page.getContent());
        return "catalogues/costs/list_costs";
    }

    @GetMapping("/showFormAddCostValue/{id}")
    public String showFormForSetCostValues(@PathVariable (value = "id") long id, Model model) {
        Costs cost = costService.getCostById(id);
        model.addAttribute("costs", cost);
        model.addAttribute("resources", resourceRepository.findAll());
        model.addAttribute("costValue", new CostValues());
        model.addAttribute("costSelect", id);
        return "catalogues/costs/new_cost_value";
    }


}
