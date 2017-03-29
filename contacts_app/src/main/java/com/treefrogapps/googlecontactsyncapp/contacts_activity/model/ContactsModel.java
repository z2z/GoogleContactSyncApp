package com.treefrogapps.googlecontactsyncapp.contacts_activity.model;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent;
import com.treefrogapps.googlecontactsyncapp.common.LoginRevokedIntent;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.MVP;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.Contact;
import com.treefrogapps.googlecontactsyncapp.contacts_service.AuthService;
import com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.ContactDataModel;
import com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.ModelConverter;
import com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.PeopleDataModel;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.ACCESS_TOKEN_EXTRA;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.ACCESS_TOKEN_TIMEOUT;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.ACTION_LOGIN_SUCCESSFUL;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.REFRESH_TOKEN_EXTRA;
import static com.treefrogapps.googlecontactsyncapp.common.LoginRevokedIntent.*;
import static com.treefrogapps.googlecontactsyncapp.common.LoginRevokedIntent.LOGIN_REVOKED;
import static com.treefrogapps.googlecontactsyncapp.common.RxUtils.RxBroadcastReceiver;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.CLIENT_API_ID;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.OAUTH_URL;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.REDIRECT_URI;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.SCOPE;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.generateAuthorisationUrl;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.AuthUtils.isTokenValid;
import static com.treefrogapps.googlecontactsyncapp.contacts_service.data_model.ModelConverter.*;

public class ContactsModel implements MVP.IContactsModel {

    // https://people.googleapis.com/v1/people/me/connections?requestMask.includeField=person.names,person.addresses,person.email_addresses,person.organizations,person.phone_numbers,person.photos - get request  : people request (add access token as header?)
    // https://www.google.com/m8/feeds/contacts/default/full/ - get request  : contacts request (add access token as header?)

    private static final String PEOPLE_URL = "https://people.googleapis.com/v1/people/me/connections?requestMask.includeField=" +
            "person.names,person.addresses,person.email_addresses,person.organizations,person.phone_numbers,person.photos";
    private static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full";
    private static final String TAG = ContactsModel.class.getSimpleName();

    private WeakReference<MVP.IContactsPresenter> presenterRef;
    private ContentResolver resolver;
    private SharedPreferences preferences;
    private OkHttpClient client;
    private ObjectMapper xmlMapper;
    private ObjectMapper jsonMapper;
    private Clock clock;
    private AuthService authService;
    private boolean boundToService;
    private CompositeDisposable disposable;
    private BehaviorSubject<ContactDataModel> contactsSubject;
    private BehaviorSubject<PeopleDataModel> peopleSubject;

    public ContactsModel(ContentResolver resolver, SharedPreferences preferences,
                         OkHttpClient client, XmlMapper xmlMapper, ObjectMapper jsonMapper, Clock clock) {

        this.resolver = resolver;
        this.preferences = preferences;
        this.client = client;
        this.xmlMapper = xmlMapper;
        this.jsonMapper = jsonMapper;
        this.clock = clock;
    }

    @Override public void onCreate(MVP.IContactsPresenter contactsPresenter) {
        this.presenterRef = new WeakReference<>(contactsPresenter);

        disposable = new CompositeDisposable();
        peopleSubject = BehaviorSubject.create();
        contactsSubject = BehaviorSubject.create();
    }

    @Override public void onConfigChange(MVP.IContactsPresenter contactsPresenter) {
        this.presenterRef = new WeakReference<>(contactsPresenter);
    }

    @Override public void onStart() {
        presenterRef.get().getActivityContext()
                .bindService(new Intent(presenterRef.get().getActivityContext(), AuthService.class),
                        loginServiceConnection, BIND_AUTO_CREATE);

        disposable.add(RxBroadcastReceiver(presenterRef.get().getAppContext(),
                new IntentFilter(ACTION_LOGIN_SUCCESSFUL), true).subscribe(this::serviceCallback));

        disposable.add(RxBroadcastReceiver(presenterRef.get().getAppContext(),
                new IntentFilter(LOGIN_REVOKED), true).subscribe(this::loginRevoked));
    }

    @Override public void onResume() {

    }

    @Override public void onPause() {

    }

    @Override public void onStop() {
        presenterRef.get().getActivityContext().unbindService(loginServiceConnection);
        disposable.dispose();
    }

    @Override public void onDestroy() {
        peopleSubject = null;
        contactsSubject = null;
    }

