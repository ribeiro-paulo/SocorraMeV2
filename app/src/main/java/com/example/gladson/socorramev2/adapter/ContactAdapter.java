package com.example.gladson.socorramev2.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<EmmergencyContact> contactList;
    private Context context;

    public ContactAdapter(List<EmmergencyContact> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemList = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_emmergency_contacts, viewGroup, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        EmmergencyContact emmergencyContact = contactList.get(i);

        myViewHolder.name.setText(emmergencyContact.getName());
        myViewHolder.number.setText(emmergencyContact.getNumber());

        if (emmergencyContact.getPhoto() != null) {
            // TODO FOTO DO CONTATO
        } else {
            myViewHolder.photo.setImageResource(R.drawable.ic_person_gray_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView name, number;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.imageContactPhoto);
            name = itemView.findViewById(R.id.textContactName);
            number = itemView.findViewById(R.id.textContactNumber);

        }
    }
}
