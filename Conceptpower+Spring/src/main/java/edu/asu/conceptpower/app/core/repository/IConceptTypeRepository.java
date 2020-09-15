package edu.asu.conceptpower.app.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.conceptpower.app.core.model.impl.ConceptType;

/**
 * Concept Type repository
 *  
 * @author Keerthivasan Krishnamurthy
 * 
 */
@Repository
public interface IConceptTypeRepository extends PagingAndSortingRepository<ConceptType, String>{
    
}