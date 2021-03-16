package com.example.project_two_grosko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RequestNotificationPermissionActivity extends AppCompatActivity {
    // Initialize variables
    NotificationTableHelper notificationTableHelper;
    Button acceptButton, rejectButton;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab username from extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        // Grab values from content view
        setContentView(R.layout.activity_request_notifaction_permission);
        acceptButton = (Button) findViewById(R.id.notificationAcceptButton);
        rejectButton = (Button) findViewById(R.id.notificationRejectButton);

        // Initialize database
        notificationTableHelper = new NotificationTableHelper(this);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotificationSettings(username, 1);
                Intent inventoryManagementIntent = new Intent(
                        RequestNotificationPermissionActivity.this,
                        InventoryManagementActivity.class
                );
                inventoryManagementIntent.putExtra("username", username);
                startActivity(inventoryManagementIntent);
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotificationSettings(username, -1);
                Intent inventoryManagementIntent = new Intent(
                        RequestNotificationPermissionActivity.this,
                        InventoryManagementActivity.class
                );
                inventoryManagementIntent.putExtra("username", username);
                startActivity(inventoryManagementIntent);
            }
        });
    }

    // Update notification settings for user
    public void updateNotificationSettings(String username, int notificationSetting) {
        notificationTableHelper.SetNotificationCol(username, notificationSetting);
        toastMessage("Updated notification settings.");
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
