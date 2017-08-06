package com.fernanjo.poopbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Anthony on 08/06/2017.
 * This activity displays each record with associated information in a vertically scrolling screen
 */

//TODO Allow for editing of individual records
public class RecordsActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper myDb;
    Button delAll;
    TextView tvNoRecs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_records);
        myDb = new DatabaseHelper(this);

        delAll = (Button)findViewById(R.id.btn_del);
        tvNoRecs = (TextView)findViewById(R.id.tv_norecords);

        deleteRecords();
        refreshRecords();
    }

    private void deleteRecords() {
        delAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isTableCleared = myDb.clearAllRecords();
                        if (isTableCleared) {
                            Toast.makeText(RecordsActivity.this, "All records have been deleted", Toast.LENGTH_LONG).show();
                            LinearLayout ll = (LinearLayout)findViewById(R.id.ll_records);
                            ll.removeAllViews();
                            refreshRecords();
                        } else {
                            Toast.makeText(RecordsActivity.this, "No records were deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    private void refreshRecords() {
        Cursor records = myDb.getAllData();
        records.moveToFirst();
        if (records.getCount() == 0) {
            tvNoRecs.setVisibility(View.VISIBLE);
        } else {
            tvNoRecs.setVisibility(View.GONE);
        }
        for (int i = 0; i < records.getCount(); i++) {
            int id = records.getInt(records.getColumnIndex(myDb.getColId()));
            String date = records.getInt(records.getColumnIndex(myDb.getColDay())) + "/" +
                    records.getInt(records.getColumnIndex(myDb.getColMonth())) + "/" +
                    records.getInt(records.getColumnIndex(myDb.getColYear()));
            String time = records.getInt(records.getColumnIndex(myDb.getColHour())) + ":" +
                    String.format(Locale.ENGLISH,"%02d",records.getInt(records.getColumnIndex(myDb.getColMinute())));
            String location = records.getString(records.getColumnIndex(myDb.getColLocation()));
            String quality = records.getString(records.getColumnIndex(myDb.getColQuality()));
            String pain = records.getString(records.getColumnIndex(myDb.getColPain()));
            String blood = records.getString(records.getColumnIndex(myDb.getColBlood()));
            String comment = records.getString(records.getColumnIndex(myDb.getColComment()));
            addRecord(i+1, id, date, time, location, quality, pain, blood, comment);
            records.moveToNext();
        }
        records.close();
    }

    public void addRecord(int id, int dataId, String date, String time, String location, String quality,
                          String pain, String blood, String comment) {
        RecordLayout addingRecord = new RecordLayout(this, id, dataId, date, time, location, quality, pain, blood, comment);
        LinearLayout master = (LinearLayout)findViewById(R.id.ll_records);
        master.addView(addingRecord);
        addingRecord.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        final RecordLayout record = (RecordLayout)v;
        int rId = v.getId();
        String rComment = record.getrComment();
        String rPain = record.getrPain();
        String rBlood = record.getrBlood();

        AlertDialog alertDialog = new AlertDialog.Builder(RecordsActivity.this).create();
        alertDialog.setTitle("Record " + rId);
        //Subtract 1 from id to match index of list
        alertDialog.setMessage("Comment:\t" + rComment + "\nPain?\t" + rPain + "\nBlood?\t" + rBlood);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Delete Record",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Reference master LinearLayout
                        LinearLayout master = (LinearLayout)findViewById(R.id.ll_records);
                        //Delete record based on ID from database table
                        boolean delRecord = myDb.clearRecord(record.getDataId());
                        //If record delete was successful, display message
                        if (delRecord) {
                            Toast.makeText(RecordsActivity.this, "One record was deleted", Toast.LENGTH_LONG).show();
                            //Remove record based on Record ID of RecordLayout, minus 1
                            master.removeAllViews();
                            refreshRecords();
                        } else {
                            //If record was not deleted
                            Toast.makeText(RecordsActivity.this, "No records were deleted", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
