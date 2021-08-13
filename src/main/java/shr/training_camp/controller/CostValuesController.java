package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shr.training_camp.core.model.database.CostValues;
import shr.training_camp.sevice.interfaces.ICostValuesService;

@Controller
public class CostValuesController extends AbstractEntityController {

    @Autowired
    private ICostValuesService<CostValues> costValuesService;

    @PostMapping("/saveCostValue")
    public String saveCostValue(@ModelAttribute("costValue") CostValues costValues) {
        costValues.setIdCost(costValues.getCost().getIdCost());
        costValues.setIdResource(costValues.getResource().getIdResource());
        costValuesService.saveCostValues(costValues);
        return "catalogues/costs/list_costs";
    }


}
