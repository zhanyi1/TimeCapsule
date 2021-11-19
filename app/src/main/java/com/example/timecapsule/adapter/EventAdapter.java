package com.example.timecapsule.adapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.timecapsule.R;
import com.example.timecapsule.db.Event;
import com.example.timecapsule.utils.AlarmBroadcastReceiver;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EventAdapter extends  RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private List<Event> list = new ArrayList<>();
    private List<Event> all_list = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnClickListener editListener;
    private OnClickListener completeListener;
    private OnClickListener deleteListener;


    public EventAdapter(Context context, List<Event> list, List<Event> all_list){
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.all_list = all_list;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item2, parent, false);
        final EventViewHolder holder = new EventViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            //Set the click event for the recyclerView, this is a normal click,
            //if it is a draft, enter the editing mode, if it is a sent mail, enter the reading mode
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();


            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Event event = list.get(position);

        if(!event.isIs_all_day()){
            if(event.getStart()!=0){
                Date start = new Date();
                start.setTime(event.getStart());
                SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
                String start_s = formatter1.format(start);
                holder.start.setText(start_s);
            }

            if(event.getEnd()!=0){
                Date end = new Date();
                end.setTime(event.getEnd());
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
                String end_s = formatter2.format(end);
                holder.end.setText(end_s);
            }
        }else{
            holder.start.setText("00:00");
            holder.end.setText("24:59");
        }



        String title_s = event.getTitle();
        if(!title_s.replace(" ","").equals("")){
            holder.title.setText(title_s.trim());
        }

        String location_s = event.getLocation();
        if(!location_s.replace(" ","").equals("")){
            holder.location.setText(location_s.trim());
        }

        if(!event.isIs_complete()){
            if(event.getType().equals("Boss Capsule")){
                Glide.with(mContext).load(R.drawable.boss).into(holder.image);
            }else{
                Glide.with(mContext).load(R.drawable.skill).into(holder.image);
            }
        }else {
            if(event.getType().equals("Boss Capsule")){
                Glide.with(mContext).load(R.drawable.boss_com1).into(holder.image);
            }else{
                Glide.with(mContext).load(R.drawable.skill_com).into(holder.image);
            }
        }


        int pos = holder.getAdapterPosition();
        //监听侧滑删除事件
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteListener != null){
                    deleteListener.OnClickDelete(v,pos);
                }
            }
        });

        holder.item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editListener!=null){
                    editListener.OnClickEdit(v, pos);
                }

            }
        });

        holder.item_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(completeListener!=null){
                    completeListener.OnClickCom(v, pos);
                }
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.OnItemLongClick(v,position);
                }
                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView start;
        TextView end;
        TextView location;
        Button item_delete;
        Button item_edit;
        Button item_complete;
        CardView layout;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.nTitle);
            start = (TextView) itemView.findViewById(R.id.nstart);
            end = (TextView) itemView.findViewById(R.id.nend);
            location = (TextView) itemView.findViewById(R.id.location);
            item_delete = (Button) itemView.findViewById(R.id.item_delete);
            item_edit = (Button) itemView.findViewById(R.id.item_edit);
            item_complete = (Button) itemView.findViewById(R.id.item_complete);
            layout = (CardView) itemView.findViewById(R.id.layout);

        }

    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View v, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }



    public interface OnClickListener {
        void OnClickEdit(View v, int position);
        void OnClickCom(View v, int position);
        void OnClickDelete(View v, int position);

    }

    public void setEditClickListener(OnClickListener onClickListener) {
        editListener = onClickListener;
    }

    public void setComClickListener(OnClickListener onClickListener) {
        completeListener = onClickListener;
    }

    public void setDeleteClickListener(OnClickListener onClickListener) {
        deleteListener = onClickListener;
    }




}
