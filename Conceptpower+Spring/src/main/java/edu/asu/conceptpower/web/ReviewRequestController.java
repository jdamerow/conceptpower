package edu.asu.conceptpower.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.conceptpower.app.core.impl.RequestsManager;
import edu.asu.conceptpower.core.ReviewRequest;

@Controller
public class ReviewRequestController {
 
    @Autowired
    private RequestsManager requestsMgr;
           
    @RequestMapping(value = "/auth/request/add", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest addNewReviewRequest( @ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setRequester(principal.getName());
        
        requestsMgr.addReviewRequest(reviewRequest);
        
        return reviewRequest;
    }
    
    @RequestMapping(value = "/auth/request/resolve", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest resolveRequest(@ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        reviewRequest.setResolver(principal.getName());
        
        ReviewRequest updatedRequest = requestsMgr.updateReview(reviewRequest);
        
        return updatedRequest;
    }
    
    @RequestMapping(value = "/auth/request/reopen", method = RequestMethod.POST )
    public @ResponseBody ReviewRequest reopenRequest(@ModelAttribute(value="reviewRequest") ReviewRequest reviewRequest,Principal principal) {
        ReviewRequest updatedRequest = requestsMgr.reopenReview(reviewRequest);
        
        return updatedRequest;
    }
}
