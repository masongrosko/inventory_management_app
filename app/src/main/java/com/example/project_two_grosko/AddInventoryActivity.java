package com.example.project_two_grosko;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddInventoryActivity extends AppCompatActivity {
    InventoryTableHelper inventoryTableHelper;
    FloatingActionButton acceptButton;
    String username;
    TextView name, id, price, stock;
    String name_string, id_string, price_string, stock_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab username from extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        setContentView(R.layout.activity_add_inventory);

        acceptButton = findViewById(R.id.acceptProductAdditionFloatingButton);

        name = findViewById(R.id.productNameTextEdit);
        id = findViewById(R.id.productIDTextEdit);
        price = findViewById(R.id.productPriceTextEdit);
        stock = findViewById(R.id.productQuantityTextEdit);

        inventoryTableHelper = new InventoryTableHelper(this);

        acceptButton.setOnClickListener(v -> {
            if (
                (id.length() != 0)
                && (name.length() != 0)
                && (stock.length() != 0)
                && (price.length() != 0)
            ) {
                id_string = id.getText().toString();
                name_string = name.getText().toString();
                stock_string = stock.getText().toString();
                price_string = price.getText().toString();
                Log.d(
                        "AddInventoryActivity",
                        "onClick: " + username + id_string + name_string + stock_string + price_string
                );

                inventoryTableHelper.SetRow(
                        username,
                        id_string,
                        name_string,
                        stock_string,
                        price_string
                );
                Intent inventoryManagementIntent = new Intent(
                        AddInventoryActivity.this,
                        InventoryManagementActivity.class
                );
                inventoryManagementIntent.putExtra("username", username);
                startActivity(inventoryManagementIntent);
            }
            else {
                Toast.makeText(this, "All rows must contain data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
