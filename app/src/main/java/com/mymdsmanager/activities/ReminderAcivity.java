package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.datacontrollers.Constants;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.wrapper.MedicineInfoWrapper;
import com.mymdsmanager.wrapper.ReminderWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReminderAcivity extends AppCompatActivity {
    @Bind(R.id.reminderSwitch)
    Switch reminderSwitch;
    @Bind(R.id.scheduleBtn)
    Button scheduleBtn;
    @Bind(R.id.frequencyBtn)
    Button frequencyBtn;
    @Bind(R.id.startTimeTxt)
    TextView startTimeTxt;
    @Bind(R.id.timeTxt)
    TextView timeTxt;
    @Bind(R.id.startContainer)
    RelativeLayout startContainer;
    @Bind(R.id.reminderContainer)
    RelativeLayout reminderContainer;
    @Bind(R.id.timeListView)
    ListView timeListView;
    @Bind(R.id.soundBtn)
    Button soundBtn;
    private Dialog dialog;
    private Toolbar toolbar;
    private final int TIME_DIALOG_ID = 2;
    String timeString = "";
    TimeAdapter adapter;
    boolean strat_time_boolean = true;
    ArrayList<String> timeLst;
    int time_list_position;
    int hourOfDay = 0, minute = 0;
    String ringtone_nameStr="",schduleStr="";
    int duration = 1;
    MedicineInfoWrapper rowItem;
    private String[] ringtoneArr = new String[]


            {"default", "Attention",
                    "Frenzy",
                    "Gentlealarm",
                    "Jingle bell",
                    "Msg posted",
                    "Soft play once"};
    private TimePickerDialog.OnTimeSetListener timePickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            ReminderAcivity.this.hourOfDay = hourOfDay;
            ReminderAcivity.this.minute = minute;
            String hourString = hourOfDay + "", minuteString = minute + "";

            if (hourOfDay < 10) {
                hourString = "0" + hourOfDay;
            }
            if (minute < 10) {
                minuteString = "0" + minute;
            }

            timeString = hourString + "-" + minuteString;
            if (strat_time_boolean) {
//                timeTxt.setText(formateTime(timeString));
//                timeLst.set(0, formateTime(timeString));
                setTimeList();
            } else {
                timeLst.set(time_list_position, formateTime(timeString));
                adapter.notifyDataSetChanged();
            }
//            setTimeList();

        }
    };

