package edu.asu.conceptpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.conceptpower.core.ConceptManager;
import edu.asu.conceptpower.exceptions.DictionaryExistsException;

@Controller
public class AddConceptListView {

	@Autowired
	private ConceptManager conceptManager;

	@Autowired
	private ConceptListManager conceptListManager;

	private String name;
	private String description;

	@RequestMapping(value = "auth/concepts/ListAddView")
	public String listAddView(HttpServletRequest req, ModelMap model) {
		return "/auth/concepts/ListAddView";
	}

	@RequestMapping(value = "auth/concepts/createconceptlist")
	public String createConceptList(HttpServletRequest req, ModelMap model) {
		name = req.getParameter("name");
		description = req.getParameter("description");

		try {
			conceptManager.addConceptList(name, description);
		} catch (DictionaryExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conceptListManager.showConceptList(req, model);

		return "/auth/concepts/ConceptList";
	}

}
