
package edu.asu.conceptpower.rest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.xml.sax.SAXException;

import edu.asu.conceptpower.app.core.ConceptTypesService;
import edu.asu.conceptpower.app.core.ConceptTypesService.ConceptTypes;
import edu.asu.conceptpower.app.core.IConceptManager;
import edu.asu.conceptpower.app.db.TypeDatabaseClient;
import edu.asu.conceptpower.app.util.URIHelper;
import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.rest.msg.IMessageConverter;
import edu.asu.conceptpower.rest.msg.IMessageRegistry;
import edu.asu.conceptpower.rest.msg.xml.XMLConceptMessage;
import junit.framework.Assert;

public class ConceptIDLookupTest {

    @Mock
    private IConceptManager dictManager;

    @Mock
    private TypeDatabaseClient typeManager;

    @Mock
    private IMessageRegistry messageFactory;

    @Mock
    private IConceptManager conceptManager;

    @Mock
    private ConceptTypesService conceptTypesService;

    @Mock
    private URIHelper uriCreator;

    @Mock
    private IMessageConverter xmlMessageFactory;

    @InjectMocks
    private ConceptIDLookup conceptIDLookup;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE)).thenReturn(xmlMessageFactory);
        Mockito.when(messageFactory.getMessageFactory(MediaType.APPLICATION_XML_VALUE).createConceptMessage())
                .thenReturn(new XMLConceptMessage(uriCreator));
    }

    @Test
    public void test_getConceptEntry_successForWordNetIds()
            throws IOException, SAXException, ParserConfigurationException {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptEntryWithWordnet.xml"));
        final String conceptId = "CONf375adff-dde7-4536-9e62-f80328f800d0";
        final String wordNetIds = "W-ID1, W-ID2";
        ConceptEntry entry = new ConceptEntry();
        entry.setId(conceptId);
        entry.setWordnetId(wordNetIds);
        Mockito.when(dictManager.getConceptEntry(conceptId)).thenReturn(entry);
        ResponseEntity<String> response = conceptIDLookup
                .getConceptById("http://www.digitalhps.org/concepts/" + conceptId, MediaType.APPLICATION_XML_VALUE);
        RestTestUtility.testValidXml(response.getBody());
        Assert.assertEquals(output, response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getConceptEntry_sucessWithConceptTypes() throws Exception {
        final String expectedResponse = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/alternativeGenericIds.xml"));
        final String wordNetIds = "W-ID01, W-ID02";
        final String conceptId = "CONf375adff-dde7-4536-9e62-f80328f800d0";
        ConceptEntry entry = new ConceptEntry();
        entry.setId("W-ID01");
        entry.setWordnetId(wordNetIds);
        Mockito.when(dictManager.getConceptEntry("W-ID??")).thenReturn(entry);
        Mockito.when(conceptTypesService.getConceptTypeByConceptId("W-ID??"))
                .thenReturn(ConceptTypes.GENERIC_WORDNET_CONCEPT);

        ConceptEntry newEntry = new ConceptEntry();

        newEntry.setId(conceptId);
        newEntry.setTypeId("T-123");
        newEntry.setDescription("Description");
        newEntry.setSynonymIds("SYN-01");
        newEntry.setEqualTo("equals");
        newEntry.setSimilarTo("similar");
        newEntry.setPos("NOUN");
        newEntry.setWord("pony");
        newEntry.setWordnetId(wordNetIds);

        Mockito.when(conceptManager.getConceptEntry("W-ID01")).thenReturn(newEntry);
        ResponseEntity<String> response = conceptIDLookup.getConceptById("W-ID??", MediaType.APPLICATION_XML_VALUE);
        RestTestUtility.testValidXml(response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(expectedResponse, response.getBody());
    }
}
