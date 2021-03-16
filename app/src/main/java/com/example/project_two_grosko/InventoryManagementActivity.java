package com.example.project_two_grosko;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class InventoryManagementActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton addButton;
    String username;

    InventoryTableHelper inventoryTableHelper;
    ArrayList<String> item_names, item_stocks, item_prices, item_ids;

    CustomAdaptor customAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Grab username from extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        setContentView(R.layout.activity_inventory_management);
        recyclerView = (RecyclerView) findViewById(R.id.inventoryList);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        InventoryManagementActivity.this,
                        AddInventoryActivity.class
                );
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        inventoryTableHelper = new InventoryTableHelper(this);
        item_ids = new ArrayList<String>();
        item_names = new ArrayList<String>();
        item_stocks = new ArrayList<String>();
        item_prices = new ArrayList<String>();

        displayData();

        customAdaptor = new CustomAdaptor(InventoryManagementActivity.this, item_ids, item_names, item_stocks, item_prices);
        recyclerView.setAdapter(customAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(InventoryManagementActivity.this));
    }

    private void displayData() {
        Cursor data = inventoryTableHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()) {
            item_ids.add(data.getString(2));
            item_names.add(data.getString(3));
            item_stocks.add(data.getString(4));
            item_prices.add(data.getString(5));
        }
    }
}
