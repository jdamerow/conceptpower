package edu.asu.conceptpower.app.core.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.app.core.IAlternativeIdService;
import edu.asu.conceptpower.app.core.IConceptTypesService;
import edu.asu.conceptpower.app.core.impl.ConceptTypesService.ConceptTypes;
import edu.asu.conceptpower.core.ConceptEntry;

/**
 * This helper class is used for adding alternative ids to the concept entry.
 * 
 * @author karthikeyanmohan
 *
 */
@Service
public class AlternativeIdService implements IAlternativeIdService {

    @Autowired
    private IConceptTypesService conceptTypesService;

    /**
     * This method adds all alternative ids to the given concept entry including
     * the id that was used to search for a concept (passed as method
     * parameter).
     * 
     * If id or concept entry passed to the method is null, then method just
     * returns and no changes are made.
     * 
     * The queriedId parameter will be added to the alternative id list if the
     * queriedId is of Generic wordnet concept type.
     * 
     * @param queriedId
     * @param entry
     */
    public void addAlternativeIds(String queriedId, ConceptEntry entry) {

        if (entry == null) {
            return;
        }

        if (conceptTypesService.getConceptTypeByConceptId(queriedId) == ConceptTypes.GENERIC_WORDNET_CONCEPT) {
            entry.getAlternativeIds().add(queriedId);
        }
        // Specific Wordnet id is added irrespective of what is queried for
        if (entry.getWordnetId() != null) {
            String[] wordNetIds = entry.getWordnetId().split(",");
            for (String wordNetId : wordNetIds) {
                entry.getAlternativeIds().add(wordNetId.trim());
            }
        }
        // This has been added to make sure local concept id is added.
        entry.getAlternativeIds().add(entry.getId());

        // Added the merged ids of the concepts to alternative id
        if (entry.getMergedIds() != null) {
            String[] mergedIds = entry.getMergedIds().split(",");
            for (String mergedId : mergedIds) {
                entry.getAlternativeIds().add(mergedId.trim());
            }
        }
    }

    /**
     * This method adds the alternative ids to each of the concept entry in the
     * Collection<ConceptEntry> which is passed as a parameter.
     * 
     * @param conceptEntries
     */
    public void addAlternativeIds(Collection<ConceptEntry> conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }

    /**
     * This method adds the alternative ids to each of the concept entry in the
     * ConceptEntry[] which is passed as a parameter.
     * 
     * @param conceptEntries
     */
    public void addAlternativeIds(ConceptEntry[] conceptEntries) {
        for (ConceptEntry entry : conceptEntries) {
            addAlternativeIds(entry.getId(), entry);
        }
    }
}
