/*
 * Copyright (c) 2017.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.icare.icare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GoogleAuthentication extends AppCompatActivity {

    SignInButton btnLogin;

    /* A client for interacting with the Google Sign In API. */
    private GoogleSignInClient mGoogleSignInClient;
    private static  final int RC_SIGN_IN = 999;

    /**
     * warnWrongEmail - alerts user that the inserted email and/or password is not correct
     */
    private void warnWrongEmail() {
        new AlertDialog.Builder(this).setTitle(R.string.acitivity_googleauthentication_str4).
                setMessage(getString(R.string.acitivity_googleauthentication_str2)).show();
    }

    /**
     * updateUI - updates the user interface after the attempt to login is made.
     *  This method warns whether the login has failed or redirects to the next view otherwise.
     *
     * @param account the gmail account object
     */
    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            warnWrongEmail();
        } else {
            PersonalToast.toastMessage(this, getString(R.string.acitivity_googleauthentication_str3));
            Intent i = new Intent(this, CaregiverDashboard.class);
            startActivity(i);
        }
    }

    /**
     * signIn - log into the  user GMail account.
     */
    protected void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * signOut - log out from user GMail account.
     */
    protected void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    /**
     * handleSignInResult: handles the attempt to login into a Google Account
     *
     * @param completedTask: Task of Class that holds the basic account information of the
     *  signed in Google user.
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);

        }
    }

    /**
     * onActivityResult - series of actions to be made after the user made an attempt (valid or not)
     *      to login to his Google Account.
     *
     * @param requestCode: The integer request code originally supplied to startActivityForResult(),
     *                   allowing you to identify who this result came from.
     * @param resultCode: The integer result code returned by the child activity through its setResult().
     * @param data: An Intent, which can return result data to the caller
     *            (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_authentication);

        /* initialize graphical elements here */


        /* Setting up some sign in options */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        /* build a google sign in client with the options specified by gso */
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /* defines behavior for Google Account login button */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
}
