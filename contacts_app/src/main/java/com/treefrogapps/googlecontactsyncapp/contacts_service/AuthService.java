package com.treefrogapps.googlecontactsyncapp.contacts_service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent;
import com.treefrogapps.googlecontactsyncapp.common.LoginRevokedIntent;
import com.treefrogapps.googlecontactsyncapp.contacts_service.clients.ContactsApiClient;
import com.treefrogapps.googlecontactsyncapp.contacts_service.clients.PeopleApiClient;
import com.treefrogapps.googlecontactsyncapp.contacts_service.di.ApiServiceModule;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.treefrogapps.googlecontactsyncapp.application.ContactsApplication.getApplicationComponent;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.*;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.AUTH_TOKEN_END_POINT;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.createBody;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.createRefreshBody;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.createRevokeURL;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.getAccessToken;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.getAccessTokenTimeout;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.getRefreshToken;

public class AuthService extends Service {

    private static final String TAG = AuthService.class.getSimpleName();

    private LoginServiceBinder loginServiceBinder;

    @Inject OkHttpClient client;
    @Inject ContactsApiClient contactsApiClient;
    @Inject PeopleApiClient peopleApiClient;
    @Inject SharedPreferences preferences;

    @Override public void onCreate() {
        super.onCreate();
        getApplicationComponent(this).addContactsLoginService(new ApiServiceModule()).inject(this);
        loginServiceBinder = new LoginServiceBinder(this);
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        return loginServiceBinder;
    }

    @Override public boolean onUnbind(Intent intent) {
        loginServiceBinder.onUnbind();
        return true;
    }

    public static class LoginServiceBinder extends Binder {
        private AuthService authService;

        LoginServiceBinder(AuthService service) {
            this.authService = service;
        }

        public AuthService getAuthService() {
            return authService;
        }

        void onUnbind() {
            this.authService = null;
        }
    }

    public void requestAccessTokenFromRefreshToken(String refreshToken) {
        MediaType type = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(type, createRefreshBody(refreshToken));
        Request request = new Request.Builder().post(body).url(AUTH_TOKEN_END_POINT).build();
        makeAuthRequest(client, request);
    }

    public void requestAccessToken(String redirectUri) {
        String[] redirectArray = redirectUri.split("=");
        if (redirectArray.length == 2) {
            MediaType type = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(type, createBody(redirectArray[1]));
            Request request = new Request.Builder().post(body).url(AUTH_TOKEN_END_POINT).build();
            makeAuthRequest(client, request);
        }
    }

    public void revokeAccess(String accessToken) {
        Request request = new Request.Builder().url(createRevokeURL(accessToken)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to revoke access : " + e.toString());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "Response Code : " + response.code());
                if (response.isSuccessful()) {
                    LocalBroadcastManager.getInstance(AuthService.this).sendBroadcast(new LoginRevokedIntent());
                }
            }
        });
    }

    private void makeAuthRequest(OkHttpClient client, Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "Response Code : " + response.code());
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    Log.i(TAG, responseString);
                    LocalBroadcastManager.getInstance(AuthService.this)
                            .sendBroadcast(new LoginAccessIntent(getAccessToken(responseString),
                                    getRefreshToken(responseString) != null ? getRefreshToken(responseString) :
                                            preferences.getString(REFRESH_TOKEN_EXTRA, null),
                                    getAccessTokenTimeout(responseString)));
                }
            }
        });
    }
}
