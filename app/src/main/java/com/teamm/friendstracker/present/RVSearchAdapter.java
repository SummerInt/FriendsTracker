package com.teamm.friendstracker.present;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.entity.User;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RVSearchAdapter extends RecyclerView.Adapter<RVSearchAdapter.PersonViewHolder>{

    private ArrayList<User>friends =  new ArrayList<User>();

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_search_card, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.tvName.setText(friends.get(i).getName()+" "+friends.get(i).getSurname());
        personViewHolder.tvMail.setText(friends.get(i).getEmail());
        personViewHolder.bAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // и тут уже как выше через friends.get(i) получаем инфу userа
            }
        });

    }

    public void clearItems() {
        friends.clear();
        notifyDataSetChanged();
    }

    public void setItems(Collection<User> newFriends) {
        friends.addAll(newFriends);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return friends.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvName;
        TextView tvMail;
        Button bAdd;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvFriendSearch);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvMail = (TextView)itemView.findViewById(R.id.tvMail);
            bAdd= (Button)itemView.findViewById(R.id.bAdd);
        }


    }

}

