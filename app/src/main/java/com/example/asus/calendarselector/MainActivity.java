package com.example.asus.calendarselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.asus.calendarselector.calendar.CalendarSelector;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CalendarSelector.ICalendarSelectorCallBack {

    private TextView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendar);
        calendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CalendarSelector selector = new CalendarSelector(MainActivity.this,0,this);
        selector.show(calendar);
    }

    @Override
    public void transmitPeriod(HashMap<String, String> result) {
        calendar.setText(result.get("year")+result.get("month")+result.get("day")+result.get("hour")+result.get("minute"));
    }
}
