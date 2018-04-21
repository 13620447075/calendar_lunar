package com.example.asus.calendarselector.calendar;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.calendarselector.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cecil on 2017/8/22.
 * Email:guixixuan1120@outlook.com
 */

public class CalendarSelector {

    private Activity mContext;
    private HashMap<String, Object> mMap;
    private ICalendarSelectorCallBack mCallBack;
    private List<String> mYearList;
    private List<String> mMonthList;
    private List<String> mDayList;
    private List<String> mHourList;
    private List<String> mMinuteList;
    private TextView tvConfirm;
    private TextView tvCancel;
    private TextView tvAverage;
    private TextView tvLunar;
    private PopupWindow mPopupWindow;
    private String mSelectedYear;
    private String mSelectedMonth;
    private String mSelectedDay;
    private String mSelectedHour;
    private String mSelectedMinute;
    //    private CheckBox cbHideYear;
    private boolean isHideYear = false;
    private String calendarType = "average";

    /**
     * 初始化年月日
     * 剪切时间
     */
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    Date date = new Date(System.currentTimeMillis());
    String time = simpleDateFormat.format(date);
    int year = Integer.parseInt(time.substring(0, 4));
    int mounth = Integer.parseInt(time.substring(5, 7));
    int day = Integer.parseInt(time.substring(8, 10));
    int hour = Integer.parseInt(time.substring(12, 14));
    int minute = Integer.parseInt(time.substring(15, 17));

    private int mYearPosition = year - 1901;
    private int mMonthPosition = mounth - 1;
    private int mDayPosition = day - 1;
    private int mHourPosition = hour;
    private int mMinutePosition = minute;
    private boolean isLunar = false;
    private StringScrollPicker mYearPicker;
    private StringScrollPicker mMonthPicker;
    private StringScrollPicker mDayPicker;
    private StringScrollPicker mHourPicker;
    private StringScrollPicker mMinutePicker;

    public interface ICalendarSelectorCallBack {
        void transmitPeriod(HashMap<String, String> result);
    }

    public CalendarSelector(Activity context, int position,
                            ICalendarSelectorCallBack iCalendarSelectorCallBack) {
        this.mContext = context;
        this.mCallBack = iCalendarSelectorCallBack;
        initData();
        initView();
        initListener();

    }

    private void initData() {
        mYearList = new ArrayList<>();
        mMonthList = new ArrayList<>();
        mDayList = new ArrayList<>();
        mHourList = new ArrayList<>();
        mMinuteList = new ArrayList<>();
        for (int i = 1901; i < 2098; i++) {
            mYearList.add(i + "年");
        }
        for (int i = 1; i < 13; i++) {
            mMonthList.add(i + "月");
        }
        for (int i = 0; i < 24; i++) {
            mHourList.add(i + "时");
        }
        for (int i = 0; i < 61; i++) {
            mMinuteList.add(i + "分");
        }
    }

