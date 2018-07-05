package com.teamm.friendstracker.present;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.ui.FriendActivity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RVFriendsAdapter extends RecyclerView.Adapter<RVFriendsAdapter.PersonViewHolder>{

    private ArrayList<User> friends =  new ArrayList<>();

    public interface OnCardClickListener {
        void onCardClick(View view,  int position);
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        mListener = listener;
    }

    private static OnCardClickListener mListener;

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_card, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        final int index = i;
        personViewHolder.friendName.setText(friends.get(i).getName()+" "+friends.get(i).getSurname());
        personViewHolder.friendMail.setText(friends.get(i).getEmail());
        //personViewHolder.friendPhoto.setImageResource(friends.get(i).getPhoto());
    }


    public void clearItems() {
        friends.clear();
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<User> newFriends) {
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

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView friendName;
        TextView friendMail;
        ImageView friendPhoto;
        RelativeLayout friendLayout;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvFriend);
            friendName = (TextView)itemView.findViewById(R.id.friend_name);
            friendMail = (TextView)itemView.findViewById(R.id.friend_mail);
            friendPhoto = (ImageView)itemView.findViewById(R.id.friend_photo);
            friendLayout = (RelativeLayout)itemView.findViewById(R.id.friendLayout);
            cv.setOnClickListener(this);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(view.getContext(), view);
                    popup.inflate(R.menu.popup_menu_friends);
                    popup.setOnMenuItemClickListener(PersonViewHolder.this);
                    popup.show();
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            // вызов метода-колбэка и назначение передаваемых значений аргументам
            int position = getAdapterPosition();
            mListener.onCardClick(v, position);
        }
    }

}
