package com.treefrogapps.googlecontactsyncapp.contacts_activity.model.data_model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.data_model.ContactDataModel.ContactEntry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.data_model.ContactDataModel.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class ContactsModelTests {

    private static final String TEST_AUTHOR = "Sync Test";
    private static final String TEST_USER_EMAIL = "synctest1978@gmail.com";
    private static final String TEST_USER_CONTACT_ENTRY_TITLE = "Brian Matthew Youfeed Fivespan Photobug";
    private static final String TEST_USER_CONTACT_ENTRY_PHONE_NUMBER = "2760254846";
    private static final int TEST_USER_CONTACT_COUNT = 5;

    private String jsonResponse;

    @Before public void setUp() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("contacts_test_json.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }

        reader.close();
        jsonResponse = builder.toString();
    }

    @Test public void givenJSONContactDataModelResponse_testObjectIsNotNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ContactDataModel contactDataModel = mapper.readValue(jsonResponse, ContactDataModel.class);

        assertNotNull(contactDataModel);
    }

    @Test public void givenJSONContactDataModelResponse_testObjectFieldsDeserialised() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ContactDataModel contactDataModel = mapper.readValue(jsonResponse, ContactDataModel.class);
        Author author = contactDataModel.getFeed().getAuthorList().get(0);
        ContactEntry contactEntry = contactDataModel.getFeed().getContactEntryList().get(0);

        assertEquals(TEST_AUTHOR, author.getName().getName());
        assertEquals(TEST_USER_EMAIL, author.getEmail().getEmail());
        assertEquals(TEST_USER_CONTACT_COUNT, contactDataModel.getFeed().getContactEntryList().size());
        assertEquals(TEST_USER_CONTACT_ENTRY_TITLE, contactEntry.getContactName().getFullname().getFullName());
        assertEquals(TEST_USER_CONTACT_ENTRY_PHONE_NUMBER, contactEntry.getPhoneNumberList().get(0).getPhoneNumber());
    }

    @Test public void givenJSONContactDataModelResponse_testThatOneUserHasBeenDeleted() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ContactDataModel contactDataModel = mapper.readValue(jsonResponse, ContactDataModel.class);

        int expectedDeleted = 1;
        int actualDeleted = 0;

        for(ContactEntry entry : contactDataModel.getFeed().getContactEntryList()){
            if(entry.getDeleteStatusList().size() > 0){
                if(entry.getDeleteStatusList().get(0).getDeleteStatus()){
                     actualDeleted++;
                }
            }
        }

        assertEquals(expectedDeleted, actualDeleted);
    }
}
