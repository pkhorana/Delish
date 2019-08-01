package com.example.delish.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.delish.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;

    // data is passed into the constructor
    public CartAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.buy_item_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String food = mData.get(position);
        holder.itemName.setText(food);
        holder.itemPrice.setText("$" + "0.00");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public int getItemCount(int id) {
        if(mData!=null){
            return mData.size();
        }
        return 0;
    }

    public String getItem(int id) {
        return mData.get(id);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.countText);

            itemView.setOnClickListener((view) -> {
                int position = getAdapterPosition();

                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(mData.get(position));
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClicked(String food);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}
}