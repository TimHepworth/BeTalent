package com.betalent.betalent;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.toolbox.Volley;
import com.betalent.betalent.Model.BeTalentDB;
import com.betalent.betalent.Model.User;

import java.lang.ref.WeakReference;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private BeTalentDB betalentDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        betalentDb = BeTalentDB.getInstance(SplashActivity.this);

        BeTalentProperties.getInstance().mRequestQueue = Volley.newRequestQueue(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Running Runnable ...");
                User user = betalentDb.getUserDao().getUser();

                if (user == null) {
                    System.out.println("No user found");

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("DataAvailable", false);
                    startActivity(intent);

                } else {
                    System.out.println(user.toString());

                    Intent intent;

                    if (user.getLoggedIn()) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("DataAvailable", true);
                        intent.putExtra("EmailAddress", user.getEmailAddress());
                        intent.putExtra("Password", user.getPassword());
                    }

                    startActivity(intent);
                }

            }
        }) .start();

        this.finish();
    }
}
