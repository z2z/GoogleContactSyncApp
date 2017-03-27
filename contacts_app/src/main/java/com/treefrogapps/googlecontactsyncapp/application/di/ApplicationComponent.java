package com.treefrogapps.googlecontactsyncapp.application.di;

import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ApplicationScope;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.di.ContactsActivityComponent;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.di.ContactsActivityModule;
import com.treefrogapps.googlecontactsyncapp.contacts_service.di.ApiServiceComponent;
import com.treefrogapps.googlecontactsyncapp.contacts_service.di.ApiServiceModule;

import dagger.Component;

@ApplicationScope @Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    ContactsActivityComponent addContactsComponent(ContactsActivityModule contactsActivityModule);
    ApiServiceComponent addContactsLoginService(ApiServiceModule serviceModule);
}
