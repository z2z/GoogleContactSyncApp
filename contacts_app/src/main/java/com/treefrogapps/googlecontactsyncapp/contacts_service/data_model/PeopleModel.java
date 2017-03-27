package com.treefrogapps.googlecontactsyncapp.contacts_service.data_model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

public class PeopleModel {

    List<Connection> connectionList;


    public List<Connection> getConnectionList() {
        return connectionList;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"resourceName", "etag", "names", "photos", "metadata"})

    private static class Connection {

        @JsonProperty("resourceName") private String resourceName;
        @JsonProperty("etag") private String eTag;
        @JsonProperty("names") private List<Names> namesList;
        @JsonProperty("photos") private List<Photos> photos;

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public String geteTag() {
            return eTag;
        }

        public void seteTag(String eTag) {
            this.eTag = eTag;
        }

        public List<Names> getNamesList() {
            return namesList;
        }

        public void setNamesList(List<Names> namesList) {
            this.namesList = namesList;
        }

        public List<Photos> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photos> photos) {
            this.photos = photos;
        }
    }

    private static class Names {

        @JsonProperty("givenName") private String givenName;
        @JsonProperty("familyName") private String familyName;
        @JsonProperty("displayName") private String displayName;
        @JsonProperty("displayNameLastFirst") private String displayNameLastFirst;
        @JsonProperty("metadata") private MetaData metaData;

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayNameLastFirst() {
            return displayNameLastFirst;
        }

        public void setDisplayNameLastFirst(String displayNameLastFirst) {
            this.displayNameLastFirst = displayNameLastFirst;
        }

        public MetaData getMetaData() {
            return metaData;
        }

        public void setMetaData(MetaData metaData) {
            this.metaData = metaData;
        }
    }

    private static class MetaData {

        @JsonProperty("source") private Source source;
        @JsonProperty("primary") private boolean primary;

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public boolean isPrimary() {
            return primary;
        }

        public void setPrimary(boolean primary) {
            this.primary = primary;
        }
    }

    private static class Source {

        @JsonProperty("type") private String type;
        @JsonProperty("id") private String id;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private static class Photos {

        @JsonProperty("url") private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}