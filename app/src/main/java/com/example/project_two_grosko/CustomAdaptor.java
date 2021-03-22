package com.example.project_two_grosko;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyViewHolder> {

    Context context;
    ArrayList<String> item_names, item_stocks, item_prices, item_ids;
    InventoryTableHelper inventoryTableHelper;

    CustomAdaptor(
            Context context,
            ArrayList<String> item_ids,
            ArrayList<String> item_names,
            ArrayList<String> item_stocks,
            ArrayList<String> item_prices
    ) {
        this.context = context;
        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_stocks = item_stocks;
        this.item_prices = item_prices;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.inventory_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item_id_txt.setText(String.valueOf(item_ids.get(position)));
        holder.item_name_txt.setText(String.valueOf(item_names.get(position)));
        holder.item_stock_txt.setText(String.valueOf(item_stocks.get(position)));

        holder.increment.setOnClickListener(v -> {
            inventoryTableHelper.incrementStock(holder.item_id_txt.getText().toString());
            updateData();
            notifyDataSetChanged();
        });
        holder.decrement.setOnClickListener(v -> {
            inventoryTableHelper.decrementStock(holder.item_id_txt.getText().toString());
            updateData();
            notifyDataSetChanged();
        });
        holder.delete.setOnClickListener(v -> {
            inventoryTableHelper.removeRow(holder.item_id_txt.getText().toString());
            updateData();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return item_ids.size();
    }

    private void updateData() {
        item_ids = new ArrayList<>();
        item_names = new ArrayList<>();
        item_stocks = new ArrayList<>();
        item_prices = new ArrayList<>();
        Cursor data = inventoryTableHelper.getData();
        while(data.moveToNext()) {
            item_ids.add(data.getString(2));
            item_names.add(data.getString(3));
            item_stocks.add(data.getString(4));
            item_prices.add(data.getString(5));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_id_txt, item_name_txt, item_stock_txt;
        Button increment, decrement, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            inventoryTableHelper = new InventoryTableHelper(itemView.getContext());
            item_id_txt = itemView.findViewById(R.id.item_id);
            item_name_txt = itemView.findViewById(R.id.item_name);
            item_stock_txt = itemView.findViewById(R.id.item_stock);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
