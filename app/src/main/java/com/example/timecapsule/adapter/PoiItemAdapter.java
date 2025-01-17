package com.example.timecapsule.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.timecapsule.R;

import java.util.List;


//REFERENCE: BaiDu SDK website
public class PoiItemAdapter extends RecyclerView.Adapter {
    private List<PoiInfo> mPoiInfos; // poi information
    private int mCurSelectPos = 0; // currently selected item pos
    private EditText location_t;

    private MyOnItemClickListener mOnItemClickListener;

    public PoiItemAdapter(List<PoiInfo> poiInfoList,EditText location ) {
        mPoiInfos = poiInfoList;
        location_t = location;
    }

    public void updateData(List<PoiInfo> poiInfos, EditText location) {
        mPoiInfos = poiInfos;
        notifyDataSetChanged();
        mCurSelectPos = 0;
        location_t = location;
    }

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.baidumap_rgc_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (position < 0) {
            return;
        }

        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (null == myViewHolder) {
            return;
        }

        modifyItemSelectState(myViewHolder, position);

        myViewHolder.mPoiItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mOnItemClickListener) {
                    return;
                }


                int pos = myViewHolder.getAdapterPosition();
                if (pos == mCurSelectPos) {
                    return;
                }

                mCurSelectPos = pos;
                location_t.setText(mPoiInfos.get(pos).getName());

                notifyDataSetChanged();

                if (null != mPoiInfos && pos < mPoiInfos.size()) {
                    PoiInfo poiInfo = mPoiInfos.get(pos);
                    mOnItemClickListener.onItemClick(pos, poiInfo);
                }
            }
        });

        bindViewHolder(myViewHolder, position);

    }

    /**
     * Due to the ViewHodler reuse logic of Recyclerview, after sliding, the next bound item reuses ViewHolder and reuses the selected state of the previous item
     * Here you need to make corrections to the selected state of the item
     *
     * @param myViewHolder
     * @param position
     */
    private void modifyItemSelectState(MyViewHolder myViewHolder, int position) {
        if (position != mCurSelectPos) {
            myViewHolder.mImgCheck.setVisibility(View.GONE);
        } else if (position == mCurSelectPos) {
            myViewHolder.mImgCheck.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Bind ViewHolder information
     *
     * @param viewHolder
     * @param position
     */
    private void bindViewHolder(MyViewHolder viewHolder, int position) {
        if (null == mPoiInfos || position >= mPoiInfos.size()) {
            return;
        }

        if (0 == position) {
            PoiInfo poiInfo = mPoiInfos.get(0);
            bindAddressInfo(viewHolder, poiInfo);
        } else {
            bindPoiInfo(viewHolder, position);
        }
    }

    private void bindPoiInfo(MyViewHolder viewHolder, int position) {
        PoiInfo poiInfo = mPoiInfos.get(position);
        if (null == poiInfo) {
            return;
        }

        String name = poiInfo.getName();

        viewHolder.mPoiNameText.setText(name);
        viewHolder.mPoiAddressText.setText(poiInfo.getAddress());
        String poiAddress = poiInfo.getAddress();
        if (!TextUtils.isEmpty(poiAddress)) {
            if (View.INVISIBLE == viewHolder.mPoiAddressText.getVisibility()
                    || View.GONE == viewHolder.mPoiAddressText.getVisibility()) {
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(viewHolder.mPoiNameText.getLayoutParams());
                layoutParams.setMargins(0, 40, 0, 0);
                viewHolder.mPoiNameText.setLayoutParams(layoutParams);
                viewHolder.mPoiAddressText.setVisibility(View.VISIBLE);
                viewHolder.mPoiAddressText.setText(poiAddress);
            }
            viewHolder.mPoiAddressText.setText(poiAddress);
        } else {
            hideAddressInfo(viewHolder);
        }
    }

    private void bindAddressInfo(MyViewHolder viewHolder, PoiInfo poiInfo) {
        String name = "[" + poiInfo.getAddress() + "]";
        viewHolder.mPoiNameText.setText(name);
        hideAddressInfo(viewHolder);
    }

    private void hideAddressInfo(MyViewHolder viewHolder) {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(viewHolder.mPoiNameText.getLayoutParams());
        layoutParams.setMargins(0, 40, 0, 40);
        viewHolder.mPoiNameText.setLayoutParams(layoutParams);
        viewHolder.mPoiAddressText.setVisibility(View.GONE);
    }

    /**
     * Get the number of items
     */
    @Override
    public int getItemCount() {
        if (null != mPoiInfos) {
            return mPoiInfos.size();
        }

        return 0;
    }

    /**
     * Define the item click callback interface
     */
    public interface MyOnItemClickListener {
        void onItemClick(int position, PoiInfo poiInfo);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mPoiItemView;
        public TextView mPoiNameText;
        public TextView mPoiAddressText;
        public ImageView mImgCheck;

        public MyViewHolder(View view) {
            super(view);
            mPoiItemView = view;
            mPoiNameText = view.findViewById(R.id.poiResultName);
            mPoiAddressText = view.findViewById(R.id.poiResultDetail);
            mImgCheck = view.findViewById(R.id.imgCheck);
        }
    }
}
