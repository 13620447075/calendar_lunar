package com.example.asus.calendarselector.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.asus.calendarselector.R;
import com.example.asus.calendarselector.calendar.CalendarSelector;
import com.example.asus.calendarselector.calendar.Utils;
import com.example.asus.calendarselector.utils.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CalendarSelector.ICalendarSelectorCallBack {

    private TextView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendar);
        calendar.setOnClickListener(this);

        /**
         * 时间戳！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
         * 是否农历 或者公历
         */

        //公历转农历  输出结果： 2017腊月廿七
        Map<String, String> date = Utils.averageToLunar(2018, 2, 12);
        String year = date.get("year");
        String month = date.get("month");
        String day = date.get("day");
        LogUtils.e("date:::" + year + month + day);


        //农历转公历  输出结果： 20111225
        Map<String, Integer> date1 = Utils.lunaToAverage(2011, 12, 10);
        int year1 = date1.get("year");
        int month1 = date1.get("month");
        int day1 = date1.get("day");
        LogUtils.e("dddddddddddddddddddddd" + year1 + month1 + day1);

    }

    @Override
    public void onClick(View v) {
        CalendarSelector selector = new CalendarSelector(MainActivity.this, 0, this);
        selector.show(calendar);
    }

    @Override
    public void transmitPeriod(HashMap<String, String> result) {
        calendar.setText(result.get("year") + result.get("month") + result.get("day")
                + result.get("hour") + result.get("minute")
                + result.get("calender"));
    }
}
