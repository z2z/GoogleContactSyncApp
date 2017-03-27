package com.treefrogapps.googlecontactsyncapp.contacts_activity;


import android.app.Activity;

import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IContext;
import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IModelLifeCycle;
import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IPresenterLifeCycle;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.Contact;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactsActivity;

import java.util.List;

import io.reactivex.Observable;

public interface MVP {

    interface IContactsView extends IContext {

        void getAuthConsent();
    }

    interface IContactsViewPresenter extends IPresenterLifeCycle<IContactsView>{

        void setUpDisplay(ContactsActivity activity);
        void getAuthConsent();
        void getAccessToken(String redirectUri);
        boolean hasPermissions();
        void requestPermissions(Activity activity, int requestCode);
        Observable<List<Contact>> getContactsObservable();
        Observable<List<Contact>> getPeopleObservable();
        void revokeAccess();
    }

    interface IContactsPresenter extends  IContext {

        void results(String results);
    }

    interface IContactsModel extends IModelLifeCycle<IContactsPresenter> {

        void getAuth();
        void requestAccessToken(String redirectUri);
        Observable<List<Contact>> getContactsObservable();
        Observable<List<Contact>> getPeopleObservable();
        void revokeAccess();
    }
}
