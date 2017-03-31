package com.treefrogapps.googlecontactsyncapp.contacts_activity.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.treefrogapps.googlecontactsyncapp.R;
import com.treefrogapps.googlecontactsyncapp.common.base_classes.BaseActivity;
import com.treefrogapps.googlecontactsyncapp.common.base_interfaces.IContext;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.MVP;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.di.ContactsActivityModule;
import com.treefrogapps.googlecontactsyncapp.contacts_activity.presenter.ContactsPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

import static com.treefrogapps.googlecontactsyncapp.application.ContactsApplication.getApplicationComponent;
import static com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactsFragment.OnFragmentListener;

public class ContactsActivity extends BaseActivity<MVP.IContactsView, MVP.IContactsViewPresenter, ContactsPresenter>
        implements IContext, MVP.IContactsView, OnFragmentListener {

    private static final String TAG = ContactsActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 10;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private Unbinder unbinder;
    private ContactsPagerAdapter contactsPagerAdapter;
    private String[] tabs = new String[]{"Contacts Api", "People Api"};

    @Inject ContactsPresenter contactsPresenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getApplicationComponent(this).addContactsComponent(new ContactsActivityModule(getSupportFragmentManager())).inject(this);
        unbinder = ButterKnife.bind(this);
        super.initialise(contactsPresenter, this, savedInstanceState != null);
        contactsPagerAdapter = new ContactsPagerAdapter(getSupportFragmentManager(), tabs);

        setUpDisplay();

        getPresenter().makeApiCall();
    }

    private void setUpDisplay() {
        setSupportActionBar(toolbar);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(contactsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actvitiy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_get_contacts:
                getAuthConsent();
                return true;
            case R.id.menu_remove_access:
                getPresenter().revokeAccess();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean hasPermissions = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = false;
                    break;
                }
            }
            if (hasPermissions) getAuthConsent();
        }
    }

    @Override protected void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override protected void onResume() {
        super.onResume();

        if (getIntent() != null && getIntent().getData() != null) {
            Log.i(getClass().getSimpleName(), "returned with redirect uri : " + getIntent().getData().toString());
            getPresenter().getAccessToken(getIntent().getData().toString());
        }
        getPresenter().onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        getPresenter().onPause();
    }

    @Override protected void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy(!isChangingConfigurations());
        unbinder.unbind();
    }


    @Override public Observable<List<Contact>> getContactsApiSubject() {
        return getPresenter().getContactsSubject();
    }

    @Override public Observable<List<Contact>> getPeopleApiSubject() {
        return getPresenter().getPeopleSubject();
    }

    private void getAuthConsent() {
        if (getPresenter().hasPermissions()) {
            if (!getPresenter().hasAccessOrRefreshToken()) {
                getPresenter().getAuthConsent();
            }
        } else {
            getPresenter().requestPermissions(this, PERMISSION_REQUEST_CODE);
        }
    }
}
