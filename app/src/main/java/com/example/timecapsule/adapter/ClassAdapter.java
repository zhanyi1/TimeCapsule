package com.example.timecapsule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timecapsule.R;
import com.example.timecapsule.db.Classroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The adapter of Recycleview used in popwindow
public class ClassAdapter extends  RecyclerView.Adapter<ClassAdapter.ClassViewHolder>{
    private List<Classroom> classList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private int itemID;
    private HashMap<Integer,Boolean>Maps=new HashMap<Integer,Boolean>();
    private HashMap<Integer,Boolean>AllMaps=new HashMap<Integer,Boolean>();
    public RecyclerViewOnItemClickListener onItemClickListener;



    public ClassAdapter(Context context, List<Classroom> emailList, int itemID){
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.classList = emailList;
        this.itemID = itemID;
        //Use Map to maintain checkbox information
        initMap();
    }


    private void initMap() {
        for (int i = 0; i < classList.size(); i++) {
            Maps.put(i, false);
        }
    }

    public Map<Integer,Boolean> getMap(){
        return Maps;
    }

    public Map<Integer,Boolean>getAllMap(){
        return AllMaps;
    }

    public void setSelectItem(int position) {
        //Invert the current state
        if (Maps.get(position)) {
            Maps.put(position, false);
        } else {
            Maps.put(position, true);
        }
        notifyItemChanged(position);
    }




    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ClassViewHolder viewHolder = new ClassViewHolder(LayoutInflater.from(mContext).inflate(itemID, parent,false),onItemClickListener);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {

        int index = position;
        Classroom classroom = classList.get(index);
        String name = classroom.getName();
        holder.name.setText(name);

        //Clear the listener
        holder.check.setOnCheckedChangeListener(null);
        //Set the selected state
        holder.check.setChecked(Maps.get(index));
        //Set up the selected listener of the CheckBox again, when the selected state of the CheckBox changes, store the changed state in the Map
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Maps.put(index, isChecked);
                //Once the check status changes, the saved check value will also change accordingly
            }
        });

        if(Maps.get(index)==null){
            Maps.put(index,false);
        }
        holder.check.setChecked(Maps.get(index));
        AllMaps.put(index,true);


    }

    public void setItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Interface callback setting click event
    public interface RecyclerViewOnItemClickListener {
        //Click event
        void onItemClickListener(View view, int position);
    }



    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewOnItemClickListener mListener;
        TextView name;
        CheckBox check;

        public ClassViewHolder(@NonNull View itemView, RecyclerViewOnItemClickListener onItemClickListener) {
            super(itemView);
            this.mListener = onItemClickListener;
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
            check = (CheckBox) itemView.findViewById(R.id.is_check);

        }

        //Click event
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClickListener(v,getAdapterPosition());
            }
        }
    }



}
