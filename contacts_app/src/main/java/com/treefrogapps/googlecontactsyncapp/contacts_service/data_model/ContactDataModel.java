package com.treefrogapps.googlecontactsyncapp.contacts_service.data_model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.Collections;
import java.util.List;

@JacksonXmlRootElement(localName = "feed")
public class ContactDataModel {

    public ContactDataModel() {
    }

    @JacksonXmlProperty(localName = "id") private String id;

    @JacksonXmlProperty(localName = "updated") private String updated;

    @JacksonXmlProperty(localName = "title") private String title;

    @JacksonXmlProperty(localName = "author") private Author author;

    @JacksonXmlProperty(localName = "entry")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ContactEntry> contactEntryList = Collections.emptyList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<ContactEntry> getContactEntryList() {
        return contactEntryList;
    }

    public void setContactEntryList(List<ContactEntry> contactEntryList) {
        this.contactEntryList = contactEntryList;
    }

    public static class Author {

        public Author() {
        }

        @JacksonXmlProperty(localName = "name") private String name;
        @JacksonXmlProperty(localName = "email") private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override public String toString() {
            return "Author{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    public static class ContactEntry {

        public ContactEntry() {
        }

        @JacksonXmlProperty(localName = "id") private String id;

        @JacksonXmlProperty(localName = "title") private String title;

        @JacksonXmlProperty(namespace = "gd", localName = "name")
        private NameDetails nameDetails;

        @JacksonXmlProperty(namespace = "gd", localName = "phoneNumber")
        private PhoneNumber phoneNumber;

        public String getId() {

            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public NameDetails getNameDetails() {
            return nameDetails;
        }

        public void setNameDetails(NameDetails nameDetails) {
            this.nameDetails = nameDetails;
        }

        public PhoneNumber getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(PhoneNumber phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override public String toString() {
            return "ContactEntry{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", nameDetails=" + nameDetails.toString() +
                    ", phoneNumber='" + phoneNumber.toString() + '\'' +
                    '}';
        }
    }

    public static class NameDetails {

        public NameDetails() {
        }

        @JacksonXmlProperty(namespace = "gd", localName = "fullName") private String fullName;
        @JacksonXmlProperty(namespace = "gd", localName = "givenName") private String givenName;
        @JacksonXmlProperty(namespace = "gd", localName = "familyName") private String familyName;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

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

        @Override public String toString() {
            return "NameDetails{" +
                    "fullName='" + fullName + '\'' +
                    ", givenName='" + givenName + '\'' +
                    ", familyName='" + familyName + '\'' +
                    '}';
        }
    }

    @Override public String toString() {
        String contacts = "\n";
        for (ContactEntry entry : contactEntryList) {
            contacts += entry.toString() + '\n';
        }
        return "ContactDataModel{" +
                "id='" + id + '\'' +
                ", updated='" + updated + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author.toString() +
                ", contactEntries=" + contacts +
                '}';
    }

    public static class PhoneNumber {

        @JacksonXmlText private String number;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override public String toString() {
            return "PhoneNumber{" +
                    "number='" + number + '\'' +
                    '}';
        }
    }
}