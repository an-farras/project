package com.ta.belajarsdap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "PasswordlessSignIn";
    private static final String KEY_PENDING_EMAIL = "key_pending_email";
    private static final String BUNDLE_EMAIL = "BUNDLE_EMAIL";
    private static final String BUNDLE_LINK = "BUNDLE_LINK";
    private static final String PREF_NAME = "emailPref";

    EditText inputEmail;
    ProgressBar progressBar;
    Button btnSendVerifLink;
    SharedPreferences sharedPref;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String email;

    private String mPendingEmail;
    private String mEmailLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);
        inputEmail = findViewById(R.id.inputEmail);
        btnSendVerifLink = findViewById(R.id.btnSendVerifLink);
        progressBar = findViewById(R.id.progressGetOTP);
        sharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if (savedInstanceState != null) {
            mPendingEmail = savedInstanceState.getString(KEY_PENDING_EMAIL, null);
            inputEmail.setText(mPendingEmail);
        }

        btnSendVerifLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Masukkan Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                email = inputEmail.getText().toString().trim();
                btnSendVerifLink.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                sharedPref.edit().putString(BUNDLE_EMAIL, email).apply();
                sendSignInLink(email);
            }
        });
        checkIntent(getIntent());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onNewIntent (Intent newIntent) {
        super.onNewIntent(newIntent);
        checkIntent(newIntent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_PENDING_EMAIL, mPendingEmail);
    }

    /**
     * Check to see if the Intent has an email link, and if so set up the UI accordingly.
     * This can be called from either onCreate or onNewIntent, depending on how the Activity
     * was launched.
     */
    private void checkIntent(@Nullable Intent intent) {
        if (intentHasEmailLink(intent)) {
            mEmailLink = intent.getData().toString();
            email = sharedPref.getString(BUNDLE_EMAIL, "");
            showSnackbar("Link received");
            signInWithEmailLink(email, mEmailLink);

        } else {
            showSnackbar("Send link to email to start");
        }
    }

    /**
     * Determine if the given Intent contains an email sign-in link.
     */
    private boolean intentHasEmailLink(@Nullable Intent intent) {
        if (intent != null && intent.getData() != null) {
            String intentData = intent.getData().toString();
            if (mAuth.isSignInWithEmailLink(intentData)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Send an email sign-in link to the specified email.
     */
    private void sendSignInLink(String email) {
        ActionCodeSettings settings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName(
                        getPackageName(),
                        false, /* install if not available? */
                        null   /* minimum app version */)
                .setHandleCodeInApp(true)
                .setUrl("https://belajarsdap.page.link")
                .build();

        mAuth.sendSignInLinkToEmail(email, settings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Log.d(TAG, "Link sent");
                            showSnackbar("Sign-in link sent!");

                            mPendingEmail = email;

                        } else {
                            Exception e = task.getException();
                            Log.w(TAG, "Could not send link", e);
                            showSnackbar("Failed to send link.");

                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                showSnackbar("Invalid email address.");
                            }
                        }
                        btnSendVerifLink.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * Sign in using an email address and a link, the link is passed to the Activity
     * from the dynamic link contained in the email.
     */
    private void signInWithEmailLink(String e_mail, String link) {
        Log.d(TAG, "signInWithLink:" + link + "\nwithEmail:" + e_mail);

        mAuth.signInWithEmailLink(e_mail, link)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mPendingEmail = null;

                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmailLink:success");
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("email", e_mail);
                            hashMap.put("name", "");
                            hashMap.put("lessonCompleted", 0);
                            //databaseReference.child("users").child(e_mail).setValue("hashMap");
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.w(TAG, "signInWithEmailLink:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthActionCodeException) {
                                showSnackbar("Invalid or expired sign-in link.");
                            }
                        }
                    }
                });
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}