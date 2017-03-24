package com.treefrogapps.googlecontactsyncapp.contacts_activity.di;

import android.content.ContentResolver;
import android.support.v4.app.FragmentManager;

import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ActivityScope;
import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ApplicationScope;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.model.ContactsModel;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.presenter.ContactsPresenter;

import dagger.Module;
import dagger.Provides;

@Module public class ContactsActivityModule {

    private FragmentManager fragmentManager;

    public ContactsActivityModule(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Provides @ActivityScope ContactsPresenter providePresenter(ContactsModel model){
        return new ContactsPresenter(model, fragmentManager);
    }


    @Module public static class ApplicationScoped {

        @Provides @ApplicationScope ContactsModel provideModel(ContentResolver resolver) {
            return new ContactsModel(resolver);
        }
    }
}
