package edu.osu.timekiller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// "Login" class is to help users to log in from firebase
public class Login extends AppCompatActivity {
    private static final int SMS_NOTI_ID = 0;
    private static final String TEST_CHANNEL_ID = "welcome_channel";
    EditText emailField,passwordField;
    Button LoginButton;
    TextView alreadyRegistered, forgetLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    // Email format validate regex
    private static final String regex = "^(.+)@(.+)$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        LoginButton = findViewById(R.id.login_button);
        alreadyRegistered = findViewById(R.id.already_resigtered);
        forgetLink = findViewById(R.id.forget_password);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // trim() is to delete head and tail white space
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Boundary cases when email is empty
                if(TextUtils.isEmpty(email)){
                    emailField.setError("Please enter your email");
                    return;
                }

                // Boundary cases when email is empty
                if(!isValidEmailAddress(email)){
                    emailField.setError("Email format is not correct, Please check again");
                    return;
                }

                // Boundary cases when password is empty
                if(TextUtils.isEmpty(password)){
                    passwordField.setError("Please enter your password");
                    return;
                }

                // Boundary cases when password is not strong
                if(password.length() < 6){
                    passwordField.setError("The length of password should greater than 6");
                    return;
                }

                // Set processbar visable
                progressBar.setVisibility(View.VISIBLE);

                // Authentication with Firebase server
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If login task successes, go into MainActivity
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        // If login task failed
                        }else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        // If need to sign up a account
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        // If forgot the password
        forgetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("resetPasswordTag", "This is on click of reset password");
                final EditText resetText = new EditText(view.getContext());
                final AlertDialog.Builder resetDialog = new AlertDialog.Builder(view.getContext());
                resetDialog.setTitle("Do you want to reset password ?");
                resetDialog.setMessage("Please enter the email of your account");
                resetDialog.setView(resetText);

                // Reset password from AlertDialog, when Clicking Yes
                resetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mailString = resetText.getText().toString();
                        // Checking if email is valid
                        if(!isValidEmailAddress(mailString) || mailString.length() <= 0){
                            Toast.makeText(Login.this, "Error the email is not valid", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Send reset password link to the email
                        fAuth.sendPasswordResetEmail(mailString).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link sent to email you entered", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error happened! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                // Exit password from AlertDialog, when Clicking No
                resetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Empty for closing
                    }
                });
                resetDialog.create().show();

            }
        });

    }

    // Regex checker
    public static boolean isValidEmailAddress(String email) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    @Override
    public void onResume(){
        super.onResume();
        welcome_note();
    }


    // The Module users can enter form notification
    public void welcome_note(){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        createNotificationChannel();

        // Build a notification object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, TEST_CHANNEL_ID)
                .setSmallIcon(R.drawable.done)
                .setContentTitle("Click here to start map")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(SMS_NOTI_ID, builder.build());

    }

    // Set up Test_channel notification channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "welcome_channel";
            String description = "welcome_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( TEST_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}