package edu.asu.conceptpower.servlet.core;

import java.util.Map;

import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface ISearchManager {

    public ConceptEntry[] searchForConceptsConnected(Map<String, String> fieldMap,String operator) throws LuceneException,IllegalAccessException;
    
}