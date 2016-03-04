package com.example.pankinmichail.myapplication.Activitys;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pankinmichail.myapplication.Models.AdAction;
import com.example.pankinmichail.myapplication.Models.AdNotification;
import com.example.pankinmichail.myapplication.R;
import com.example.pankinmichail.myapplication.Service.ServiceManager;
import com.example.pankinmichail.myapplication.SharedPrefsManager;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.adActionType)
    Spinner adActionTypeSelector;

    @OnClick(R.id.setTime)
    public void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Toast.makeText(MainActivity.this, selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @OnClick(R.id.addNotification)
    public void addNotification() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new ServiceManager(this);
        adActionTypeSelector.setAdapter(ArrayAdapter.createFromResource(this, R.array.adActionTypes, android.R.layout.simple_spinner_dropdown_item));

        final AdNotification notification = new AdNotification("Tile", "Description", R.mipmap.ic_launcher, new AdAction(AdAction.ActionType.URL, "http://ya.ru"), 1457038651000l);
        SharedPrefsManager.getInstance().saveNotificationsQueue(new ArrayList<AdNotification>() {{
            add(notification);
        }});


    }
}
