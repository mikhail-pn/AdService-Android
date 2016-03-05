package com.example.pankinmichail.myapplication.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pankinmichail.myapplication.Fragments.AdServiceControllerFragment;
import com.example.pankinmichail.myapplication.R;
import com.example.pankinmichail.myapplication.Service.AdServiceManager;

public class MainActivity extends AppCompatActivity {
    private AdServiceManager adServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adServiceManager = new AdServiceManager(this);

        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, new AdServiceControllerFragment()).commit();
    }

    public AdServiceManager getAdServiceManager() {
        return adServiceManager;
    }
}
