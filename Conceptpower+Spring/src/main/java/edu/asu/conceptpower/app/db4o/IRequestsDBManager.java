package edu.asu.conceptpower.app.db4o;

import java.util.List;

import edu.asu.conceptpower.core.ReviewRequest;

public interface IRequestsDBManager {

    
    public abstract void store(ReviewRequest reviewRequest);
    
    public List<ReviewRequest> getAllReviews(ReviewRequest conceptId);
    
    public ReviewRequest getReview(ReviewRequest request);
}
