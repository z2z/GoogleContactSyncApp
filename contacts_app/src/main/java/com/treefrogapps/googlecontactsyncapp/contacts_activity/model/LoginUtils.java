package com.treefrogapps.googlecontactsyncapp.contacts_activity.model;


import android.support.annotation.Nullable;

final public class LoginUtils {

    public static final String OAUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth?";
    public static final String AUTH_TOKEN_END_POINT = "https://www.googleapis.com/oauth2/v4/token";
    public static final String CLIENT_API_ID = "1058427583280-5vh5r0c2qi8bbh1o26oudu5hlbm5lldi.apps.googleusercontent.com";
    public static final String SCOPE = "https://www.googleapis.com/auth/contacts.readonly";
    public static final String REDIRECT_URI = "com.treefrogapps.googlecontactsyncapp:/oauth_redirect";

    static String generateAuthorisationUrl(String authUrl, String clientId, String scope, String redirectUri, @Nullable String state){
        return new StringBuilder(authUrl)
                .append("scope=").append(scope)
                .append("&response_type=code")
                .append(state != null ? "&state=" : "").append(state != null ? state : "")
                .append("&redirect_uri=").append(redirectUri)
                .append("&client_id=").append(clientId).toString();
    }

}
