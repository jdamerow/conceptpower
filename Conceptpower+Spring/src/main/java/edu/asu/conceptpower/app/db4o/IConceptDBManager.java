package edu.asu.conceptpower.app.db4o;

import java.util.List;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptList;

public interface IConceptDBManager {

    public static final int ASCENDING = 1;
    public static final int DESCENDING = -1;

	public abstract ConceptEntry getEntry(String id);

	public abstract ConceptEntry[] getEntriesByFieldContains(String field,
			String containsString);

	public abstract ConceptEntry[] getEntriesForWord(String word, String pos);

	public abstract ConceptEntry[] getSynonymsPointingToId(String id);

	public abstract ConceptEntry[] getEntriesForWord(String word);

	public abstract ConceptList getConceptList(String name);

    public abstract List<ConceptEntry> getAllEntriesFromList(String conceptList, int pageNo, int pageSize,
            String sortBy, int sortDirection);

	public abstract void store(ConceptEntry element, String databasename);

	public abstract void update(ConceptEntry entry, String databasename);

	public abstract void deleteConceptList(String name);

	public abstract void update(ConceptList list, String listname,
			String databasename);

    public List<ConceptEntry> getAllEntriesFromList(String listname);

    public List<ConceptEntry> getAllEntriesByTypeId(String typeId);

    public List<ConceptEntry> getWrapperEntryByWordnetId(String wordnetId);
    
    public List<ConceptEntry> getAllConcepts();
    
    public List<ConceptList> getAllConceptLists();
    
    public void storeConceptList(ConceptList element, String databasename);
    
    public boolean checkIfConceptListExists(String id);

}