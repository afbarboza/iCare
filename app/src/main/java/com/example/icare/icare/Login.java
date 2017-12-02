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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Login extends AppCompatActivity {

    /**
     * gotoGoogleAuthentication - transfers control to the
     *  GoogleAuthentication acitivity
     */
    private void gotoGoogleAuthentication() {
        Intent i = new Intent(Login.this, GoogleAuthentication.class);
        startActivity(i);
    }

    public void onRadioButtonClickedCaregiver(View view) {
        gotoGoogleAuthentication();
    }

    public void onRadioButtonClickedOld(View view) {
        // Check which radio button was clicked
        String str2 = getString(R.string.activity_login_wait);

        PersonalToast.toastMessage(getApplicationContext(), str2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
