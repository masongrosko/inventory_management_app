package com.example.project_two_grosko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public int getItemCount() {
        return item_ids.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_id_txt, item_name_txt, item_stock_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            inventoryTableHelper = new InventoryTableHelper(itemView.getContext());
            item_id_txt = itemView.findViewById(R.id.item_id);
            item_name_txt = itemView.findViewById(R.id.item_name);
            item_stock_txt = itemView.findViewById(R.id.item_stock);

            itemView.findViewById(R.id.increment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inventoryTableHelper.incrementStock(item_id_txt.getText().toString());;
                    notifyDataSetChanged();
                }
            });

            itemView.findViewById(R.id.decrement).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inventoryTableHelper.decrementStock(item_id_txt.getText().toString());
                    notifyDataSetChanged();
                }
            });
            itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inventoryTableHelper.removeRow(item_id_txt.getText().toString());
                    notifyDataSetChanged();
                }
            });

        }


    }

}
