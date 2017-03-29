package com.treefrogapps.googlecontactsyncapp.common;

import android.content.Intent;


public class LoginRevokedIntent extends Intent {

    public static final String LOGIN_REVOKED = "com.treefrogapps.googlecontactsyncapp.login_revokedl";

    public LoginRevokedIntent(String accessToken, String refreshToken, int accessTokenTimeout) {
        super(LOGIN_REVOKED);
    }

    public static boolean matchesAction(Intent intent) {
        return intent.getAction().equals(LOGIN_REVOKED);
    }
}
