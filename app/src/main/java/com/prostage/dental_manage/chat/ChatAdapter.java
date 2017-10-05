package com.prostage.dental_manage.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.utils.OnSingleClickListener;
import com.prostage.dental_manage.utils.Utils;

import java.util.ArrayList;

import static com.prostage.dental_manage.utils.AppConstants.ARG_CHAT_ROOMS;
import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_ID;

/**
 * Created by congnc on 3/30/17.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<UserModel> userModels = new ArrayList<>();
    private OnChatAdapterListener listener;
    //private DatabaseReference mChatRef;
    //private int adminId;

    public ChatAdapter(Context context, ArrayList<UserModel> models) {
        this.context = context;
        this.userModels = models;
        //adminId = Utils.getInt(context, PREF_ADMIN_ID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        vh = new ChatHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserModel userModel = userModels.get(position);
        int dentistId = Utils.getInt(context, PREF_ADMIN_ID);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(ARG_CHAT_ROOMS)
                .child(dentistId + "_" + userModel.getUserCode());
        if (holder instanceof ChatHolder) {
            ChatHolder chatHolder = (ChatHolder) holder;
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        int unread = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            boolean read = (boolean) child.child("read").getValue();
                            String senderId = (String) child.child("senderId").getValue();
                            if (userModel.getUserCode().equals(senderId) && !read) {
                                unread++;
                            }
                        }
                        if (unread > 0) {
                            chatHolder.tvNumberChat.setVisibility(View.VISIBLE);
                            chatHolder.tvNumberChat.setText(String.valueOf(unread));
                        } else {
                            chatHolder.tvNumberChat.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            chatHolder.tvId.setText(String.valueOf(userModel.getUserCode()));
            chatHolder.tvName.setText(userModel.getFirstName() + " " + userModel.getLastName());


            if (position % 2 == 0) {
                chatHolder.userLayout.setBackgroundColor(
                        Utils.getColor(context, R.color.colorLightYellow));
            } else {
                chatHolder.userLayout.setBackgroundColor(
                        Utils.getColor(context, R.color.colorLightOrange));
            }
            chatHolder.userLayout.setTag(userModel);
            chatHolder.userLayout.setOnClickListener(mySingleListener);
        }
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public void setList(ArrayList<UserModel> userModels) {
        this.userModels.clear();
        this.userModels.addAll(userModels);
        notifyDataSetChanged();
    }

    public void setUnread(int unread, String receiverId) {
        for (int i = 0; i < userModels.size(); i++) {
            if (userModels.get(i).getUserCode().equals(receiverId)) {
                userModels.get(i).setNumberUnread(unread);
                notifyItemChanged(i);
                break;
            }
        }
    }

    static class ChatHolder extends RecyclerView.ViewHolder {
        final TextView tvId;
        final TextView tvName;
        final TextView tvNumberChat;
        final RelativeLayout userLayout;

        ChatHolder(View view) {
            super(view);
            tvId = (TextView) view.findViewById(R.id.tv_user_id);
            tvName = (TextView) view.findViewById(R.id.tv_user_name);
            tvNumberChat = (TextView) view.findViewById(R.id.tv_number_chat);
            userLayout = (RelativeLayout) view.findViewById(R.id.chat_layout);
        }
    }

    public interface OnChatAdapterListener {
        void onShowMessage(String receiverId, String chatRoomId, String name, int unreadCount, int gender);
    }

    public void setListener(OnChatAdapterListener listener) {
        this.listener = listener;
    }

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            UserModel userModel = (UserModel) v.getTag();
            if (listener != null && userModel != null) {
                int refreshCountChat = 0;
                listener.onShowMessage(userModel.getUserCode(),
                        Utils.getInt(context, Utils.PREF_ADMIN_ID) + "_" + userModel.getUserCode(),
                        userModel.getFirstName(),
                        refreshCountChat,
                        userModel.getGender());
            }
        }
    };
}
