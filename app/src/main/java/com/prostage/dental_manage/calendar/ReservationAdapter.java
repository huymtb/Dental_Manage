package com.prostage.dental_manage.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.core.model.ReservationModel;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.utils.OnSingleClickListener;

import java.util.ArrayList;

class ReservationAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<UserModel> userModels;
    private OnReservationAdapterListener listener;

    ReservationAdapter(Context context, ArrayList<UserModel> models) {
        this.userModels = models;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_request, parent, false);

        vh = new ReservationAdapter.ReservationHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReservationHolder) {
            ReservationHolder reservationHolder = (ReservationHolder) holder;
            UserModel userModel = userModels.get(position);
            ReservationModel reservationModel = userModel.getReservations().get(0);

            if (position == 0) {
                reservationHolder.vPrev.setVisibility(View.VISIBLE);
            } else {
                reservationHolder.vPrev.setVisibility(View.GONE);
            }
            if (checkPrevPosition(position)) {
                reservationHolder.tvTime.setVisibility(View.GONE);
                reservationHolder.timeLayout.setVisibility(View.GONE);

            } else {
                reservationHolder.tvTime.setVisibility(View.VISIBLE);
                String hour;
                String minute;
                if (reservationModel.getReservationHour() < 10) {
                    hour = "0" + reservationModel.getReservationHour();
                } else {
                    hour = String.valueOf(reservationModel.getReservationHour());
                }
                if (reservationModel.getReservationMin() < 10) {
                    minute = "0" + reservationModel.getReservationMin();
                } else {
                    minute = String.valueOf(reservationModel.getReservationMin());
                }
                reservationHolder.tvTime.setText(hour + ":" + minute);
                reservationHolder.timeLayout.setVisibility(View.VISIBLE);
            }
            reservationHolder.tvLastName.setText(userModel.getLastName());
            reservationHolder.tvGivenName.setText(userModel.getFirstName());
            reservationHolder.tvSex.setText("様");

            if (checkNextPosition(position)) {
                reservationHolder.vDivider.setVisibility(View.VISIBLE);
                reservationHolder.vBigDivider.setVisibility(View.GONE);
            } else {
                reservationHolder.vDivider.setVisibility(View.GONE);
                reservationHolder.vBigDivider.setVisibility(View.VISIBLE);
            }

            reservationHolder.ivInfo.setTag(userModel);
            reservationHolder.ivInfo.setOnClickListener(mySingleListener);

            reservationHolder.ivChat.setTag(userModel);
            reservationHolder.ivChat.setOnClickListener(mySingleListener);
        }
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    private static class ReservationHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvLastName;
        TextView tvGivenName;
        TextView tvSex;
        ImageView ivInfo;
        ImageView ivChat;
        RelativeLayout timeLayout;
        View vDivider;
        View vBigDivider;
        View vPrev;

        ReservationHolder(View view) {
            super(view);
            tvTime = view.findViewById(R.id.tv_time);
            tvLastName = view.findViewById(R.id.tv_last_name);
            tvGivenName = view.findViewById(R.id.tv_given_name);
            tvSex = view.findViewById(R.id.tv_sex);
            ivInfo = view.findViewById(R.id.iv_info_request);
            ivChat = view.findViewById(R.id.iv_chat_request);
            timeLayout = view.findViewById(R.id.time_layout);
            vDivider = view.findViewById(R.id.v_divider);
            vBigDivider = view.findViewById(R.id.v_big_divider);
            vPrev = view.findViewById(R.id.v_prev);
        }
    }

    private boolean checkPrevPosition(int position) {
        if (position - 1 >= 0) {
            ReservationModel currentReservation = userModels.get(position).getReservations().get(0);
            ReservationModel prevReservation = userModels.get(position - 1).getReservations().get(0);
            String positionTime = currentReservation.getReservationHour() + ":" + currentReservation.getReservationMin();
            String prevPositionTime = prevReservation.getReservationHour() + ":" + prevReservation.getReservationMin();
            if (positionTime.equals(prevPositionTime)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkNextPosition(int position) {
        if (position + 1 < userModels.size()) {
            ReservationModel currentReservation = userModels.get(position).getReservations().get(0);
            ReservationModel nextReservation = userModels.get(position + 1).getReservations().get(0);
            String positionTime = currentReservation.getReservationHour() + ":" + currentReservation.getReservationMin();
            String nextPositionTime = nextReservation.getReservationHour() + ":" + nextReservation.getReservationMin();
            if (positionTime.equals(nextPositionTime)) {
                return true;
            }
        }
        return false;
    }

    public void setList(ArrayList<UserModel> userModels) {
        this.userModels.clear();
        this.userModels.addAll(userModels);
        notifyDataSetChanged();
    }

    interface OnReservationAdapterListener {
        void onInfoItemClicked(UserModel userModel);

        void onChatItemClicked(UserModel userModel);
    }

    public void setListener(OnReservationAdapterListener listener) {
        this.listener = listener;
    }

    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            UserModel userModel = (UserModel) v.getTag();
            if (listener != null && userModel != null) {
                switch (v.getId()) {
                    case R.id.iv_info_request:
                        listener.onInfoItemClicked(userModel);
                        break;
                    case R.id.iv_chat_request:
                        listener.onChatItemClicked(userModel);
                        break;
                }
            }
        }
    };
}
