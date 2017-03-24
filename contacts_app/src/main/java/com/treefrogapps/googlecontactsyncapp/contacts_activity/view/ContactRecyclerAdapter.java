package com.treefrogapps.googlecontactsyncapp.contacts_activity.view;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.treefrogapps.googlecontactsyncapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactRecyclerAdapter.*;

class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactViewHolder>{

    private List<Contact> contactList;

    public ContactRecyclerAdapter(List<Contact> contactsList) {
        this.contactList = contactsList;
    }

    @Override public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false));
    }

    @Override public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.item1.setText(contact.getItem1());
        holder.item2.setText(contact.getItem2());
        holder.item3.setText(contact.getItem3());
    }

    @Override public int getItemCount() {
        return contactList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_detail_1) TextView item1;
        @BindView(R.id.row_detail_2) TextView item2;
        @BindView(R.id.row_detail_3) TextView item3;

        ContactViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
