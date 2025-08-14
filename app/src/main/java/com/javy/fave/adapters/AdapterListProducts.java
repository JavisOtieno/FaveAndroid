package com.javy.fave.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.javy.fave.R;
import com.javy.fave.models.Product;

import java.util.ArrayList;
import java.util.List;

public class AdapterListProducts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Product obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListProducts(Context context, List<Product> items) {
        this.items = items;
        ctx = context;
    }
    public AdapterListProducts(List<Product> items) {
        this.items = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView startlocation;
        public TextView date;
        public TextView value;
        public TextView endlocation;
        public View lyt_parent;
        public TextView contactName;
        public TextView amount;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            startlocation = (TextView) v.findViewById(R.id.startlocation);
            endlocation = (TextView) v.findViewById(R.id.endlocation);
            date = (TextView) v.findViewById(R.id.date);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            amount = (TextView) v.findViewById(R.id.amount);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Product p = items.get(position);
            view.startlocation.setText(p.name);
            view.date.setText(p.commission);
            view.endlocation.setText(p.category);
            view.amount.setText(p.price);
//            view.date.setText(p.phone);

            //Tools.displayImageRound(ctx, view.image, p.image);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