//    private void exportToCal(String title, String notes, long millie) {
//        try {
//            final ContentValues event = new ContentValues();
//            event.put(CalendarContract.Events.CALENDAR_ID, 1);
//
//            event.put(CalendarContract.Events.TITLE, "MyMdsTracker : " + title);
//            event.put(CalendarContract.Events.DESCRIPTION, notes);
//            event.put(CalendarContract.Events.EVENT_LOCATION, "");
//            event.put(CalendarContract.Events.DTSTART, millie);
//
//
//            event.put(CalendarContract.Events.DTEND, millie + 3600000);
//
//
//            String timeZone = TimeZone.getDefault().getID();
//            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
//
//            Uri baseUri;
//            if (Build.VERSION.SDK_INT >= 8) {
//                baseUri = Uri.parse("content://com.android.calendar/events");
//            } else {
//                baseUri = Uri.parse("content://calendar/events");
//            }
//
//            getContentResolver().insert(baseUri, event);
//
//
//            Toast.makeText(AddAppointmentsActivity.this, "Event successfully exported", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void showPopUp(final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, ringtoneArr);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();


                soundBtn.setText(ringtoneArr[position]);
                ringtone_nameStr = ringtoneArr[position];


            }
        });


    }

    private void setCurrentTime() {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);


    }

    private void setTimeList() {

        timeLst = new ArrayList<>();

        int var = 24 / duration;


        int hourOfDayTemp = hourOfDay, minuteTemp = minute;

        for (int i = 0; i < duration; i++) {
            hourOfDayTemp = hourOfDayTemp + var;


            String minuteString = minuteTemp + "", hourString = hourOfDayTemp + "";

            if (hourOfDayTemp > 24) {
                hourOfDayTemp = hourOfDayTemp - 24;
            }
            if (hourOfDayTemp < 10) {
                hourString = "0" + hourOfDayTemp;
            }

            if (minuteTemp < 10) {
                minuteString = "0" + minuteTemp;
            }
            timeString = hourString + "-" + minuteString;
            timeLst.add(formateTime(timeString));
            System.out.println("timeLst" + timeLst.toString());

            Log.e("timeList", timeLst.toString());
        }
        Collections.reverse(timeLst);
        adapter = new TimeAdapter(ReminderAcivity.this, timeLst);
        timeListView.setAdapter(adapter);
    }

    @OnClick(R.id.startTimeTxt)
    public void startTime() {
        strat_time_boolean = true;
        showDialog(TIME_DIALOG_ID);
    }

    private String formateTime(String timeString) {
        String oriTime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH-mm");
            Date dateObj = sdf.parse(timeString);
            System.out.println(dateObj);
            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));

            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
            Date dt = null;
            try {
                dt = sdf.parse(timeString);
                System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            oriTime = sdfs.format(dt);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return oriTime;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();

        switch (id) {

            case TIME_DIALOG_ID:
                // set date picker as current date

                return new TimePickerDialog(this, timePickerListener, c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), false);
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_reminder);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Reminder");
        ButterKnife.bind(this);
        if (DataManager.getInstance().getReminderTimeList().isEmpty()) {
            scheduleBtn.setText("");
            frequencyBtn.setText("");
            reminderContainer.setVisibility(View.GONE);
            reminderSwitch.setChecked(false);
        } else {
            scheduleBtn.setText("Daily");
            reminderSwitch.setChecked(true);
            DataManager.getInstance().setReminderTimeList(DataManager.getInstance().getReminderTimeList());
            adapter = new TimeAdapter(ReminderAcivity.this, DataManager.getInstance().getReminderTimeList());
            timeListView.setAdapter(adapter);
            frequencyBtn.setText("once a day");
            reminderContainer.setVisibility(View.VISIBLE);
        }
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scheduleBtn.setText("Daily");
                    frequencyBtn.setText("once a day");
                    reminderContainer.setVisibility(View.VISIBLE);
                    MyApplication.reminderBln=true;
                } else {
                    scheduleBtn.setText("");
                    frequencyBtn.setText("");
                    reminderContainer.setVisibility(View.GONE);
                    MyApplication.reminderBln=false;
                }
            }
        });
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        hourOfDay = today.hour;
        minute = today.minute;
        String hourString = today.hour + "", minuteString = today.minute + "";
        if (today.hour < 10) {
            hourString = "0" + today.hour;
        }
        if (today.minute < 10) {
            minuteString = "0" + today.minute;
        }

        timeString = hourString + "-" + minuteString;
        timeTxt.setText(formateTime(timeString));
        setTimeList();
