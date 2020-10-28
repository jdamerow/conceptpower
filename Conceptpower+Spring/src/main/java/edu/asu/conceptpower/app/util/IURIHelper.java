package edu.asu.conceptpower.app.util;

import edu.asu.conceptpower.app.model.ConceptEntry;
import edu.asu.conceptpower.app.model.ConceptType;

public interface IURIHelper {
    public String getURI(ConceptEntry entry);
    public String getTypeURI(ConceptType type);
    public String getTypeId(String typeUriOrId);
    
}
