package com.ffb.friendsfootbets.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ffb.friendsfootbets.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask;

    private FirebaseAuth mAuth;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // When we come back on the activity after exiting the application, the password has to be
        // written again
        mPasswordView.setText("");

        // We reinitialize the Firebase Authentication instance
        mAuth.signOut();
        mAuthTask = null;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    //TODO bug à corriger : le premier mot de passe qu'on met est toujours incorrect
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showLoadingCircle(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showLoadingCircle(boolean show) {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void onFirebaseSignIn(FirebaseUser user){
        mAuthTask = null;
        showLoadingCircle(false);

        boolean verifiedMail = user != null && user.isEmailVerified();

        //TODO : bug quand on vérifie l'email mais qu'on laisse les champs inchangés sur la page de login : solution oublier le mot de passe quand on revient sur l'activité login ?

        // If the user only signs in
        if (user != null && verifiedMail) {
            Toast.makeText(getApplicationContext(), getString(R.string.authentication_success), Toast.LENGTH_SHORT).show();

            // Explicit intent to go to the home page of the app
            Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            mainActivityIntent.putExtra("userEmail", user.getEmail());
            startActivity(mainActivityIntent);


        } // if the user registers (first connection to the app)
        else if (user != null && !verifiedMail){
            Toast.makeText(getApplicationContext(), getString(R.string.registration_success), Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setMessage("An verification email has been sent to you. " +
                    "Please validate your email before signing in.");
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)  {
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }// the only other case is that the password is wrong
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.authentication_fail), Toast.LENGTH_SHORT).show();

            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String mEmail;
        private String mPassword;

        UserLoginTask(String email, String password) {
            this.mEmail = email;
            this.mPassword = password;
        }

        FirebaseUser user;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    user = mAuth.getCurrentUser();
                                    //As it is a listner, we can't use onPostExecute
                                    onFirebaseSignIn(user);
                                } else {
                                    mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        user = mAuth.getCurrentUser();
                                                        // We send a verification email for the user
                                                        user.sendEmailVerification();
                                                        //As it is a listner, we can't use onPostExecute
                                                        onFirebaseSignIn(user);
                                                    } else {
                                                        // If sign in fails, the password is wrong
                                                        user = null;
                                                        //As it is a listner, we can't use onPostExecute
                                                        onFirebaseSignIn(user);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            } catch (Exception e) {
                System.out.println("case 2");
                System.out.println("Email : "+mEmail);
                System.out.println("Password : "+mPassword);
                System.out.println(e.toString());
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean success) {

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showLoadingCircle(false);
        }
    }
}

