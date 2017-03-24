package com.treefrogapps.googlecontactsyncapp.contacts_activity.presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;

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

    private ContactsModel contactsModel;
    private FragmentManager manager;
    private WeakReference<MVP.IContactsView> contactsViewRef;

    public ContactsPresenter(ContactsModel contactsModel, FragmentManager manager){
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
        getModel().getAccessToken(redirectUri);
    }

    @Override public Observable<List<Contact>> getContactsObservable() {
        return getModel().getContactsObservable();
    }

    @Override public Observable<List<Contact>> getPeopleObservable() {
        return getModel().getPeopleObservable();
    }

    @Override public void results(String results) {

    }



}
