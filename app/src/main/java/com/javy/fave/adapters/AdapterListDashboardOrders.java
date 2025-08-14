package com.javy.fave.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.javy.fave.R;
import com.javy.fave.models.Order;

import java.util.ArrayList;
import java.util.List;

public class AdapterListDashboardOrders extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Order obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListDashboardOrders(Context context, List<Order> items) {
        this.items = items;
        ctx = context;
    }
    public AdapterListDashboardOrders(List<Order> items) {
        this.items = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView actualTarget;
        public TextView amount;
        public TextView date;
        public View lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            actualTarget = (TextView) v.findViewById(R.id.actualTarget);
            amount = (TextView) v.findViewById(R.id.amount);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            date = (TextView) v.findViewById(R.id.date);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_order, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Order p = items.get(position);
            view.name.setText(p.customerName);
            view.date.setText(p.date);
            view.amount.setText(p.value);
            view.actualTarget.setText(p.orderDetails);
//            view.status.setText(p.target_type);
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

