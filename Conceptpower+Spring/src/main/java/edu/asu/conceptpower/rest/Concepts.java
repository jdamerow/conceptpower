package edu.asu.conceptpower.rest;

import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.tools.ant.taskdefs.compilers.Sj;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.core.POS;
import edu.asu.conceptpower.exceptions.DictionaryDoesNotExistException;
import edu.asu.conceptpower.exceptions.DictionaryModifyException;
import edu.asu.conceptpower.web.ConceptAddController;

@Controller
public class Concepts {
	
	@Autowired
	private IConceptManager conceptManager;
	
	private static final Logger logger = LoggerFactory
			.getLogger(Concepts.class);

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "rest/concept/add", method = RequestMethod.POST)
	public ResponseEntity<String> addConcept(@RequestBody String body, Principal principal) {
		
		StringReader reader = new StringReader(body);
		JSONParser jsonParser = new JSONParser();
		
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(reader);
		} catch (IOException | ParseException e1) {
			logger.error("Error parsing request.", e1);
			return new ResponseEntity<String>("Error parsing request: " + e1, HttpStatus.BAD_REQUEST);
		} catch (ClassCastException ex) {
			logger.error("Couldn't cast object.", ex);
			return new ResponseEntity<String>("It looks like you are not submitting a JSON Object.", HttpStatus.BAD_REQUEST);
		}
		
		JsonValidationResult result  = checkJsonObject(jsonObject);
		if (!result.isValid())
			return new ResponseEntity<String>(result.getMessage(), HttpStatus.BAD_REQUEST);
		
		
		ConceptEntry conceptEntry = createEntry(jsonObject, principal.getName());
		
		String id = null;
		try {
			id = conceptManager.addConceptListEntry(conceptEntry, principal.getName());
		} catch (DictionaryDoesNotExistException e) {
			logger.error("Error creating concept from REST call.", e);
			return new ResponseEntity<String>("Specified dictionary does not exist in Conceptpower.", HttpStatus.BAD_REQUEST);
		} catch (DictionaryModifyException e) {
			logger.error("Error creating concept from REST call.", e);
			return new ResponseEntity<String>("Specified dictionary can't be modified.", HttpStatus.BAD_REQUEST);
		}
		
		jsonObject.put("id", id);
		
		return new ResponseEntity<String>(jsonObject.toJSONString(), HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "rest/concepts/add", method = RequestMethod.POST)
	public ResponseEntity<String> addConcepts(@RequestBody String body, Principal principal) {
		StringReader reader = new StringReader(body);
		JSONParser jsonParser = new JSONParser();
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) jsonParser.parse(reader);
		} catch (IOException | ParseException e1) {
			logger.error("Error parsing request.", e1);
			return new ResponseEntity<String>("Error parsing request: " + e1, HttpStatus.BAD_REQUEST);
		}
		
		ListIterator<JSONObject> listIt = jsonArray.listIterator();
		JSONArray responseArray = new JSONArray();
		while (listIt.hasNext()) {
			JSONObject jsonObject = listIt.next();
			
			JsonValidationResult result = checkJsonObject(jsonObject);
			
			JSONObject responseObj = new JSONObject();
			responseObj.put("word", jsonObject.get("word"));
			responseObj.put("validation", result.getMessage() != null ? result.getMessage() : "OK");
			
			responseArray.add(responseObj);
			
			if (!result.isValid()) {
				responseObj.put("success", false);
				continue;
			}
			
			ConceptEntry conceptEntry = createEntry(jsonObject, principal.getName());
		
			String id = null;
			try {
				id = conceptManager.addConceptListEntry(conceptEntry, principal.getName());
				responseObj.put("id", id);
				responseObj.put("success", true);
			} catch (DictionaryDoesNotExistException e) {
				logger.error("Error creating concept from REST call.", e);
				responseObj.put("success", false);
				responseObj.put("error_message", "Specified dictionary does not exist in Conceptpower.");
			} catch (DictionaryModifyException e) {
				logger.error("Error creating concept from REST call.", e);
				responseObj.put("success", false);
				responseObj.put("error_message", "Specified dictionary can't be modified.");
			}
			
		}
		
		return new ResponseEntity<String>(responseArray.toJSONString(), HttpStatus.OK);
	}
	
	private JsonValidationResult checkJsonObject(JSONObject jsonObject) {
		if (jsonObject.get("pos") == null) {
			return new JsonValidationResult("Error parsing request: please provide a POS ('pos' attribute).", jsonObject, false);
		}
		
		if (jsonObject.get("word") == null) {
			return new JsonValidationResult("Error parsing request: please provide a word for the concept ('word' attribute).", jsonObject, false);
		}
		
		if (jsonObject.get("description") == null) {
			return new JsonValidationResult("Error parsing request: please provide a description for the concept ('description' attribute).", jsonObject, false);
		}
		
		if (jsonObject.get("types") == null) {
			return new JsonValidationResult("Error parsing request: please provide a type for the concept ('types' attribute).", jsonObject, false);
		}
		
		if (jsonObject.get("conceptlist") == null) {
			return new JsonValidationResult("Error parsing request: please provide a concept list for the concept ('conceptlist' attribute).", jsonObject, false);
		}
		
		String pos = jsonObject.get("pos").toString();
		if (!POS.posValues.contains(pos)) {
			logger.error("Error creating concept from REST call. " + pos + " does not exist.");
			return new JsonValidationResult("POS '" + pos + "' does not exist.", jsonObject, false);
		}
		
		return new JsonValidationResult(null, jsonObject, true);
	}
	
	private ConceptEntry createEntry(JSONObject jsonObject, String username) {
		ConceptEntry conceptEntry = new ConceptEntry();
		conceptEntry.setSynonymIds(jsonObject.get("synonymids") != null ? jsonObject.get("synonymids").toString() : "");
		conceptEntry.setWord(jsonObject.get("word").toString());
		conceptEntry.setConceptList(jsonObject.get("conceptlist").toString());
		conceptEntry.setPos(jsonObject.get("pos").toString());
		conceptEntry.setDescription(jsonObject.get("description").toString());
		conceptEntry.setEqualTo(jsonObject.get("equals") != null ? jsonObject.get("equals").toString() : "");
		conceptEntry.setSimilarTo(jsonObject.get("similar") != null ? jsonObject.get("similar").toString() : "");
		conceptEntry.setTypeId(jsonObject.get("types").toString());
		return conceptEntry;
	}
	
	class JsonValidationResult {
		private String message;
		private JSONObject jsonObject;
		private boolean valid;
		
		public JsonValidationResult(String message, JSONObject object, boolean valid) {
			this.message = message;
			this.valid = valid;
			this.jsonObject = object;
		}
		
		public JSONObject getJsonObject() {
			return jsonObject;
		}
		public void setJsonObject(JSONObject jsonObject) {
			this.jsonObject = jsonObject;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		
	}
}
