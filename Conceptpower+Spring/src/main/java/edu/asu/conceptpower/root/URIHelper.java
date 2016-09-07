package edu.asu.conceptpower.root;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.ConceptType;
import edu.asu.conceptpower.servlet.xml.XMLConstants;

/**
 * This class creates URIs for concepts and types based on the 
 * in xml-config.xml configured URI prefixes.
 */
@Service
public class URIHelper implements IURIHelper{

	@Autowired
	private XMLConfig xmlConfig;

	public String getURI(ConceptEntry entry) {

		String uriPrefix = xmlConfig.getUriPrefix();

		if (entry.getId() != null && !entry.getId().isEmpty())
			return uriPrefix + entry.getId();

		else
			return uriPrefix + entry.getWordnetId();
	}

	public String getTypeURI(ConceptType type) {

		return XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX
				+ type.getTypeId();
	}
	
	public String getTypeId(String typeUriOrId) {
		if (!typeUriOrId.startsWith(XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX)) {
			return typeUriOrId;
		}
		
		return typeUriOrId.substring((XMLConstants.TYPE_NAMESPACE + XMLConstants.TYPE_PREFIX).length());
	}

    public Map<String, String> getUrisBasedOnIds(Set<String> ids) {
        Map<String, String> uriPrefixes = new HashMap<>();
        String uriPrefix = xmlConfig.getUriPrefix();
        for (String id : ids) {
            uriPrefixes.put(id, uriPrefix + id);
        }
        return uriPrefixes;
    }
}
