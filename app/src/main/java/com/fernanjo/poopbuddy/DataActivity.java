package com.fernanjo.poopbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Anthony on 14/06/2017.
 * This activity displays information based on the available records in the database
 */

public class DataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //TODO Have alterable graph for displaying data with user defined inputs
    //TODO Update code so that tables reflect relevant data
    //TODO Set placeholder text for when there is no data
    DatabaseHelper myDb;

    TextView tvNone, tvRow1, tvRow2, tvRow3;
    TextView tvTitle1, tvTitle2, tvTitle3;
    Spinner sParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_data);
        myDb = new DatabaseHelper(this);

        tvNone = (TextView)findViewById(R.id.tv_nodata);

        tvRow1 = (TextView)findViewById(R.id.data_row1);
        tvRow2 = (TextView)findViewById(R.id.data_row2);
        tvRow3 = (TextView)findViewById(R.id.data_row3);

        tvTitle1 = (TextView)findViewById(R.id.data_title1);
        tvTitle2 = (TextView)findViewById(R.id.data_title2);
        tvTitle3 = (TextView)findViewById(R.id.data_title3);

        sParam = (Spinner)findViewById(R.id.data_spinner);
        sParam.setOnItemSelectedListener(this);



    }

    /*TODO Fixes to poopPerDay
    For this month total, per day figure should be based on days of month passed so far
    Otherwise, total should be displayed as a rounded number to 2 decimal places
    Corrected to be a float, rather than long and now need to confirm 2 decimal places
     */
    private String poopPerDay(int month, int year) { return myDb.poopPerDay(month+1, year); }

    public String popularLocation() {
        return myDb.mostPopLoc();
    }

    public String poopThisMonth() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH);
        int result = myDb.monthTotal(month+1);
        return "" + result;
    }

    //Method to change table layout dependent on item selected in spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Variables to assign int to Data Select Screen, based on Spinner on Data page
        //TODO Get a better way of assigning numbers to selection options.
        int allData = 0;
        int thisMonthData = 1;
        int thisYearData = 2;

        int res = myDb.tableCount();

        /*TODO Decide whether to have multiple layouts or set layouts dynamically
        If setting layouts dynamically, code below will become really long.
        However, using multiple layouts becomes problematic because all items need to be referenced and then assigned a value anyway.
        Probably better just to use layout dynamically and then update as necessary? Possible better solution available.
        */
        //Use if statements to set layout
        if (res <= 0) {
            //Make placeholder text visible
            tvNone.setVisibility(View.VISIBLE);
            //Hide all other UI elements
            tvTitle1.setVisibility(View.GONE);
            tvTitle2.setVisibility(View.GONE);
            tvTitle3.setVisibility(View.GONE);
            tvRow1.setVisibility(View.GONE);
            tvRow2.setVisibility(View.GONE);
            tvRow3.setVisibility(View.GONE);
        }
        if(sParam.getItemIdAtPosition(position) == thisMonthData && res > 0) {
            //Set text of Table elements
            String row1 = "Poops This Month";
            String row2 = "Poops Per Day";
            String row3 = "Most popular Location";
            tvTitle1.setText(row1);
            tvTitle2.setText(row2);
            tvTitle3.setText(row3);
            tvRow1.setText(poopThisMonth());
            tvRow2.setText(poopPerDay(Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR)));
            tvRow3.setText(popularLocation());

            //Set visibility of all table elements
            tvNone.setVisibility(View.GONE);
            tvTitle1.setVisibility(View.VISIBLE);
            tvTitle2.setVisibility(View.VISIBLE);
            tvTitle3.setVisibility(View.VISIBLE);
            tvRow1.setVisibility(View.VISIBLE);
            tvRow2.setVisibility(View.VISIBLE);
            tvRow3.setVisibility(View.VISIBLE);

        } else {
            tvNone.setVisibility(View.VISIBLE);
            tvRow1.setVisibility(View.GONE);
            tvRow2.setVisibility(View.GONE);
            tvRow3.setVisibility(View.GONE);
            tvTitle1.setVisibility(View.GONE);
            tvTitle2.setVisibility(View.GONE);
            tvTitle3.setVisibility(View.GONE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}
