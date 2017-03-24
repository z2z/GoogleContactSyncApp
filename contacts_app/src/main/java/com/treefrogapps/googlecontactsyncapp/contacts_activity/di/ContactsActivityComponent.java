package com.treefrogapps.googlecontactsyncapp.contacts_activity.di;


import com.treefrogapps.googlecontactsyncapp.common.di_scopes.ActivityScope;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactsActivity;

import dagger.Subcomponent;

@ActivityScope @Subcomponent(modules = {ContactsActivityModule.class}) public interface ContactsActivityComponent {

    void inject(ContactsActivity activity);
}