//       /* textViewDay.setText(today.monthDay + "");             // Day of the month (1-31)
//        textViewMonth.setText(today.month + "");              // Month (0-11)
//        textViewYear.setText(today.year + "");
// Year
//        textViewTime.setText(to*/day.format("%k:%M:%S"));
        if (MedicationActivity.show_dialog_flag==false) {
            rowItem = (MedicineInfoWrapper) getIntent().getExtras().getSerializable("wrapper");
            if (rowItem!=null)
            {


                timeLst = new ArrayList<>();
                String time = rowItem.getReminderstring().replace("", "");
                String timeStr[] = time.split(",");
                for (int i = 0; i < timeStr.length; i++) {
                    timeLst.add(timeStr[i]);
                }
                Collections.reverse(timeLst);
                adapter = new TimeAdapter(ReminderAcivity.this, timeLst);
                timeListView.setAdapter(adapter);
                if (!TextUtils.isEmpty(rowItem.getRemindersoundfile()))
                {
                    soundBtn.setText(rowItem.getRemindersoundfile());
                }else if (!TextUtils.isEmpty(rowItem.getRemindercounts()))
                {
                    frequencyBtn.setText(rowItem.getRemindercounts());
                }else if (!TextUtils.isEmpty(rowItem.getReminderstring()))
                {
//            timeLst = new ArrayList<>();
//            String time = rowItem.getReminderstring().replace("", "");
//            String timeStr[] = time.split(",");
//            for (int i = 0; i < timeStr.length; i++) {
//                timeLst.add(timeStr[i]);
//            }
//            Collections.reverse(timeLst);
//            adapter = new TimeAdapter(ReminderAcivity.this, timeLst);
//            timeListView.setAdapter(adapter);
                }
            }
        }

    }

    @OnClick(R.id.frequencyBtn)
    public void frequencyBtn()

    {
        showMedicationDialog(false);
    }

    @OnClick(R.id.soundBtn)
    public void soundBtn()

    {
        showPopUp("Select Sound");
    }

    @OnClick(R.id.scheduleBtn)
    public void scheduleBtn()

    {
        showMedicationDialog(true);
    }

    @Override
    public void onBackPressed() {
       if (MyApplication.reminderBln) {
           if (timeLst.size() != 0) {
               DataManager.getInstance().setReminderTimeList(timeLst);
               ReminderWrapper reminderWrapper = new ReminderWrapper();
               reminderWrapper.setRemindercounts(frequencyBtn.getText().toString());
               reminderWrapper.setRemindersoundfile(ringtone_nameStr);
               reminderWrapper.setRemindersoundindex("");
               reminderWrapper.setReminderstarttime(timeString);
               reminderWrapper.setScheduledays("");
               DataManager.getInstance().setReminderWrapper(reminderWrapper);
               Constants.add_reminder = false;
           }
       }else
       {
           ArrayList<String> timeLst= new ArrayList<>();
           DataManager.getInstance().setReminderTimeList(timeLst);
           ReminderWrapper reminderWrapper = new ReminderWrapper();
           reminderWrapper.setRemindercounts("");
           reminderWrapper.setRemindersoundfile("");
           reminderWrapper.setRemindersoundindex("");
           reminderWrapper.setReminderstarttime("");
           reminderWrapper.setScheduledays("");
           DataManager.getInstance().setReminderWrapper(reminderWrapper);
//           Constants.add_reminder = false;
       }
        finish();
    }

    private void showMedicationDialog(final boolean isDailySelected) {

        dialog = new Dialog(ReminderAcivity.this, R.style.AppCompatTheme);

        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_reminder_frequency);

        TextView frequencyTxt, intervalTxt;
        final ListView frequencyListView = (ListView) dialog.findViewById(R.id.frequencyListView);
        final ListView intervalListView = (ListView) dialog.findViewById(R.id.intervalListView);


        final String frequencyarr[] = new String[]{"once a day", "twice a day", "3 times a day", "4 times a day", "6 times a day", "8 times a day", "12 times a day", "24 times a day"};
        final String intervalarr[] = new String[]{"every 24 hours", "every 12 hours", "every 8 hours", "every 6 hours", "every 4 hours", "every 3 hours", "every 2 hours", "every hour",};

        final String repeatArr[] = new String[]{"Daily", "Sun", "Mon", "Tues", "Wed", "Thus", "Fri", "Sat"};
        frequencyTxt = (TextView) dialog.findViewById(R.id.frequencyTxt);
        intervalTxt = (TextView) dialog.findViewById(R.id.intervalTxt);
        if (isDailySelected) {
            intervalTxt.setVisibility(View.GONE);
            intervalListView.setVisibility(View.GONE);
            BoneMarrowAdapter freAdapter = new BoneMarrowAdapter(ReminderAcivity.this, repeatArr);
            frequencyListView.setAdapter(freAdapter);

            frequencyTxt.setText("REPEAT");
            setListViewHeightBasedOnChildren(frequencyListView);
        } else {
            BoneMarrowAdapter freAdapter = new BoneMarrowAdapter(ReminderAcivity.this, frequencyarr);
            frequencyListView.setAdapter(freAdapter);

            frequencyTxt.setVisibility(View.VISIBLE);
            intervalTxt.setText("FREQUENCY");
//            setListViewHeightBasedOnChildren(frequencyListView);
//            setListViewHeightBasedOnChildren(intervalListView);
        }


        LinearLayout takePhotoLl = (LinearLayout) dialog.findViewById(R.id.takePhoto_ll);


        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        BoneMarrowAdapter intervaladapter = new BoneMarrowAdapter(ReminderAcivity.this, intervalarr);


        intervalListView.setAdapter(intervaladapter);

        intervalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    duration = 1;
                } else if (position == 1) {
                    duration = 2;
                } else if (position == 2) {
                    duration = 3;
                } else if (position == 3) {
                    duration = 4;
                } else if (position == 4) {
                    duration = 6;
                } else if (position == 5) {
                    duration = 8;
                } else if (position == 6) {
                    duration = 12;
                } else if (position == 7) {
                    duration = 24;
                }
                setTimeList();

                frequencyBtn.setText(intervalarr[position]);
                dialog.dismiss();
            }
        });
        frequencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDailySelected) {
                    scheduleBtn.setText(repeatArr[position]);
                    schduleStr = repeatArr[position];
                }
                else {

                    if (position == 0) {
                        duration = 1;
                    } else if (position == 1) {
                        duration = 2;
                    } else if (position == 2) {
                        duration = 3;
                    } else if (position == 3) {
                        duration = 4;
                    } else if (position == 4) {
                        duration = 6;
                    } else if (position == 5) {
                        duration = 8;
                    } else if (position == 6) {
                        duration = 12;
                    } else if (position == 7) {
                        duration = 24;
                    }
                    setTimeList();
                    frequencyBtn.setText(frequencyarr[position]);

                }
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private class TimeAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> frequencyarr;
        public TimeAdapter(Context context,
                           ArrayList<String> frequencyarr) {
            this.context = context;
            this.frequencyarr = frequencyarr;
        }
        private class ViewHolder {
            TextView text,startTimeTxt;
            LinearLayout startLayout;
        }
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_frequency_reminder_item,
                        null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView
                        .findViewById(R.id.text);
                holder.startTimeTxt =(TextView)convertView.findViewById(R.id.startTimeTxt);
                holder.startLayout =(LinearLayout)convertView.findViewById(R.id.startLayout);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(frequencyarr.get(position));
            holder.text.setTag(position);
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    strat_time_boolean = false;
                    time_list_position = (Integer) view.getTag();
                    showDialog(TIME_DIALOG_ID);
                }
            });
            holder.startTimeTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    strat_time_boolean = true;
                    showDialog(TIME_DIALOG_ID);
                }
            });
            if (position==0)
            {
                holder.startLayout.setVisibility(View.VISIBLE);
            }else
            {
                holder.startLayout.setVisibility(View.GONE);
            }
            return convertView;
        }
        @Override
        public int getCount() {
            return frequencyarr.size();
        }
        @Override
        public Object getItem(int position) {
            return frequencyarr.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }
    private class BoneMarrowAdapter extends BaseAdapter {
        Context context;
        String frequencyarr[];

        public BoneMarrowAdapter(Context context,
                                 String frequencyarr[]) {
            this.context = context;
            this.frequencyarr = frequencyarr;
        }
        private class ViewHolder {
            TextView text;
        }
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_frequency_reminder_item,
                        null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView
                        .findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(frequencyarr[position]);
            return convertView;
        }

        @Override
        public int getCount() {
            return frequencyarr.length;
        }
        @Override
        public Object getItem(int position) {
            return frequencyarr[position];
        }
        @Override
        public long getItemId(int position) {
            return position;

        }
    }


}
