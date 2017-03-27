package com.treefrogapps.googlecontactsyncapp.contacts_service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent;
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
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.AUTH_TOKEN_END_POINT;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.CLIENT_API_ID;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.REDIRECT_URI;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.ApiUtils.getAccessToken;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.ApiUtils.getRefreshToken;

public class ApiService extends Service {

    // https://people.googleapis.com/v1/people/me/connections - get request  : people request (add access token as header?)
    // https://www.google.com/m8/feeds/contacts/default/full/ - get request  : contacts request (add access token as header?)

    private static final String TAG = ApiService.class.getSimpleName();

    private LoginServiceBinder loginServiceBinder;

    @Inject OkHttpClient client;
    @Inject ContactsApiClient contactsApiClient;
    @Inject PeopleApiClient peopleApiClient;

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
        private ApiService apiService;

        LoginServiceBinder(ApiService service) {
            this.apiService = service;
        }

        public ApiService getApiService(){
            return apiService;
        }

        void onUnbind(){
            this.apiService = null;
        }
    }

    public void requestAccessToken(String redirectUri){
        String[] redirectArray = redirectUri.split("=");
        if(redirectArray.length == 2) {
            MediaType type = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(type, createBody(redirectArray[1]));
            Request request = new Request.Builder().post(body).url(AUTH_TOKEN_END_POINT).build();
            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "Response Code : " + response.code());
                    if(response.isSuccessful()){
                        String responseString = response.body().string();
                        Log.i(TAG, response.body().string());

                        LocalBroadcastManager.getInstance(ApiService.this)
                                .sendBroadcast(new LoginAccessIntent(getAccessToken(responseString), getRefreshToken(responseString)));

                    }
                }
            });
        }
    }

    private String createBody(String redirectCode) {
        return new StringBuilder()
                .append("code=").append(redirectCode).append("&")
                .append("client_id=").append(CLIENT_API_ID).append("&")
                .append("redirect_uri=").append(REDIRECT_URI).append("&")
                .append("grant_type=authorization_code").toString();
    }
}
