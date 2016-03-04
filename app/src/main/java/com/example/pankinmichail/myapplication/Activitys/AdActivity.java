package com.example.pankinmichail.myapplication.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pankinmichail.myapplication.IntentCreator;
import com.example.pankinmichail.myapplication.Models.AdAlert;
import com.example.pankinmichail.myapplication.R;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mikhail Pankin
 * mikhail.p-n@yandex.ru
 * on 03.03.16.
 */
public class AdActivity extends AppCompatActivity {
	@Bind(R.id.title)
	TextView title;

	@Bind(R.id.description)
	TextView description;

	@Bind(R.id.okButton)
	Button okButton;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_activity);
		ButterKnife.bind(this);
		showAlert(createAlertObjectFromIntent(getIntent()));


	}

	private void showAlert(final AdAlert alert) {
		title.setText(alert.getTitle());
		description.setText(alert.getDescription());
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(IntentCreator.create(alert.getAction()));
			}
		});

	}

	private AdAlert createAlertObjectFromIntent(Intent intent) {
		return Parcels.unwrap(intent.getParcelableExtra("alert"));
	}
}
