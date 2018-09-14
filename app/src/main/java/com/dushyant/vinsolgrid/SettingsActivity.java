package com.dushyant.vinsolgrid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener {

    private Switch startAnimation, flipAnimation, itemAnimation;
    private SeekBar startSeekBar, flipSeekBar, widthSeekBar, spacingSeekBar;
    private TextView startTimeText, flipTimeText, widthText, spacingText;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sharedPreferences = getSharedPreferences(StaticData.PREF_NAME, MODE_PRIVATE);
        setResult(RESULT_OK);

        startAnimation = findViewById(R.id.enable_start_animation);
        flipAnimation = findViewById(R.id.enable_flip_animation);
        itemAnimation = findViewById(R.id.enable_items_animation);
        startSeekBar = findViewById(R.id.start_anim_seek_bar);
        flipSeekBar = findViewById(R.id.flip_anim_seek_bar);
        widthSeekBar = findViewById(R.id.column_width_seek_bar);
        spacingSeekBar = findViewById(R.id.spacing_seek_bar);
        startTimeText = findViewById(R.id.start_anim_text);
        flipTimeText = findViewById(R.id.flip_anim_text);
        widthText = findViewById(R.id.column_width_text);
        spacingText = findViewById(R.id.spacing_text);


        startSeekBar.setMax(1900);
        flipSeekBar.setMax(1900);
        widthSeekBar.setMax(152);
        spacingSeekBar.setMax(20);

        startAnimation.setOnCheckedChangeListener(this);
        flipAnimation.setOnCheckedChangeListener(this);
        itemAnimation.setOnCheckedChangeListener(this);

        startSeekBar.setOnSeekBarChangeListener(this);
        flipSeekBar.setOnSeekBarChangeListener(this);
        widthSeekBar.setOnSeekBarChangeListener(this);
        spacingSeekBar.setOnSeekBarChangeListener(this);

        //DefaultValues
        int startTime = sharedPreferences.getInt(StaticData.START_ANIM_TIME, StaticData.DEFAULT_START_DURATION);
        int flipTime = sharedPreferences.getInt(StaticData.FLIP_ANIM_TIME, StaticData.DEFAULT_FLIP_DURATION);
        int columnWidth = sharedPreferences.getInt(StaticData.COLUMN_WIDTH, StaticData.DEFAULT_WIDTH);
        int spacing = sharedPreferences.getInt(StaticData.GRID_SPACING, StaticData.DEFAULT_SPACING);

        if (columnWidth < 48)
            columnWidth = 48;

        startSeekBar.setProgress(startTime);
        flipSeekBar.setProgress(flipTime);
        widthSeekBar.setProgress(columnWidth - 48);
        spacingSeekBar.setProgress(spacing);

        startAnimation.setChecked(sharedPreferences.getBoolean(StaticData.ENABLE_START_ANIMATION, true));
        flipAnimation.setChecked(sharedPreferences.getBoolean(StaticData.ENABLE_FLIP_ANIMATION, true));
        itemAnimation.setChecked(sharedPreferences.getBoolean(StaticData.ENABLE_ITEMS_ANIMATION, true));

        startTimeText.setText(String.format("%s ms", String.valueOf(startTime)));
        flipTimeText.setText(String.format("%s ms", String.valueOf(flipTime)));
        widthText.setText(String.format("%s dp", String.valueOf(columnWidth)));
        spacingText.setText(String.format("%s dp", String.valueOf(spacing)));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.enable_start_animation:
                savePreferenceValue(StaticData.ENABLE_START_ANIMATION, isChecked);
                break;
            case R.id.enable_flip_animation:
                savePreferenceValue(StaticData.ENABLE_FLIP_ANIMATION, isChecked);
                break;
            case R.id.enable_items_animation:
                savePreferenceValue(StaticData.ENABLE_ITEMS_ANIMATION, isChecked);
                break;
        }
    }

    /*
     * Save Values in Shared Preference
     * */
    private void savePreferenceValue(String keyPref, boolean isChecked) {
        sharedPreferences.edit().putBoolean(keyPref, isChecked).apply();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;

        switch (seekBar.getId()) {
            case R.id.start_anim_seek_bar:
                savePreferenceValueSeekBar(StaticData.START_ANIM_TIME, progress + 100);
                startTimeText.setText(String.format("%s ms", String.valueOf(progress + 100)));
                break;
            case R.id.flip_anim_seek_bar:
                savePreferenceValueSeekBar(StaticData.FLIP_ANIM_TIME, progress + 100);
                flipTimeText.setText(String.format("%s ms", String.valueOf(progress + 100)));
                break;
            case R.id.column_width_seek_bar:
                savePreferenceValueSeekBar(StaticData.COLUMN_WIDTH, progress + 48);
                widthText.setText(String.format("%s dp", String.valueOf(progress + 48)));
                break;
            case R.id.spacing_seek_bar:
                savePreferenceValueSeekBar(StaticData.GRID_SPACING, progress);
                spacingText.setText(String.format("%s dp", String.valueOf(progress)));
                break;
        }
    }

    /*
     * Save SeekBar Values in Shared Preference
     * */
    private void savePreferenceValueSeekBar(String keyPref, int value) {
        sharedPreferences.edit().putInt(keyPref, value).apply();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
