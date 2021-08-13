package shr.training_camp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shr.training_camp.core.model.database.Debts;
import shr.training_camp.sevice.interfaces.IDebtService;

import java.time.LocalDate;

@Controller
public class DebtController extends AbstractEntityController {

    @Autowired
    private IDebtService<Debts> debtService;

    @GetMapping("/listOfDebts")
    public String showListOfTheQuests(Model model) {
        return findPaginated(1, "startDate", "asc", model);
    }

    @GetMapping("/showNewDebtForm")
    public String showNewActivityForm(Model model) {
        Debts debt = new Debts();
        model.addAttribute("debt", debt);
        return "catalogues/quests/new_debt";
    }

    @PostMapping("/saveDebt")
    public String savePlayer(@ModelAttribute("debt") Debts debt ) {
        debtService.save(debt);
        return "redirect:/";
    }


    @GetMapping("/debts/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortdir,
                                Model model) {
        int pageSize = 25;
        Page<Debts> page = debtService.findPaginated(pageNo, pageSize, sortField, sortdir);
        setModelParametersForPagination(page, pageNo, sortField, sortdir, model);
        model.addAttribute("listDebts", page.getContent());
        return "catalogues/quests/list_debts";
    }

}
