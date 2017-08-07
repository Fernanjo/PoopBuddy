package com.fernanjo.poopbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LogActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper myDb;
    EditText editLocation, editComment;
    TextView dateText, timeText, locationText;
    Button btnInsertData, btnDate, btnTime;
    DatePicker datePick;
    TimePicker timePick;
    RelativeLayout RLSelectors;
    LinearLayout DateLayout, TimeLayout;
    Spinner sLocation, sQuality;
    CheckBox cbPain, cbBlood;

    //Test items
    Button btnAdd10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log);
        myDb = new DatabaseHelper(this);

        //Cast UI elements to variables
        dateText = (TextView)findViewById(R.id.textView_date);
        timeText = (TextView)findViewById(R.id.textView_time);
        locationText = (TextView)findViewById(R.id.textView_location);
        editLocation = (EditText)findViewById(R.id.editText_location);
        sQuality = (Spinner)findViewById(R.id.quality_spinner);
        editComment = (EditText)findViewById(R.id.editText_comment);
        btnInsertData = (Button)findViewById(R.id.insertData);
        datePick = (DatePicker)findViewById(R.id.datePicker);
        timePick = (TimePicker)findViewById(R.id.timePicker);
        RLSelectors = (RelativeLayout)findViewById(R.id.layout_selectors);
        DateLayout = (LinearLayout)findViewById(R.id.layout_date);
        TimeLayout = (LinearLayout)findViewById(R.id.layout_time);
        btnDate = (Button)findViewById(R.id.btn_date);
        btnTime = (Button)findViewById(R.id.btn_time);
        sLocation = (Spinner)findViewById(R.id.location_spinner);
        cbPain = (CheckBox)findViewById(R.id.cb_pain);
        cbBlood = (CheckBox)findViewById(R.id.cb_blood);
        //Test object
        btnAdd10 = (Button)findViewById(R.id.btn_add10);

        //Calendar object for default times
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        //Set timePicker to default 24hr mode
        timePick.setIs24HourView(true);
        //Set dateText to current Date
        dateText.setText(
                String.format(Locale.ENGLISH, "%02d", now.get(Calendar.DAY_OF_MONTH)) + "/"
                        + String.format(Locale.ENGLISH, "%02d", now.get(Calendar.MONTH) + 1) + "/"
                        + String.format(Locale.ENGLISH, "%02d", now.get(Calendar.YEAR))
        );
        //Set max date of DatePicker
        datePick.setMaxDate(System.currentTimeMillis());
        //Set timeText to current Time
        timeText.setText(
                String.format(Locale.ENGLISH, "%02d", now.get(Calendar.HOUR_OF_DAY)) + ":"
                        + String.format(Locale.ENGLISH, "%02d", now.get(Calendar.MINUTE))
        );

        //Set spinner listener
        sLocation.setOnItemSelectedListener(this);

        //Set list of locations to be dynamically loaded from table
        //Create list of strings, initialised with array from strings.xml
        List<String> locationList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.location_spinner)));
        //Get all locations from table and add to list
        Cursor locations = myDb.getAllLocations();
        locations.moveToFirst();
        for (int i = 0; i < locations.getCount(); i++) {
            locationList.add(locations.getString(locations.getColumnIndex(myDb.getColLocation())));
            locations.moveToNext();
        }
        locations.close();
        //Add list to set to remove duplicates then add set back to list
        Set<String> ls = new LinkedHashSet<>();
        ls.addAll(locationList);
        locationList.clear();
        locationList.addAll(ls);
        //Adapter created to add list to spinner
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationList);
        sLocation.setAdapter(locationAdapter);
        //Set initial item on spinner to 2nd item, i.e. not *NEW*
        sLocation.setSelection(1);

        //Button logging events
        addData();
        datePick();
        timePick();
        locationReset();
        //Test button method
        TEST_add10();
    }

    private void TEST_add10() {
        btnAdd10.setOnClickListener(
        new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String day = "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        String month = "" + Calendar.getInstance().get(Calendar.MONTH);
                        String year = "" + Calendar.getInstance().get(Calendar.YEAR);

                        myDb.insertData(day, month, year, "9", "05", "Home", "Solid", "N", "N", "Test Record 1");
                        myDb.insertData(day, month, year, "10", "10", "Train Station", "Soft", "N", "N", "Test Record 2");
                        myDb.insertData(day, month, year, "11", "15", "Work", "Solid", "N", "N", "Test Record 3");
                        myDb.insertData(day, month, year, "12", "20", "Work", "Soft", "N", "N", "Test Record 4");
                        myDb.insertData(day, month, year, "13", "25", "Work", "Solid", "N", "N", "Test Record 5");
                        myDb.insertData(day, month, year, "14", "30", "Train Station", "Soft", "N", "N", "Test Record 6");
                        myDb.insertData(day, month, year, "15", "35", "Home", "Liquid", "N", "N", "Test Record 7");
                        myDb.insertData(day, month, year, "16", "40", "Home", "Liquid", "N", "N", "Test Record 8");
                        myDb.insertData(day, month, year, "17", "45", "Home", "Solid", "Y", "N", "Test Record 9P");
                        myDb.insertData(day, month, year, "18", "50", "Home", "Solid", "N", "Y", "Test Record 10B");

                        Toast.makeText(LogActivity.this, "10 records added", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void addData() {
        btnInsertData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String year = dateText.getText().toString().substring(6);
                        String month = dateText.getText().toString().substring(3,5);
                        String day = dateText.getText().toString().substring(0,2);
                        String hour = timeText.getText().toString().substring(0,2);
                        String minute = timeText.getText().toString().substring(3,5);
                        String location = getLocation();
                        String quality = sQuality.getSelectedItem().toString();
                        String pain = checkPain();
                        String blood = checkBlood();
                        String comment = editComment.getText().toString();
                        boolean isInserted = myDb.insertData(day, month, year, hour, minute, location, quality, pain, blood, comment);
                        //Return to home screen
                        Intent i = new Intent(LogActivity.this, HomeActivity.class);
                        startActivity(i);
                        //Display message of success/failure
                        if (isInserted) {
                            Toast.makeText(LogActivity.this, "Data has been inserted",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LogActivity.this, "Data has not been inserted",Toast.LENGTH_LONG).show();
                        }

                    }

                    private String getLocation() {
                        if (sLocation.getVisibility() == View.VISIBLE) {
                            return sLocation.getSelectedItem().toString();
                        } else {
                            return editLocation.getText().toString();
                        }
                    }
                }
        );
    }

    private String checkPain() {
        if (cbPain.isChecked()) {
            return "Y";
        } else {
            return "N";
        }
    }

    private String checkBlood() {
        if (cbBlood.isChecked()) {
            return "Y";
        } else {
            return "N";
        }
    }

    public void datePick() {
        dateText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Store values for current date
                        Calendar now = Calendar.getInstance();
                        now.setTimeInMillis(System.currentTimeMillis());
                        int year = now.get(Calendar.YEAR);
                        int month = now.get(Calendar.MONTH);
                        int day = now.get(Calendar.DAY_OF_MONTH);
                        //Set datePicker object to current date
                        datePick.updateDate(year, month, day);
                        //Set all other UI elements to gone
                        RLSelectors.setVisibility(View.GONE);
                        //Make datePicker object visible
                        DateLayout.setVisibility(View.VISIBLE);
                        btnDate.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Store values from DatePicker
                                        String day = String.format(Locale.ENGLISH, "%02d", datePick.getDayOfMonth());
                                        String month = String.format(Locale.ENGLISH, "%02d", datePick.getMonth() + 1);
                                        String year = String.format(Locale.ENGLISH, "%02d", datePick.getYear());
                                        //Set dateText textView to concatenated string
                                        dateText.setText(day + "/" + month + "/" + year);
                                        //Make DateLayout gone
                                        DateLayout.setVisibility(View.GONE);
                                        //Make all other UI elements reappear
                                        RLSelectors.setVisibility(View.VISIBLE);
                                    }
                                }
                        );

                    }
                }
        );
    }

    public void timePick() {
        timeText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Store values for current time
                        Calendar now = Calendar.getInstance();
                        now.setTimeInMillis(System.currentTimeMillis());
                        int hour = now.get(Calendar.HOUR_OF_DAY);
                        int minute = now.get(Calendar.MINUTE);
                        //Set timePicker to current time
                        timePick.setCurrentHour(hour);
                        timePick.setCurrentMinute(minute);
                        //Set all other UI elements to gone
                        RLSelectors.setVisibility(View.GONE);
                        //Make timePick elements visible
                        TimeLayout.setVisibility(View.VISIBLE);
                        btnTime.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Store values for hour and minute
                                        String hour = String.format(Locale.ENGLISH, "%02d", timePick.getCurrentHour());
                                        String minute = String.format(Locale.ENGLISH, "%02d", timePick.getCurrentMinute());
                                        //Set timeText to selected hour and minute
                                        timeText.setText(hour + ":" + minute);
                                        //Make time layout gone
                                        TimeLayout.setVisibility(View.GONE);
                                        //Make all other UI elements reppear
                                        RLSelectors.setVisibility(View.VISIBLE);
                                    }
                                }
                        );
                    }
                }
        );
    }

    //TODO Find better solution for changing between spinner and editText for location
    public void locationReset() {
        locationText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editLocation.getVisibility() == View.VISIBLE && sLocation.getVisibility() == View.GONE) {
                            editLocation.setVisibility(View.GONE);
                            editLocation.setText("");
                            sLocation.setSelection(0);
                            sLocation.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(sLocation.getItemAtPosition(position).equals("*NEW*")) {
            sLocation.setVisibility(View.GONE);
            editLocation.setVisibility(View.VISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}
