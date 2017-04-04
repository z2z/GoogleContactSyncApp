package com.treefrogapps.googlecontactsyncapp.contacts_activity.model.data_model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeopleDataModel {

    @JsonProperty("connections")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Connection> connectionList = new ArrayList<>();

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Connection {

        @JsonProperty("resourceName") private String resourceName;
        @JsonProperty("phoneNumbers") private List<PhoneNumbers> phoneNumbers = Collections.emptyList();
        @JsonProperty("etag") private String eTag;
        @JsonProperty("names") private List<Names> namesList = Collections.emptyList();
        @JsonProperty("photos") private List<Photos> photos = Collections.emptyList();

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public List<PhoneNumbers> getPhoneNumbers() {
            return phoneNumbers;
        }

        public void setPhoneNumbers(List<PhoneNumbers> phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhoneNumbers {

        @JsonProperty("canonicalForm") private String phoneNumber;
        @JsonProperty("formattedType") private String formattedType;
        @JsonProperty("value") private String value;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFormattedType() {
            return formattedType;
        }

        public void setFormattedType(String formattedType) {
            this.formattedType = formattedType;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Names {

        @JsonProperty("givenName") private String givenName;
        @JsonProperty("familyName") private String familyName;
        @JsonProperty("displayName") private String displayName;
        @JsonProperty("displayNameLastFirst") private String displayNameLastFirst;

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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photos {

        @JsonProperty("url") private String imageUrl;
        @JsonProperty("metadata") private MetaData metaData;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public MetaData getMetaData() {
            return metaData;
        }

        public void setMetaData(MetaData metaData) {
            this.metaData = metaData;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetaData {

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {

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

    @Override public String toString() {
        String connections = "";
        for (Connection connection : connectionList) {
            connections += connection.toString();
        }
        return connections;
    }
}