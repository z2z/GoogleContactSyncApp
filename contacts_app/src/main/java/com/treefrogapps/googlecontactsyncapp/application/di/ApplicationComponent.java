package com.treefrogapps.googlecontactsyncapp.application.di;

import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ApplicationScope;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.di.ContactsActivityComponent;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.di.ContactsActivityModule;
import com.treefrogapps.googlecontactsyncapp.contacts_service.di.LoginServiceComponent;
import com.treefrogapps.googlecontactsyncapp.contacts_service.di.LoginServiceModule;

import dagger.Component;

@ApplicationScope @Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    ContactsActivityComponent addContactsComponent(ContactsActivityModule contactsActivityModule);
    LoginServiceComponent addContactsLoginService(LoginServiceModule serviceModule);
}
