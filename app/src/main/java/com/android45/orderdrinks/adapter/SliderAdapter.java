package com.android45.orderdrinks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.models.SliderModel;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderModel> list = new ArrayList<>();

    public SliderAdapter(Context context, List<SliderModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.image_slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        final SliderModel sliderItem = list.get(position);
        Picasso.get().load(sliderItem.getImgUrl()).into(viewHolder.img);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class SliderAdapterVH extends ViewHolder {

        ImageView img;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.idIVimage);
        }
    }
}
