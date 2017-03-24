package com.treefrogapps.googlecontactsyncapp.contacts_activity.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.WeakHashMap;

import static com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactsFragment.*;


public class ContactsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles;
    private FragmentManager fm;
    private WeakHashMap<Integer, Fragment> fragmentWeakHashMap;

    public ContactsPagerAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        fragmentWeakHashMap = new WeakHashMap<>();
        this.tabTitles = tabTitles;
        this.fm = fm;
    }

    @Override public Fragment getItem(int position) {
        return getFragment(position);
    }

    @Override public int getCount() {
        return tabTitles.length;
    }

    private Fragment getFragment(int position){
        Fragment fragment = fragmentWeakHashMap.get(position);
        if(fragment != null){
            return fragment;
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", position);
            fragment = newInstance(bundle);
            fragmentWeakHashMap.put(position, fragment);
            return  fragment;
        }
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
