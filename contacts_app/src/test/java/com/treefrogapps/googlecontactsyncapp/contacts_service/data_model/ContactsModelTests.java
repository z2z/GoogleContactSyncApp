package com.treefrogapps.googlecontactsyncapp.contacts_service.data_model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ContactsModelTests {

    private static final String TEST_USER_TITLE = "Mark Keen's Contacts";
    private static final String TEST_AUTHOR = "Mark Keen";
    private static final String TEST_USER_EMAIL = "testemail@gmail.com";
    private static final String TEST_USER_CONTACT_ENTRY_TITLE = "Graeme Smith";
    private static final String TEST_USER_CONTACT_ENTRY_FULL_NAME = "Graeme Smith";
    private static final String TEST_USER_CONTACT_ENTRY_PHONE_NUMBER = "+443344454682";

    private String xmlResponse;

    @Before public void setUp() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("contacts_test_xml.xml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line;
        StringBuilder builder = new StringBuilder();
        while((line = reader.readLine()) != null){
            builder.append(line).append("\n");
        }

        reader.close();
        xmlResponse = builder.toString();
    }

    @Test public void givenXMLContactDataModelResponse_testObjectIsNotNull() throws Exception {

        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ContactDataModel contactDataModel = mapper.readValue(xmlResponse, ContactDataModel.class);

        assertNotNull(contactDataModel);
    }

    @Test public void givenXMLContactDataModelResponse_testObjectFieldsDeserialised() throws Exception {

        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ContactDataModel contactDataModel = mapper.readValue(xmlResponse, ContactDataModel.class);

        System.out.println(contactDataModel.toString());

        assertNotNull(contactDataModel);

        assertEquals(TEST_USER_TITLE, contactDataModel.getTitle());
        assertEquals(TEST_AUTHOR, contactDataModel.getAuthor().getName());
        assertEquals(TEST_USER_EMAIL, contactDataModel.getAuthor().getEmail());
        assertEquals(TEST_USER_CONTACT_ENTRY_TITLE, contactDataModel.getContactEntryList().get(0).getTitle());
        assertEquals(TEST_USER_CONTACT_ENTRY_FULL_NAME, contactDataModel.getContactEntryList().get(0).getNameDetails().getFullName());
        assertEquals(TEST_USER_CONTACT_ENTRY_PHONE_NUMBER, contactDataModel.getContactEntryList().get(0).getPhoneNumber().getNumber());

        assertEquals(8, contactDataModel.getContactEntryList().size());
    }
}
