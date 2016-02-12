package edu.asu.conceptpower.servlet.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.servlet.exceptions.LuceneException;
import edu.asu.conceptpower.servlet.lucene.ILuceneIndexManger;

/**
 * This class provides methods for deleting and viewing lucene indexes
 * 
 * @author mkarthik90
 *
 */
@Controller
public class LuceneIndexController {

    @Autowired
    private ILuceneIndexManger manager;

    @RequestMapping(value = "auth/luceneIndex", method = RequestMethod.GET)
    public String showLuceneIndex(ModelMap model) {
        return "/auth/luceneIndex";
    }

    @RequestMapping(value = "auth/indexLuceneWordNet", method = RequestMethod.POST)
    public String indexConcepts(HttpServletRequest req, Principal principal, ModelMap model) throws LuceneException {

        manager.deleteIndexes();
        manager.indexConcepts();
        model.addAttribute("message", "Indexed Successfully");
        return "/auth/luceneIndex";
    }

    @RequestMapping(value = "auth/deleteConcepts", method = RequestMethod.POST)
    public String deleteConcepts(HttpServletRequest req, Principal principal, ModelMap model) throws LuceneException {

        manager.deleteIndexes();
        model.addAttribute("message", "Deleted Indexes Successfully");
        return "/auth/luceneIndex";
    }
}