package com.treefrogapps.googlecontactsyncapp.contacts_service.data_model;


import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.ContactDataModel.ContactEntry;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.ContactDataModel.NameDetails;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.ContactDataModel.PhoneNumber;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.PeopleDataModel.Connection;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.PeopleDataModel.Names;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.PeopleDataModel.PhoneNumbers;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.PeopleDataModel.Photos;

public final class ModelConverter {

    public static List<Contact> convertPeopleDataModel(PeopleDataModel dataModel) {
        List<Contact> contactList = new ArrayList<>();

        for (Connection connection : dataModel.getConnectionList()) {
            String firstName = "";
            String lastName = "";
            String phoneNumber = "";
            String imageUrl = null;

            List<Names> namesList = connection.getNamesList();
            if (namesList.size() > 0) {
                firstName = namesList.get(0).getGivenName();
                lastName = namesList.get(0).getFamilyName();
            }

            List<PhoneNumbers> numbersList = connection.getPhoneNumbers();
            if (numbersList.size() > 0) {
                phoneNumber = numbersList.get(0).getPhoneNumber();
            }
            List<Photos> photosList = connection.getPhotos();
            if (photosList.size() > 0) {
                imageUrl = photosList.get(0).getImageUrl();
            }
            contactList.add(new Contact(firstName, lastName, phoneNumber, imageUrl));
        }
        return contactList;
    }

    public static List<Contact> convertContactDataModel(ContactDataModel dataModel) {
        List<Contact> contactList = new ArrayList<>();

        for (ContactEntry entry : dataModel.getContactEntryList()) {
            String firstName = "";
            String lastName = "";
            String phoneNumber = "";

            NameDetails details = entry.getNameDetails();
            if (details != null) {
                firstName = details.getGivenName();
                lastName = details.getFamilyName();
            }

            PhoneNumber number = entry.getPhoneNumber();
            if (number != null) {
                phoneNumber = number.getNumber();
            }
            contactList.add(new Contact(firstName, lastName, phoneNumber, null));
        }
        return contactList;
    }
}
