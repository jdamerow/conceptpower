package edu.asu.conceptpower.web;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConceptDetailsController {

    @RequestMapping(value="/getconceptdetails", method=RequestMethod.POST)
    public String getConceptDetails(@RequestBody Map<String, String> details, Model model) {
        System.out.println("Enteirng"+details);
        model.addAttribute("details", details);
        return "layouts/modals/conceptdetails";
    }

}
