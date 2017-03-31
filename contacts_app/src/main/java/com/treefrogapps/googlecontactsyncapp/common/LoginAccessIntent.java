package com.treefrogapps.googlecontactsyncapp.common;

import android.content.Intent;


public class LoginAccessIntent extends Intent {

    public static final String ACTION_LOGIN_SUCCESSFUL = "com.treefrogapps.googlecontactsyncapp.login_successful";
    public static final String ACCESS_TOKEN_EXTRA = "access_token_extra";
    public static final String ACCESS_TOKEN_TIMEOUT = "access_token_timeout";
    public static final String REFRESH_TOKEN_EXTRA = "refresh_token_extra";

    public LoginAccessIntent(String accessToken, String refreshToken, int accessTokenTimeout) {
        super(ACTION_LOGIN_SUCCESSFUL);

        putExtra(ACCESS_TOKEN_EXTRA, accessToken);
        putExtra(ACCESS_TOKEN_TIMEOUT, accessTokenTimeout);
        putExtra(REFRESH_TOKEN_EXTRA, refreshToken);
    }

    public static boolean matchesAction(Intent intent) {
        return intent.getAction().equals(ACTION_LOGIN_SUCCESSFUL);
    }
}
