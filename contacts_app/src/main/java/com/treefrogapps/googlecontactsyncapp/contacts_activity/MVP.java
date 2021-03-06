package com.treefrogapps.googlecontactsyncapp.contacts_activity;

import android.app.Activity;
import android.support.v4.view.ViewPager;

import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IContext;
import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IModelLifeCycle;
import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IPresenterLifeCycle;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.SyncType;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.Contact;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactsActivity;

import java.util.List;

import io.reactivex.Observable;

public interface MVP {

    interface IContactsView extends IContext {

        ViewPager getViewPager();
    }

    interface IContactsViewPresenter extends IPresenterLifeCycle<IContactsView> {

        void setUpDisplay(ContactsActivity activity);

        void getAuthConsent();

        void getAccessToken(String redirectUri);

        boolean hasAccessOrRefreshToken();

        boolean hasPermissions();

        void requestPermissions(Activity activity, int requestCode);

        Observable<List<Contact>> getContactsSubject();

        Observable<List<Contact>> getPeopleSubject();

        void revokeAccess();

        void makeApiCall(SyncType syncType);
    }

    interface IContactsPresenter extends IContext {

        void authSuccessful(boolean successful);

        void queryingApi();
    }

    interface IContactsModel extends IModelLifeCycle<IContactsPresenter> {

        void getAuth();

        void requestAccessToken(String redirectUri);

        Observable<List<Contact>> getContactsObservable();

        Observable<List<Contact>> getPeopleSubjectObservable();

        void makeApiCall(SyncType syncType);

        boolean hasAccessAndRefreshToken();

        void revokeAccess();
    }
}
