package com.treefrogapps.googlecontactsyncapp.contacts_activity.model;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;

import com.treefrogapps.googlecontactsyncapp.contacts_activity.MVP;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.Contact;
import com.treefrogapps.googlecontactsyncapp.contacts_service.ApiService;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.ACCESS_TOKEN_EXTRA;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.ACTION_LOGIN_SUCCESSFUL;
import static com.treefrogapps.googlecontactsyncapp.common.LoginAccessIntent.REFRESH_TOKEN_EXTRA;
import static com.treefrogapps.googlecontactsyncapp.common.RxUtils.RxBroadcastReceiver;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.CLIENT_API_ID;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.OAUTH_URL;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.REDIRECT_URI;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.model.LoginUtils.SCOPE;

public class ContactsModel implements MVP.IContactsModel {

    private WeakReference<MVP.IContactsPresenter> presenterRef;
    private ContentResolver resolver;
    private SharedPreferences preferences;
    private ApiService apiService;
    private boolean boundToService;
    private Disposable disposable;

    private String accessToken;

    public ContactsModel(ContentResolver resolver, SharedPreferences preferences) {
        this.resolver = resolver;
        this.preferences = preferences;
    }

    @Override public void onCreate(MVP.IContactsPresenter contactsPresenter) {
        this.presenterRef = new WeakReference<>(contactsPresenter);

    }

    @Override public void onConfigChange(MVP.IContactsPresenter contactsPresenter) {
        this.presenterRef = new WeakReference<>(contactsPresenter);
    }

    @Override public void onStart() {
        presenterRef.get().getActivityContext()
                .bindService(new Intent(presenterRef.get().getActivityContext(), ApiService.class),
                        loginServiceConnection, BIND_AUTO_CREATE);

        disposable = RxBroadcastReceiver(presenterRef.get().getAppContext(),
                new IntentFilter(ACTION_LOGIN_SUCCESSFUL), true).subscribe(this::serviceCallback);

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

    }


    @Override public void getAuth() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(LoginUtils.generateAuthorisationUrl(OAUTH_URL, CLIENT_API_ID, SCOPE, REDIRECT_URI, null)));
        presenterRef.get().getActivityContext().startActivity(intent);
    }

    @Override public void requestAccessToken(String redirectUri) {
        if(boundToService) apiService.requestAccessToken(redirectUri);
    }

    @Override public Observable<List<Contact>> getContactsObservable() {
        // TODO
        return null;
    }

    @Override public Observable<List<Contact>> getPeopleObservable() {
        //TODO
        return null;
    }

    @Override public void revokeAccess() {
        accessToken = null;
        preferences.edit().clear().apply();
        // TODO - clear Observable / PublishSubject call onNext(emptyList)
    }

    private ServiceConnection loginServiceConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
            ApiService.LoginServiceBinder binder = (ApiService.LoginServiceBinder) service;
            apiService = binder.getApiService();
            boundToService = true;
        }

        @Override public void onServiceDisconnected(ComponentName name) {
            boundToService = false;
        }
    };

    private void serviceCallback(Intent intent){
        accessToken = intent.getStringExtra(ACCESS_TOKEN_EXTRA);
        preferences.edit().putString(REFRESH_TOKEN_EXTRA, intent.getStringExtra(REFRESH_TOKEN_EXTRA)).apply();
    }
}
