package shr.training_camp.controller;


import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public abstract class AbstractEntityController {

    <T> void setModelParametersForPagination(Page<T> page,
                                         @PathVariable(value = "pageNo") int pageNo,
                                         @RequestParam("sortField") String sortField,
                                         @RequestParam("sortDir") String sortdir,
                                         Model model) {
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortdir);
        model.addAttribute("reverseSortDir", sortdir.equals("asc") ? "desc" : "asc");
    }

}
