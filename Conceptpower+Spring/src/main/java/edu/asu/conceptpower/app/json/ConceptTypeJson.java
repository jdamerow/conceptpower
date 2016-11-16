package edu.asu.conceptpower.app.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConceptTypeJson {

    @JsonProperty("type_id")
    private String typeId;

    @JsonProperty("type_uri")
    private String typeUri;

    @JsonProperty("type_name")
    private String typeName;

    private String description;

    @JsonProperty("creator_id")
    private String creatorId;

    private String matches;

    private ConceptTypeJson superType;

    @JsonProperty("modified_by")
    private String modifiedBy;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeUri() {
        return typeUri;
    }

    public void setTypeUri(String typeUri) {
        this.typeUri = typeUri;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ConceptTypeJson getSuperType() {
        return superType;
    }

    public void setSuperType(ConceptTypeJson superType) {
        this.superType = superType;
    }
}
