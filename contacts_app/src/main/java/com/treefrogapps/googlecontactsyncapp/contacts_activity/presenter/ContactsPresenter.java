package com.treefrogapps.googlecontactsyncapp.contacts_activity.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.treefrogapps.googlecontactsyncapp.common.base_classes.BasePresenter;
import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IContext;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.MVP;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.ContactsModel;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.Contact;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactsActivity;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;

public class ContactsPresenter extends BasePresenter<MVP.IContactsPresenter, MVP.IContactsModel, ContactsModel>
        implements MVP.IContactsViewPresenter, MVP.IContactsPresenter, IContext {

    private static final String TAG = ContactsPresenter.class.getSimpleName();
    private static final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

    private ContactsModel contactsModel;
    private FragmentManager manager;
    private WeakReference<MVP.IContactsView> contactsViewRef;

    public ContactsPresenter(ContactsModel contactsModel, FragmentManager manager) {
        this.contactsModel = contactsModel;
        this.manager = manager;
    }

    @Override public void onCreate(MVP.IContactsView contactsView) {
        this.contactsViewRef = new WeakReference<>(contactsView);
        super.initialise(contactsModel, this, false);
    }

    @Override public void onConfigChange(MVP.IContactsView contactsView) {
        this.contactsViewRef = new WeakReference<>(contactsView);
        super.initialise(contactsModel, this, true);
    }

    @Override public void onStart() {
        getModel().onStart();
    }

    @Override public void onResume() {
        getModel().onResume();
    }

    @Override public void onPause() {
        getModel().onPause();
    }

    @Override public void onStop() {
        getModel().onStop();
    }

    @Override public void onDestroy(boolean isFinishing) {
        if (isFinishing) getModel().onDestroy();
    }

    @Override public Context getAppContext() {
        return contactsViewRef.get().getAppContext();
    }

    @Override public Context getActivityContext() {
        return contactsViewRef.get().getActivityContext();
    }

    @Override public void setUpDisplay(ContactsActivity activity) {
    }

    @Override public void getAuthConsent() {
        getModel().getAuth();
    }

    @Override public void getAccessToken(String redirectUri) {
        getModel().requestAccessToken(redirectUri);
    }

    @Override public boolean hasAccessOrRefreshToken() {
        return getModel().hasAccessAndRefreshToken();
    }

    @Override public boolean hasPermissions() {
        return checkPermissions(permissions);
    }

    @Override public void requestPermissions(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    @Override public Observable<List<Contact>> getContactsSubject() {
        return getModel().getContactsObservable();
    }

    @Override public Observable<List<Contact>> getPeopleSubject() {
        return getModel().getPeopleSubjectObservable();
    }


    @Override public void revokeAccess() {
        getModel().revokeAccess();
    }

    @Override public void makeApiCall() {
        getModel().makeApiCall();
    }

    @Override public void authSuccessful(boolean successful) {
        Log.i(TAG, "Login successful = " + successful);
    }

    private boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED
                    != ActivityCompat.checkSelfPermission(getActivityContext(), permission)) {
                return false;
            }
        }
        return true;
    }

}
