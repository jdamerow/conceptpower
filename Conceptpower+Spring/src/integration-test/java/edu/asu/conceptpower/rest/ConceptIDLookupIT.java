package edu.asu.conceptpower.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.asu.conceptpower.IntegrationTest;

public class ConceptIDLookupIT extends IntegrationTest {

    @Test
    public void test_getConceptById_successForWordNetIdInJson() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/wordNetConcept.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-01-polo_pony")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test
    public void test_getConceptById_successForGenericWordNetIdInJson() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptForGenericWordnet.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-??-polo_pony")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getConceptById_invalidWordNetIdInJson() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/Concept").param("id", null).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_getConceptById_successForLocalConceptIdInJson() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptForLocalId.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONdf62c00c-f4a9-4564-9dd6-c9b955650f3a")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_getConceptById_successForConceptWrapperInJson() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptWrapper.json"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONe7fbf694-5609-4691-bca8-916526c2ba6a")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    public void test_getConceptById_successForWordNetIdInXml() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/wordNetConcept.xml"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "WID-02380464-N-01-polo_pony")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getConceptById_invalidWordNetIdInXml() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/Concept").param("id", null).accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_getConceptById_successForLocalConceptIdInXml() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptForLocalId.xml"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONdf62c00c-f4a9-4564-9dd6-c9b955650f3a")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

    @Test
    public void test_getConceptById_successForConceptWrapperInXml() throws Exception {
        final String output = IOUtil
                .toString(this.getClass().getClassLoader().getResourceAsStream("output/conceptWrapper.xml"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/Concept").param("id", "CONe7fbf694-5609-4691-bca8-916526c2ba6a")
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(content().string(output)).andExpect(status().isOk());
    }

}
