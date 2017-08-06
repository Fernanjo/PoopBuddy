package com.fernanjo.poopbuddy;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Anthony on 10/07/2017.
 * Used by RecordsActivity.java as standard template for all records
 */
//TODO Potentially change nested weights to hardcoded value for TextView heights, if performance is poor

class RecordLayout extends LinearLayout {
    int rId, rDataId;
    String rPain, rBlood, rComment;

    public RecordLayout(Context context, int idNum, int dataId, String date, String time, String location, String quality,
                        String pain, String blood, String comment) {
        super(context);
        //set basics
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pxToDp(50));
        this.setId(dataId);
        this.setBackgroundResource(R.drawable.rlborder);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(LayoutParams);

        rDataId = dataId;
        rId = idNum;
        rPain = pain;
        rBlood = blood;
        rComment = comment;

        //Create number TextView
        TextView idText = new TextView(context);
        LinearLayout.LayoutParams TVParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
        idText.setLayoutParams(TVParams);
        idText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        int padding = pxToDp(10);
        idText.setPadding(padding,padding,padding,padding);
        String idStr = idNum + ".";
        idText.setText(idStr);

        //LinearLayout for date and time
        LinearLayout LLDateTime = new LinearLayout(context);
        LinearLayout.LayoutParams LLDTParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        LLDateTime.setOrientation(LinearLayout.VERTICAL);
        LLDateTime.setLayoutParams(LLDTParams);
        int innerPadding = pxToDp(5);
        LLDateTime.setPadding(innerPadding, innerPadding, innerPadding, innerPadding);

        //TextView for date
        TextView dateText = new TextView(context);
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        dateText.setLayoutParams(dateParams);
        dateText.setGravity(Gravity.CENTER);
        dateText.setText(date);

        //TextView for time
        TextView timeText = new TextView(context);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        timeText.setLayoutParams(timeParams);
        timeText.setGravity(Gravity.CENTER);
        timeText.setText(time);

        //Linear Layout for location and quality
        LinearLayout LLLocQual = new LinearLayout(context);
        LinearLayout.LayoutParams LLLQParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        LLLocQual.setOrientation(LinearLayout.VERTICAL);
        LLLocQual.setLayoutParams(LLLQParams);
        LLLocQual.setPadding(innerPadding, innerPadding, innerPadding, innerPadding);

        //TextView for location
        TextView locationText = new TextView(context);
        LinearLayout.LayoutParams locationParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        locationText.setLayoutParams(locationParams);
        locationText.setGravity(Gravity.CENTER);
        locationText.setText(location);

        //TextView for quality
        TextView qualityText = new TextView(context);
        LinearLayout.LayoutParams qualityParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        qualityText.setLayoutParams(qualityParams);
        qualityText.setGravity(Gravity.CENTER);
        qualityText.setText(quality);

        //Add views to correct layouts
        LLDateTime.addView(dateText);
        LLDateTime.addView(timeText);
        LLLocQual.addView(locationText);
        LLLocQual.addView(qualityText);
        this.addView(idText);
        this.addView(LLDateTime);
        this.addView(LLLocQual);
    }

    public int pxToDp(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public int getId() {return rId;}

    public String getrPain() {return rPain;}

    public String getrBlood() {return rBlood;}

    public String getrComment() {return rComment;}

    public int getDataId() {return rDataId;}
}