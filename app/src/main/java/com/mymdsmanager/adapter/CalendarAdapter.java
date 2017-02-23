package com.mymdsmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymdsmanager.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class CalendarAdapter extends BaseAdapter {
    private static final int FIRST_DAY_OF_WEEK = Calendar.SUNDAY;
    private final Calendar calendar;
    private final CalendarItem today;
    private final CalendarItem selected;
    private final LayoutInflater inflater;
    private CalendarItem[] days;
    // ArrayList<String> periodDays = new ArrayList<String>();

    ArrayList<String> wrapperList = new ArrayList<>();

    public CalendarAdapter(Context context, Calendar monthCalendar, ArrayList<String> wrapperList) {
        this.wrapperList = wrapperList;

        calendar = monthCalendar;
        today = new CalendarItem(monthCalendar);
        selected = new CalendarItem(monthCalendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refreshDays(
                "_" + (calendar.get(Calendar.MONTH) + 1) + "_"
                        + calendar.get(Calendar.YEAR));
    }


    private String getDateinStr(long milliSeconds) {

        String dateStr = "";
        int[] date_ = new int[3];
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(milliSeconds);
        date_[0] = calendar.get(Calendar.DAY_OF_MONTH);
        date_[1] = (calendar.get(Calendar.MONTH) + 1);
        date_[2] = calendar.get(Calendar.YEAR);

        dateStr = "" + date_[0] + "_" + date_[1] + "_" + date_[2];

        return dateStr;
    }

    public long getNextDays(long _date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(_date)); // Now use today date.
        cal.add(Calendar.DATE, 1); // Adding 5 days

        return cal.getTimeInMillis();
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        final CalendarItem item = days[position];
        if (item != null) {
            return days[position].id;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_calender, parent,
                    false);

            holder.dayView = (TextView) convertView
                    .findViewById(R.id.date);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CalendarItem currentItem = days[position];
        if (currentItem == null) {
            holder.dayView.setClickable(false);
            holder.dayView.setFocusable(false);
            convertView.setBackgroundResource(R.mipmap.calender_box);
            holder.dayView.setText(null);
        } else {
//            if (currentItem.equals(today)) {
//                // today
//                convertView.setBackgroundResource(R.mipmap.selected);
//            } else
            if (currentItem.equals(selected)) {
                // selected
                convertView.setBackgroundResource(R.mipmap.calender_blue);
            } else {
                // normal
                convertView.setBackgroundResource(R.mipmap.calender_box);
            }
            holder.dayView.setText(currentItem.text);
        }

        String dateKey = "";
        holder.image.setVisibility(View.GONE);


        if (currentItem != null) {
            dateKey = currentItem.day + "-" + (currentItem.month + 1) + "-"
                    + currentItem.year;
            String dayString = currentItem.day + "", monthString = (currentItem.month + 1) + "";

            if ((currentItem.month + 1) < 10) {
                monthString = "0" + (currentItem.month + 1);
            }
            if (currentItem.day < 10) {
                dayString = "0" + currentItem.day;
            }
            String dateString = monthString + "-" + dayString + "-" + currentItem.year;
            if (wrapperList.contains(dateString)) {
                holder.image.setVisibility(View.VISIBLE);
            }
        }


        return convertView;
    }

    String[] dayArr = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    String[] monthArr = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November",
            "December"};


    private int mod(int x, int y) {
        int result = x % y;
        return result < 0 ? result + y : result;
    }

    private class ViewHolder {
        TextView dayView;
        ImageView image;
    }

    public final void setSelected(int year, int month, int day) {
        selected.year = year;
        selected.month = month;
        selected.day = day;
        notifyDataSetChanged();
    }

    public final void refreshDays(String monthYear) {

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        final int lastDayOfMonth = calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH);
        final int blankies;
        final CalendarItem[] days;
        // String selectedMonthYear = "_"+(month+1)+"_"+year;

        if (firstDayOfMonth == FIRST_DAY_OF_WEEK) {
            blankies = 0;
        } else if (firstDayOfMonth < FIRST_DAY_OF_WEEK) {
            blankies = Calendar.SATURDAY - (FIRST_DAY_OF_WEEK - 1);
        } else {
            blankies = firstDayOfMonth - FIRST_DAY_OF_WEEK;
        }
        days = new CalendarItem[lastDayOfMonth + blankies];

        for (int day = 1, position = blankies; position < days.length; position++) {

            int day_ = day++;

            days[position] = new CalendarItem(year, month, day_);
        }

        this.days = days;
        notifyDataSetChanged();
    }

    private static class CalendarItem {
        public int year;
        public int month;
        public int day;
        public String text = "";
        public long id;

        public CalendarItem(Calendar calendar) {
            this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }

        public long getDate() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            return cal.getTimeInMillis();
        }

        public CalendarItem(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.text = String.valueOf(day);
            this.id = Long.valueOf(year + "" + month + "" + day);
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof CalendarItem) {
                final CalendarItem item = (CalendarItem) o;
                return item.year == year && item.month == month
                        && item.day == day;
            }
            return false;
        }
    }

    public String getCurrent_datetag() {
        String tag = today.day + "_" + today.month + "_" + today.year;
        return tag;
    }

    private boolean isDateBetweenStartAndEndDate(String start_date,
                                                 String end_date, String date) {

        long st_d = Long.parseLong(start_date);
        long ed_d = Long.parseLong(end_date);
        long d_ = Long.parseLong(date);

        if (d_ >= st_d && d_ <= ed_d) {
            return true;
        }
        return false;

    }

}