    @Override public void getAuth() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(generateAuthorisationUrl(OAUTH_URL, CLIENT_API_ID, SCOPE, REDIRECT_URI, null)));
        presenterRef.get().getActivityContext().startActivity(intent);
    }

    @Override public void requestAccessToken(String redirectUri) {
        if (boundToService) authService.requestAccessToken(redirectUri);
    }

    @Override public void makeApiCall() {
        String accessToken = preferences.getString(ACCESS_TOKEN_EXTRA, null);
        long tokenTimeOut = preferences.getLong(ACCESS_TOKEN_TIMEOUT, -1);
        if (accessToken != null && isTokenValid(tokenTimeOut, clock)) {
            makePeopleApiCall(accessToken);
            makeContactsApiCall(accessToken);
        } else if (preferences.getString(REFRESH_TOKEN_EXTRA, null) != null) {
            authService.requestAccessTokenFromRefreshToken(preferences.getString(ACCESS_TOKEN_EXTRA, null));
        } else {
            Log.w(TAG, "No auth token, or refresh token available : requesting auth..");
            getAuth();
        }
    }

    @Override public Observable<List<Contact>> getContactsObservable() {
        return contactsSubject.observeOn(Schedulers.io()).flatMap(contactDataModel -> Observable.just(convertContactDataModel(contactDataModel)));
    }

    @Override public Observable<List<Contact>> getPeopleSubjectObservable() {
        return peopleSubject.observeOn(Schedulers.io()).flatMap(peopleDataModel -> Observable.just(convertPeopleDataModel(peopleDataModel)));
    }


    @Override public boolean hasAccessAndRefreshToken() {
        return preferences.getString(ACCESS_TOKEN_EXTRA, null) != null &&
                preferences.getString(REFRESH_TOKEN_EXTRA, null) != null;
    }

    @Override public void revokeAccess() {
        String accessToken = preferences.getString(ACCESS_TOKEN_EXTRA, null);
        if (boundToService && accessToken != null) {
            authService.revokeAccess(accessToken);
        }
        preferences.edit().clear().apply();
        contactsSubject.onNext(new ContactDataModel());
        peopleSubject.onNext(new PeopleDataModel());
    }

    private ServiceConnection loginServiceConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            AuthService.LoginServiceBinder binder = (AuthService.LoginServiceBinder) service;
            authService = binder.getAuthService();
            boundToService = true;
        }

        @Override public void onServiceDisconnected(ComponentName name) {
            boundToService = false;
        }
    };


    private void makeContactsApiCall(String accessToken) {
        Request request = new Request.Builder().url(CONTACTS_URL + "?access_token=" + accessToken).build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error from Contacts Api Call : " + e.toString());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    ContactDataModel contactDataModel = xmlMapper.readValue(response.body().toString(), ContactDataModel.class);
                    contactsSubject.onNext(contactDataModel);
                } else {
                    Log.w(TAG, "Contacts Response Failed - Code : " + response.code());
                }
            }
        });
    }

    private void makePeopleApiCall(String accessToken) {
        Request request = new Request.Builder().url(PEOPLE_URL + "&access_token=" + accessToken).build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error from People Api Call : " + e.toString());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    PeopleDataModel peopleDataModel = jsonMapper.readValue(response.body().toString(), PeopleDataModel.class);
                    peopleSubject.onNext(peopleDataModel);
                } else {
                    Log.w(TAG, "People Response Failed - Code : " + response.code());
                }
            }
        });
    }

    private void serviceCallback(Intent intent) {
        Log.i(TAG, "Service Callback");
        if(LoginAccessIntent.matchesAction(intent)) {
            String accessToken = intent.getStringExtra(ACCESS_TOKEN_EXTRA);
            String refreshToken = intent.getStringExtra(REFRESH_TOKEN_EXTRA);
            int timeout = intent.getIntExtra(ACCESS_TOKEN_TIMEOUT, -1);
            if (refreshToken != null && accessToken != null && timeout != -1) {
                Editor editor = preferences.edit();
                editor.putString(REFRESH_TOKEN_EXTRA, refreshToken);
                editor.putString(ACCESS_TOKEN_EXTRA, accessToken);
                editor.putLong(ACCESS_TOKEN_TIMEOUT, (timeout - 180) + clock.currentTimeInSeconds());
                editor.apply();
                makeApiCall();
            }
        }
    }

    private void loginRevoked(Intent intent) {
        Log.i(TAG, "Service Callback");
        if(matchesAction(intent)) {
            Toast.makeText(presenterRef.get().getActivityContext(), "Access to Api Revoked", Toast.LENGTH_SHORT).show();
        }
    }
}
