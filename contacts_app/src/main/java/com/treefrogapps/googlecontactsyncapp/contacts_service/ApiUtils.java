package com.treefrogapps.googlecontactsyncapp.contacts_service;


import org.json.JSONException;
import org.json.JSONObject;

final class ApiUtils {

    static String getAccessToken(String jsonResponse){
        try {
            JSONObject object = new JSONObject(jsonResponse);
            return object.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getRefreshToken(String jsonResponse){
        try {
            JSONObject object = new JSONObject(jsonResponse);
            return object.getString("refresh_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