    private void initView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.popup_window_calendar_selector,
                null);
        mYearPicker = v.findViewById(R.id.ssp_year);
        mMonthPicker = v.findViewById(R.id.ssp_month);
        mDayPicker = v.findViewById(R.id.ssp_day);
        mHourPicker = v.findViewById(R.id.ssp_hour);
        mMinutePicker = v.findViewById(R.id.ssp_minute);
        tvConfirm = v.findViewById(R.id.tv_confirm);
        tvCancel = v.findViewById(R.id.tv_cancel);
        tvAverage = v.findViewById(R.id.tv_average);
        tvLunar = v.findViewById(R.id.tv_lunar);
        tvAverage.setBackground(ContextCompat.getDrawable(mContext, R.drawable
                .bg_selected_calendar));
        initPicker();
        initPopup(v);
    }

    private void initPicker() {
        mYearPicker.setIsCirculation(false);
        mYearPicker.setData(mYearList);
        mYearPicker.setSelectedPosition(mYearPosition);
        mMap = Utils.parseAverageYear(1901 + mYearPosition + "年");
        setMonthList();
        mMonthPicker.setIsCirculation(false);
        mDayPicker.setIsCirculation(false);
        mHourPicker.setIsCirculation(false);
        mHourPicker.setData(mHourList);
        mHourPicker.setSelectedPosition(mHourPosition);
        mMinutePicker.setIsCirculation(false);
        mMinutePicker.setData(mMinuteList);
        mMinutePicker.setSelectedPosition(mMinutePosition);
        mSelectedYear = mYearList.get(mYearPosition);
        mSelectedMonth = mMonthList.get(mMonthPosition);
        mSelectedDay = mDayList.get(mDayPosition);
        mSelectedHour = mHourList.get(mHourPosition);
        mSelectedMinute = mMinuteList.get(mMinutePosition);
        mYearPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedYear = mYearList.get(position);
                mYearPosition = position;
                if (isLunar) {
                    mMap = Utils.parseLunarYear(mSelectedYear);
                } else {
                    mMap = Utils.parseAverageYear(mSelectedYear);
                }
                setMonthList();
            }
        });
        mMonthPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedMonth = mMonthList.get(position);
                mMonthPosition = position;
                setDayList();
            }
        });
        mDayPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedDay = mDayList.get(position);
                mDayPosition = position;
            }
        });
        mHourPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedHour = mHourList.get(position);
                mHourPosition = position;

            }
        });
        mMinutePicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSelectedMinute = mMinuteList.get(position);
                mMinutePosition = position;
            }
        });
    }

    /**
     *
     */
    private void setMonthList() {
        mMonthList = (List<String>) mMap.get("month");
        mMonthPicker.setData(mMonthList);
        mMonthPosition = mMonthPosition >= mMonthList.size() ? mMonthList.size()
                - 1 :
                mMonthPosition;
        mMonthPicker.setSelectedPosition(mMonthPosition);
        setDayList();
    }

    /**
     *
     */
    private void setDayList() {
        mDayList = ((List<List<String>>) mMap.get("day")).get(mMonthPosition);
        mDayPicker.setData(mDayList);
        mDayPosition = mDayPosition >= mDayList.size() ? mDayList.size() - 1 :
                mDayPosition;
        mDayPicker.setSelectedPosition(mDayPosition);
    }


    private void initPopup(View v) {
        mPopupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.backgroundAlpha(mContext, 1f);
            }
        });
    }

    private void initListener() {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("year", mSelectedYear);
                map.put("month", mSelectedMonth);
                map.put("day", mSelectedDay);
                map.put("hour", mSelectedHour);
                map.put("minute", mSelectedMinute);
                map.put("calender", calendarType);
                mCallBack.transmitPeriod(map);
                mPopupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        tvLunar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLunar) {
                    return;
                }
                if ("1901年".equals(mYearPicker.getSelectedItem()) || "2099年".equals(mYearPicker
                        .getSelectedItem())) {
                    Toast.makeText(mContext, "因条件受限，本APP暂不提供1901年和2099年阴阳历数据的转换，感谢理解", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                isLunar = true;
                Log.d("Cecil", "average2Lunar");
                mMap = Utils.average2Lunar(mYearPicker.getSelectedItem(),
                        mMonthPosition, mDayPosition);
                mMonthPosition = (int) mMap.get("monthPosition");
                mDayPosition = (int) mMap.get("dayPosition");
                mYearPicker.setSelectedPosition((int) mMap.get("yearPosition"));
                tvLunar.setBackground(ContextCompat.getDrawable(mContext, R.drawable
                        .bg_selected_calendar));
                tvAverage.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_transparent));
                setMonthList();
                setDayList();
                calendarType = "lunar";
            }
        });
        tvAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLunar) {
                    return;
                }
                if ("1901年".equals(mYearPicker.getSelectedItem()) || "2099年".equals(mYearPicker
                        .getSelectedItem())) {
                    Toast.makeText(mContext, "因条件受限，本APP暂不提供1901年和2099年阴阳历数据的转换，感谢理解", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                isLunar = false;
                Log.d("Cecil", "lunar2Average");
                HashMap<String, Object> map = Utils.lunar2Average(mYearPicker.getSelectedItem(),
                        mMonthPosition, mDayPosition);
                mMonthPosition = (int) map.get("monthPosition");
                mDayPosition = (int) map.get("dayPosition");
                mYearPicker.setSelectedPosition((int) map.get("yearPosition"));
                tvAverage.setBackground(ContextCompat.getDrawable(mContext, R.drawable
                        .bg_selected_calendar));
                tvLunar.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_transparent));
                mMap = Utils.parseAverageYear(mYearPicker.getSelectedItem());
                setMonthList();
                setDayList();
                calendarType = "average";
            }
        });
    }

    public void show(View v) {
        Utils.hideSoftInput(mContext);
        Utils.backgroundAlpha(mContext, 0.5f);
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

}
