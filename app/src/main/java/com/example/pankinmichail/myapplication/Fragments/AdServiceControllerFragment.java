package com.example.pankinmichail.myapplication.Fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pankinmichail.myapplication.Activitys.MainActivity;
import com.example.pankinmichail.myapplication.Models.AdAction;
import com.example.pankinmichail.myapplication.Models.AdAction.ActionType;
import com.example.pankinmichail.myapplication.Models.AdAlert;
import com.example.pankinmichail.myapplication.Models.AdNotification;
import com.example.pankinmichail.myapplication.Models.AdShortcut;
import com.example.pankinmichail.myapplication.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 05.03.16.
 */
public class AdServiceControllerFragment extends Fragment {
    @Bind(R.id.adActionType)
    Spinner adActionTypeSelector;
    @Bind(R.id.actionData)
    EditText actionData;
    @Bind(R.id.showTime)
    TextView showTime;
    @Bind(R.id.alertTitle)
    EditText alertTitle;
    @Bind(R.id.alertDescription)
    EditText alertDescription;
    @Bind(R.id.notificationTitle)
    EditText notificationTitle;
    @Bind(R.id.notificationDescription)
    EditText notificationDescription;
    @Bind(R.id.shortcutName)
    EditText shortcutName;

    private long selectedShowTime = 0;

    @OnClick(R.id.setTime)
    public void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        final int currentHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                showTime.setText(getActivity().getString(R.string.show_time) + selectedHour + ":" + selectedMinute);
                selectedShowTime = genDateLongFromHourAndMin(selectedHour, selectedMinute);
            }
        }, currentHour, currentMinute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @OnClick(R.id.addAlert)
    public void addAlert() {
        String title = alertTitle.getText().toString();
        String description = alertDescription.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || selectedShowTime == 0) {
            Toast.makeText(getActivity(), R.string.fill_req_fields, Toast.LENGTH_SHORT).show();
        }

        final AdAlert alert = new AdAlert(title, description, getSelectedAction(), selectedShowTime);
        ((MainActivity) getActivity()).getAdServiceManager().addAlert(alert);
    }

    @OnClick(R.id.addNotification)
    public void addNotification() {
        String title = notificationTitle.getText().toString();
        String description = notificationDescription.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || selectedShowTime == 0) {
            Toast.makeText(getActivity(), R.string.fill_req_fields, Toast.LENGTH_SHORT).show();
        }

        final AdNotification notification = new AdNotification(title, description, R.mipmap.ic_launcher, getSelectedAction(), selectedShowTime);
        ((MainActivity) getActivity()).getAdServiceManager().addNotification(notification);
    }

    @OnClick(R.id.addShortcut)
    public void addShortcut() {
        String name = shortcutName.getText().toString();

        if (TextUtils.isEmpty(name) || selectedShowTime == 0) {
            Toast.makeText(getActivity(), R.string.fill_req_fields, Toast.LENGTH_SHORT).show();
        }

        final AdShortcut shortcut = new AdShortcut(name, R.mipmap.ic_launcher, getSelectedAction(), selectedShowTime);
        ((MainActivity) getActivity()).getAdServiceManager().addShortcut(shortcut);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ad_service_controller, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        setupViews();
    }

    private void setupViews() {
        adActionTypeSelector.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.adActionTypes, android.R.layout.simple_spinner_dropdown_item));

        adActionTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    actionData.setText("http://");
                } else {
                    actionData.setText("com.vkontakte.android");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private long genDateLongFromHourAndMin(int hour, int minute) {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private AdAction getSelectedAction() {
        ActionType type = null;
        switch (adActionTypeSelector.getSelectedItemPosition()) {
            case 0:
                type = ActionType.App;
                break;

            case 1:
                type = ActionType.Store;
                break;

            case 2:
                type = ActionType.URL;
                break;
        }

        String data = actionData.getText().toString();

        return new AdAction(type, data);
    }
}
