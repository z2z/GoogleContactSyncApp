package com.treefrogapps.googlecontactsyncapp.contacts_activity.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.treefrogapps.googlecontactsyncapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class ContactsFragment extends Fragment {

    private int fragmentPosition;

    static ContactsFragment newInstance(Bundle bundle) {
        ContactsFragment fragment = new ContactsFragment();
        if (bundle != null) fragment.setArguments(bundle);
        return fragment;
    }

    private OnFragmentListener fragmentListener;
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private Unbinder unbinder;
    private Disposable disposable;

    @BindView(R.id.contacts_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fragment_text_view) TextView fragmentTextView;

    interface OnFragmentListener {
        Observable<List<Contact>> getContactsApiSubject();

        Observable<List<Contact>> getPeopleApiSubject();
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactsActivity) {
            fragmentListener = (OnFragmentListener) context;
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unbinder = ButterKnife.bind(this, getView());
        contactRecyclerAdapter = new ContactRecyclerAdapter(contactList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contactRecyclerAdapter);
        fragmentPosition = getArguments().getInt("POSITION", 10);
    }

    @Override public void onResume() {
        super.onResume();

        if (fragmentPosition == 0) {
            disposable = fragmentListener.getContactsApiSubject().
                    observeOn(AndroidSchedulers.mainThread()).subscribe(this::updateRecyclerView);

        } else if (fragmentPosition == 1) {
            disposable = fragmentListener.getPeopleApiSubject()
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(this::updateRecyclerView);
        }
    }

    @Override public void onPause() {
        super.onPause();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    @Override public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    private void updateRecyclerView(List<Contact> contactList) {
        fragmentTextView.setText((fragmentPosition == 0 ?  getActivity().getString(R.string.contacts_from_api)
                : getActivity().getString(R.string.people_from_api)) + " - " + contactList.size() + " Contacts");
        this.contactList.clear();
        this.contactList.addAll(contactList);
        contactRecyclerAdapter.notifyDataSetChanged();
    }
}