package com.jebstern.petrolbook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jebstern.petrolbook.R;
import com.jebstern.petrolbook.rest.RefuelResponse;

import java.util.Collections;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<RefuelResponse> refuelList = Collections.emptyList();

    public MyRecyclerViewAdapter(List<RefuelResponse> moviesList) {
        this.refuelList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.address.setText(refuelList.get(position).getAddress());
        holder.date.setText(refuelList.get(position).getTime());
        holder.amount.setText(refuelList.get(position).getAmount() + "L");
        holder.price.setText(refuelList.get(position).getPrice() + "€");
        holder.type.setText(refuelList.get(position).getType());
    }

    public RefuelResponse getItem(int position) {
        return refuelList.get(position);
    }


    @Override
    public int getItemCount() {
        return refuelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView address;
        private TextView date;
        private TextView amount;
        private TextView price;
        private TextView type;

        MyViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.tv_address);
            date = (TextView) view.findViewById(R.id.tv_date);
            amount = (TextView) view.findViewById(R.id.tv_amount);
            price = (TextView) view.findViewById(R.id.tv_price);
            type = (TextView) view.findViewById(R.id.tv_type);
        }

    }


}