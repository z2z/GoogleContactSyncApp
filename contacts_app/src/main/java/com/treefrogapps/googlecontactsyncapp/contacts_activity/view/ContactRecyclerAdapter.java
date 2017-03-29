package com.treefrogapps.googlecontactsyncapp.contacts_activity.view;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.treefrogapps.googlecontactsyncapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.treefrogapps.googlecontactsyncapp.contacts_activity.view.ContactRecyclerAdapter.ContactViewHolder;

class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<Contact> contactList;

    ContactRecyclerAdapter(List<Contact> contactsList) {
        this.contactList = contactsList;
    }

    @Override public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false));
    }

    @Override public void onViewRecycled(ContactViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.imageView);
    }

    @Override public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.item1.setText(contact.getFirstName());
        holder.item2.setText(contact.getLastName());
        holder.item3.setText(contact.getPhoneNumber());

        if (contact.getImageUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(contact.getImageUrl())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.imageView);
        }
    }

    @Override public int getItemCount() {
        return contactList.size();
    }


    static class ContactViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_image_view) ImageView imageView;
        @BindView(R.id.row_detail_1) TextView item1;
        @BindView(R.id.row_detail_2) TextView item2;
        @BindView(R.id.row_detail_3) TextView item3;

        ContactViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
