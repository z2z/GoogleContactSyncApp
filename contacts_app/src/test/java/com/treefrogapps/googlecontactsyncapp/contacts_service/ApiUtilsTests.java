package com.treefrogapps.googlecontactsyncapp.contacts_service;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.*;
import static junit.framework.Assert.*;

public class ApiUtilsTests {

    private String jsonResponse;

    @Before public void setUp() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jsonResponse.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null){
            builder.append(line).append('\n');
        }

        reader.close();
        jsonResponse = builder.toString();
    }

    @Test public void givenAccessPermissionsGranted_ExtractAccessTokenCorrectly() throws Exception {

        String token = "eterttetetetetteyyya29.GlsbBGDRHI0VP5EYg8BJ5t03QHGRPMQ1XUhawryrtyrty1ZZySt-xyP1rtyrtyuscJa0LFggrtyyrtyEmABbF0H_LCGx7Wod23fLsPaKPpiMwRDc2j7c3snbhxW4TzTnBTZsqMyt6YHCjEykQ";

        String extractedToken = getAccessToken(jsonResponse);

        assertNotNull(extractedToken);
        assertEquals(token, extractedToken);
    }

    @Test public void givenAccessPermissionGranted_ExtractRefreshTokenCorrectly() throws Exception {

        String refreshToken = "1/3sUhqn6Z0-5Om3AhfhbvycnkSbDgVHbqrsWGZERsYheT44fggfeddUYwgq9hg564LVOKt7k0NEjHz3Sx";

        String extractedRefreshToken =  getRefreshToken(jsonResponse);

        assertNotNull(extractedRefreshToken);
        assertEquals(refreshToken, extractedRefreshToken);
    }

    @Test public void givenAccessPermissionGranted_ExtractAccessTokenExpiryTimeoutCorrectly() throws Exception{

        int timeoutInSeconds = 3600;

        int extractedTimeout = getAccessTokenTimeout(jsonResponse);

        assertEquals(timeoutInSeconds, extractedTimeout);
    }

    @Test public void givenAccessTokenTimeoutHasExpired_ConfirmMethodCheckReturnsFalse() throws Exception {

        long expiredTimeout = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + 120;

        assertFalse(isTokenValid(expiredTimeout, () -> TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
    }
}
