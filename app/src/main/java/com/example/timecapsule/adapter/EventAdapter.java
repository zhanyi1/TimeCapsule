package com.example.timecapsule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.timecapsule.R;
import com.example.timecapsule.db.Classroom;
import com.example.timecapsule.db.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EventAdapter extends  RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private List<Event> list = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemLongClickListener mOnItemLongClickListener;

    public EventAdapter(Context context, List<Event> list){
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
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
                Event event = list.get(position);
                

            }
        });

        //Set the listener interface for long press, used to delete operation in mainavtivity
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();

                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.OnItemLongClick(v,position);
                }
                return false;
            }

        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = list.get(position);

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

        String title_s = event.getTitle();
        if(!title_s.replace(" ","").equals("")){
            holder.title.setText(title_s.trim());
        }

        String location_s = event.getLocation();
        if(!location_s.replace(" ","").equals("")){
            holder.location.setText(location_s.trim());
        }


        if(event.getType().equals("Boss Capsule")){
            Glide.with(mContext).load(R.drawable.capsuleboss).into(holder.image);
        }else{
            Glide.with(mContext).load(R.drawable.capsulene).into(holder.image);
        }

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

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.nTitle);
            start = (TextView) itemView.findViewById(R.id.nstart);
            end = (TextView) itemView.findViewById(R.id.nend);
            location = (TextView) itemView.findViewById(R.id.location);

        }

    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View v, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

}
