package com.treefrogapps.googlecontactsyncapp.contacts_service.data_model;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

public class PeopleModelTests {

    private String jsonResponse;
    private static final int TEST_JSON_CONNECTION_COUNT = 8;
    private static final String TEST_CONTACT_GIVEN_NAME = "Andi";
    private static final String TEST_CONTACT_FAMILY_NAME = "Cars";
    private static final String TEST_CONTACT_DISPLAY_NAME = "Andi Cars";
    private static final String TEST_CONTACT_DISPLAY_NAME_LAST_FIRST = "Cars, Andi";
    private static final String TEST_CONTACT_PHONE_NUMBER = "+442392350300";
    private static final String TEST_CONTACT_FORMATTED_TYPE = "Work";
    private static final String TEST_CONTACT_VALUE = "023-923-50300";
    private static final String TEST_USER_PHOTO_URL = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M4dAWW0xfd8u2tUiYOSLUMAIhkKAUEQ____________ARieotH_______8B/photo.jpg";

    @Before public void setUp() throws Exception {

        InputStream in = getClass().getClassLoader().getResourceAsStream("people_test_json.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line).append('\n');
        }

        bufferedReader.close();
        jsonResponse = builder.toString();
    }

    @Test public void givenJsonResponse_TestThatDeserialisedObjectIsNotNull() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        PeopleDataModel peopleDataModel = mapper.readValue(jsonResponse, PeopleDataModel.class);

        assertNotNull(peopleDataModel);
    }

    @Test public void givenJSONPeopleDataModelResponse_testObjectFieldsDeserialised() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        PeopleDataModel peopleDataModel = mapper.readValue(jsonResponse, PeopleDataModel.class);
        PeopleDataModel.Connection connection = peopleDataModel.getConnectionList().get(0);

        assertSame(TEST_JSON_CONNECTION_COUNT, peopleDataModel.getConnectionList().size());

        assertEquals(TEST_CONTACT_GIVEN_NAME, connection.getNamesList().get(0).getGivenName());
        assertEquals(TEST_CONTACT_FAMILY_NAME, connection.getNamesList().get(0).getFamilyName());
        assertEquals(TEST_CONTACT_DISPLAY_NAME, connection.getNamesList().get(0).getDisplayName());
        assertEquals(TEST_CONTACT_DISPLAY_NAME_LAST_FIRST, connection.getNamesList().get(0).getDisplayNameLastFirst());
        assertEquals(TEST_CONTACT_PHONE_NUMBER, connection.getPhoneNumbers().get(0).getPhoneNumber());
        assertEquals(TEST_CONTACT_FORMATTED_TYPE, connection.getPhoneNumbers().get(0).getFormattedType());
        assertEquals(TEST_CONTACT_VALUE, connection.getPhoneNumbers().get(0).getValue());
        assertEquals(TEST_USER_PHOTO_URL, connection.getPhotos().get(0).getImageUrl());
    }
}
