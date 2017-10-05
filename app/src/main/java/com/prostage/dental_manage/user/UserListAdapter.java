package com.prostage.dental_manage.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.utils.OnSingleClickListener;

import java.util.ArrayList;

/**
 * Created by congnc on 4/3/17.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserModel> userModels;
    private OnUserListAdapterListener listener;

    public UserListAdapter(Context context, ArrayList<UserModel> models) {
        this.userModels = models;
        this.context = context;
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);

        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        UserModel userModel = userModels.get(position);

        TextView tvLastName = holder.tvLastName;
        TextView tvGivenName = holder.tvGivenName;
        TextView tvSex = holder.tvSex;
        ImageView ivChat = holder.ivChat;
        RelativeLayout userLayout = holder.userLayout;
        View vDivider = holder.vDivider;

        tvLastName.setText(userModel.getLastName());
        tvGivenName.setText(userModel.getFirstName());
        tvSex.setText("様");

        if (position == userModels.size() - 1) {
            vDivider.setVisibility(View.GONE);
        }

        ivChat.setTag(userModel);
        ivChat.setOnClickListener(mySingleListener);

        userLayout.setTag(userModel);
        userLayout.setOnClickListener(mySingleListener);
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvLastName;
        final TextView tvGivenName;
        final TextView tvSex;
        final ImageView ivChat;
        final RelativeLayout userLayout;
        final View vDivider;

        ViewHolder(View view) {
            super(view);
            tvLastName = (TextView) view.findViewById(R.id.tv_last_name);
            tvGivenName = (TextView) view.findViewById(R.id.tv_given_name);
            tvSex = (TextView) view.findViewById(R.id.tv_sex);
            ivChat = (ImageView) view.findViewById(R.id.iv_chat_user);
            userLayout = (RelativeLayout) view.findViewById(R.id.user_layout);
            vDivider = view.findViewById(R.id.v_divider);
        }
    }

    public void setList(ArrayList<UserModel> models) {
        this.userModels.clear();
        this.userModels.addAll(models);
        notifyDataSetChanged();
    }

    public interface OnUserListAdapterListener {
        void onChatItemClicked(UserModel userModel);

        void onUserClicked(UserModel userModel);
    }

    public void setListener(OnUserListAdapterListener listener) {
        this.listener = listener;
    }

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            UserModel userModel = (UserModel) v.getTag();
            if (listener != null && userModel != null) {
                switch (v.getId()) {
                    case R.id.iv_chat_user:
                        listener.onChatItemClicked(userModel);
                        break;
                    case R.id.user_layout:
                        listener.onUserClicked(userModel);
                        break;
                }
            }
        }
    };
}
