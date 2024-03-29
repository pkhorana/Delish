//package com.example.delish.View;
//
//
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.delish.R;
//
//import com.gatech.astroworld.spacetrader.model.Game;
//import com.example.delish.Models.MarketGood;
//import com.gatech.astroworld.spacetrader.model.Store;
//import com.example.delish.View.Buy_ItemFragment.OnListFragmentInteractionListener;
//import com.gatech.astroworld.spacetrader.views.market_Activity;
//
//
//import java.util.List;
//
//
//
//public class Buy_Item_RecyclerAdapter extends
//        RecyclerView.Adapter<Buy_Item_RecyclerAdapter.ViewHolder> {
//    private final List<MarketGood> mValues;
//    private final OnListFragmentInteractionListener mListener;
//
//    public static int mBuyTotal;
//    public static int mCountTot;
//
//
//    public Buy_Item_RecyclerAdapter(List<MarketGood> items,
//                                    OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//        store = Game.getInstance().getPlayer().getCurrentPlanet().getStore();
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.fragment_market_buy_item, parent, false);
//
//        return new ViewHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mContentView.setText(holder.mItem.getName());
//        holder.itemIcon.setImageResource(holder.mItem.getResID());
//        holder.itemIcon.setBackgroundResource(holder.mItem.getResID());
//        holder.mPriceView.setText(String.valueOf(holder.mItem.getPrice()));
//        String name = String.format("%11s", holder.mItem.getName());
//        holder.mContentView.setText(name);
//        String price = String.format("%11s", "Price: " +
//                Integer.toString(holder.mItem.getPrice()));
//        String qty = String.format("%11s", "Qty: " +
//                Integer.toString(holder.mItem.getQuantity()));
//        holder.mPriceView.setText(price + "\n" + qty);
//
//    }
//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public View mView;
//        public TextView mIdView;
//        public TextView mContentView;
//        public TextView mPriceView;
//        public MarketGood mItem;
//        public ImageView itemIcon;
//
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mIdView = view.findViewById(R.id.item_number);
//            mContentView = view.findViewById(R.id.itemName);
//            mPriceView = view.findViewById(R.id.itemPrice);
//            itemIcon = view.findViewById(R.id.imageView5);
//
//            final TextView itemCountText = view.findViewById(R.id.countText);
//            Button plusButton = mView.findViewById(R.id.plusButton);
//            Button minusButton = mView.findViewById(R.id.minusButton);
//            final Toast error = Toast.makeText(view.getContext(), "You cannot" +
//                    " buy more than the available amount!", Toast.LENGTH_LONG);
//            final Toast error2 = Toast.makeText(view.getContext(), "You cannot" +
//                    " afford to buy this!", Toast.LENGTH_LONG);
//
//
//            plusButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int i = mBuyTotal + mItem.getPrice();
//                    if ((mItem.getCount() + 1) > mItem.getQuantity()) {
//                        error.show();
//                    }
//                    else if (Game.getInstance().getPlayer().getCredits() < i) {
//                        error2.show();
//                    }
//                    else {
//                        store.incrementCountBuy(mItem);
//                        itemCountText.setText(String.valueOf(mItem.getCount()));
//                        mListener.onListFragmentInteraction(mItem);
//                        mBuyTotal = i;
//                        mCountTot++;
//                        updateTotal();
//                    }
//                }
//            });
//
//            minusButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    store.decrementCountBuy(mItem);
//                    itemCountText.setText(String.valueOf(mItem.getCount()));
//                    int i = mBuyTotal - mItem.getPrice();
//                    if (i >= 0) {
//                        mBuyTotal = i;
//                        mCountTot--;
//                        updateTotal();
//                    }
//                }
//            });
//        }
//
//        public int getMTotal() {
//            return mBuyTotal;
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
//
//        public void updateTotal() {
//            market_Activity.mShowTotal.setText(String.valueOf(mBuyTotal));
//        }
//
//
//        public void updateQuant() {
//            for (int i = 0; i < mValues.size(); i++) {
//                String price = String.format("%11s", "Price: " + Integer.
//                        toString(mValues.get(i).getPrice()));
//                String qty = String.format("%11s", "Qty: " + Integer.
//                        toString(mValues.get(i).getQuantity()));
//                mPriceView.setText(price + "\n" + qty);
//            }
//        }
//
//
//    }
//
//}
