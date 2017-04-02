package com.treefrogapps.googlecontactsyncapp.contacts_activity.model.data_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDataModel {

    @JsonProperty("feed") private Feed feed;

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feed {

        @JsonProperty("author") private List<Author> author;
        @JsonProperty("entry") private List<ContactEntry> contactEntryList = new ArrayList<>();
        @JsonProperty("openSearch$totalResults") private SearchTotal searchTotal;

        public List<Author> getAuthorList() {
            return author;
        }

        public void setAuthorList(List<Author> author) {
            this.author = author;
        }

        public List<ContactEntry> getContactEntryList() {
            return contactEntryList;
        }

        public void setContactEntryList(List<ContactEntry> contactEntryList) {
            this.contactEntryList = contactEntryList;
        }

        public SearchTotal getSearchTotal() {
            return searchTotal;
        }

        public void setSearchTotal(SearchTotal searchTotal) {
            this.searchTotal = searchTotal;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {

        @JsonProperty("name") private Name name;
        @JsonProperty("email") private Email email;

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Email getEmail() {
            return email;
        }

        public void setEmail(Email email) {
            this.email = email;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Name {

        @JsonProperty("$t") private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Email {

        @JsonProperty("$t") String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactEntry {

        @JsonProperty("title") private Title title;
        @JsonProperty("gd$phoneNumber") private List<PhoneNumber> phoneNumberList = new ArrayList<>();
        @JsonProperty("gd$email") private List<ContactEmail> emailList = new ArrayList<>();
        @JsonProperty("id") private ContactId contactId;

        public Title getTitle() {
            return title;
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public List<PhoneNumber> getPhoneNumberList() {
            return phoneNumberList;
        }

        public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
            this.phoneNumberList = phoneNumberList;
        }

        public List<ContactEmail> getEmailList() {
            return emailList;
        }

        public void setEmailList(List<ContactEmail> emailList) {
            this.emailList = emailList;
        }

        public ContactId getContactId() {
            return contactId;
        }

        public void setContactId(ContactId contactId) {
            this.contactId = contactId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Title {

        @JsonProperty("$t") private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhoneNumber {

        @JsonProperty("$t") private String phoneNumber;
        @JsonProperty("rel") private String numberType;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getNumberType() {
            return numberType;
        }

        public void setNumberType(String numberType) {
            this.numberType = numberType;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactEmail {

        @JsonProperty("primary") private String isPrimary;
        @JsonProperty("address") private String contactEmail;
        @JsonProperty("rel") private String emailType;

        public String getIsPrimary() {
            return isPrimary;
        }

        public void setIsPrimary(String isPrimary) {
            this.isPrimary = isPrimary;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getEmailType() {
            return emailType;
        }

        public void setEmailType(String emailType) {
            this.emailType = emailType;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactId {

        @JsonProperty("$t") private String contactId;

        public String getContactId() {
            return contactId;
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchTotal {

        @JsonProperty("$t") private String searchTotal;

        public String getSearchTotal() {
            return searchTotal;
        }

        public void setSearchTotal(String searchTotal) {
            this.searchTotal = searchTotal;
        }
    }
}