package com.example.project_two_grosko;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    // Initialize variables
    LoginTableHelper loginTableHelper;
    NotificationTableHelper notificationTableHelper;
    private EditText username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab values from content view
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.userTextEdit);
        password = findViewById(R.id.passTextEdit);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        // Initialize database
        loginTableHelper = new LoginTableHelper(this);
        notificationTableHelper = new NotificationTableHelper(this);

        // Check to see if buttons get clicked
        loginButton.setOnClickListener(v -> {
            int loginSuccess;
            String providedUser = username.getText().toString();
            String providedPass = password.getText().toString();

            if (providedUser.length() != 0) {
                if (providedPass.length() != 0) {
                    loginSuccess = LoginAttempt(providedUser, providedPass);
                    if (loginSuccess == 1) {
                        int notificationPermission = notificationTableHelper.GetNotificationCol(providedUser);
                        if (notificationPermission == 0) {
                            Intent notificationIntent = new Intent(
                                    LoginActivity.this,
                                    RequestNotificationPermissionActivity.class
                            );
                            notificationIntent.putExtra("username", providedUser);
                            startActivity(notificationIntent);
                        } else {
                            Intent inventoryManagementIntent = new Intent(
                                    LoginActivity.this,
                                    InventoryManagementActivity.class
                            );
                            inventoryManagementIntent.putExtra("username", providedUser);
                            startActivity(inventoryManagementIntent);
                        }
                    }
                } else {
                    toastMessage("Password must not be empty.");
                }
            } else {
                toastMessage("Username must not be empty.");
            }
        });
        signUpButton.setOnClickListener(v -> {
            String newUser = username.getText().toString();
            String newPass = password.getText().toString();

            if (newUser.length() != 0) {
                if (newPass.length() != 0) {
                    CreateUser(newUser, newPass);
                } else {
                    toastMessage("Password must not be empty.");
                }
            } else {
                toastMessage("Username must not be empty.");
            }
        });
    }

    // Create a new user in the loginDatabase
    public void CreateUser(String newUser, String newPass) {
        long insertData = loginTableHelper.CreateUser(newUser, newPass);
        if (insertData == -1) {
            toastMessage("Username already taken. Try again!");
        } else {
            toastMessage("User account created");
        }
    }

    // Attempt a login
    public int LoginAttempt(String providedUser, String providedPass) {
        int loginSuccess = loginTableHelper.UserLogin(providedUser, providedPass);
        if (loginSuccess == 0) {
            toastMessage("Incorrect Username/Password");
        } else {
            toastMessage("Login success!");
        }
        return loginSuccess;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}