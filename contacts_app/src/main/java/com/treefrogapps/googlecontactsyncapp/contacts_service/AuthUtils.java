package com.treefrogapps.googlecontactsyncapp.contacts_service;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.Clock;

import org.json.JSONException;
import org.json.JSONObject;

import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.REFRESH_TOKEN_EXTRA;

public final class AuthUtils {

    public static final String OAUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth?";
    public static final String OAUTH_REVOKE_URL = "https://accounts.google.com/o/oauth2/revoke?token=";
    public static final String AUTH_TOKEN_END_POINT = "https://www.googleapis.com/oauth2/v4/token";
    public static final String CLIENT_API_ID = "1058427583280-5vh5r0c2qi8bbh1o26oudu5hlbm5lldi.apps.googleusercontent.com";
    public static final String SCOPE = "https://www.googleapis.com/auth/contacts https://www.google.com/m8/feeds"; // <-- People and Contacts Api access
    public static final String REDIRECT_URI = "com.treefrogapps.googlecontactsyncapp:/oauth_redirect";

    public static String generateAuthorisationUrl(String authUrl, String clientId, String scope, String redirectUri, @Nullable String state) {
        return new StringBuilder(authUrl)
                .append("scope=").append(scope)
                .append("&response_type=code")
                .append(state != null ? "&state=" : "").append(state != null ? state : "")
                .append("&redirect_uri=").append(redirectUri)
                .append("&client_id=").append(clientId).toString();
    }

    public static boolean checkRefreshToken(SharedPreferences preferences) {
        return preferences.getString(REFRESH_TOKEN_EXTRA, null) != null;
    }


    static String getAccessToken(String jsonResponse) {
        try {
            JSONObject object = new JSONObject(jsonResponse);
            return object.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int getAccessTokenTimeout(String jsonResponse) {
        try {
            JSONObject object = new JSONObject(jsonResponse);
            return object.getInt("expires_in");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static String getRefreshToken(String jsonResponse) {
        try {
            JSONObject object = new JSONObject(jsonResponse);
            return object.getString("refresh_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String createBody(String redirectCode) {
        return new StringBuilder()
                .append("code=").append(redirectCode).append("&")
                .append("client_id=").append(CLIENT_API_ID).append("&")
                .append("redirect_uri=").append(REDIRECT_URI).append("&")
                .append("grant_type=authorization_code").toString();
    }

    static String createRefreshBody(String refreshToken) {
        return new StringBuilder()
                .append("client_id=").append(CLIENT_API_ID).append("&")
                .append("refresh_token=").append(refreshToken).append("&")
                .append("grant_type=refresh_token").toString();
    }

    static String createRevokeURL(String accessToken) {
        return new StringBuilder().append(OAUTH_REVOKE_URL).append(accessToken).toString();
    }

    public static boolean isTokenValid(long tokenTimeOut, Clock clock) {
        return tokenTimeOut != -1 && tokenTimeOut > clock.currentTimeInSeconds();
    }
}