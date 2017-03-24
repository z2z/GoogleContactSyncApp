package com.treefrogapps.googlecontactsyncapp.contacts_service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils;
import com.treefrogapps.googlecontactsyncapp.contacts_service.clients.ContactsApiClient;
import com.treefrogapps.googlecontactsyncapp.contacts_service.clients.PeopleApiClient;
import com.treefrogapps.googlecontactsyncapp.contacts_service.di.LoginServiceModule;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.treefrogapps.googlecontactsyncapp.application.ContactsApplication.*;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.*;

public class ApiService extends Service {

    private static final String TAG = ApiService.class.getSimpleName();

    private LoginServiceBinder loginServiceBinder;

    @Inject OkHttpClient client;
    @Inject ContactsApiClient contactsApiClient;
    @Inject PeopleApiClient peopleApiClient;

    @Override public void onCreate() {
        super.onCreate();
        getApplicationComponent(this).addContactsLoginService(new LoginServiceModule()).inject(this);
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
                        Log.i(TAG, response.body().string());
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
