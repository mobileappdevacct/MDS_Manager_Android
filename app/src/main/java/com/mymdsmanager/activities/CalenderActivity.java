package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.adapter.CalendarAdapter;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.AppointmentsWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalenderActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.addAppointmentsImgBtn)
    ImageButton addAppointmentsImgBtn;
    @Bind(R.id.previous_month)
    TextView previousMonth;
    @Bind(R.id.next_month)
    TextView nextMonth;
    @Bind(R.id.current_month)
    TextView currentMonth;
    @Bind(R.id.calendar_header)
    RelativeLayout calendarHeader;
    @Bind(R.id.calendar_days_grid)
    GridView calendarDaysGrid;
    @Bind(R.id.calendar_grid)
    GridView calendarGrid;
    @Bind(R.id.calendar_switcher)
    ViewSwitcher calendarSwitcher;
    @Bind(R.id.listAllText)
    TextView listAllText;
    @Bind(R.id.appointmentsBackTxt)
    TextView appointmentsBackTxt;
    @Bind(R.id.currentDateText)
    TextView currentDateText;
    @Bind(R.id.appointmentsListView)
    ListView appointmentsListView;
    private String monthYearDb = "";
    private Activity activity;
    // Calendar instances
    private Calendar calendar = Calendar.getInstance();
    private static Calendar calendarCurrent = Calendar.getInstance();

    private final Locale locale = Locale.getDefault();
    private CalendarAdapter calendarAdapter;
    public static long DATE_OF_PERIOD = 0;
    private boolean istxtPeriodStartedButtonClicked = false;
    private Toolbar toolbar;
    private DBAdapter dbAdapter;
    ArrayList<AppointmentsWrapper> notesList;
    boolean setnotyfy = true;
    HomeWatcher mHomeWatcher;

    @OnClick(R.id.appointmentsBackTxt)
    public void appointmentsBackTxt() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calender);
        ButterKnife.bind(this);


        getUiComponent();
        mHomeWatcher = new HomeWatcher(CalenderActivity.this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                isActivityFound = true;
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();

    }

    boolean isActivityFound = false;

    private void getUiComponent() {
        dbAdapter = new DBAdapter(CalenderActivity.this);


        istxtPeriodStartedButtonClicked = false;


    }

    @OnClick({R.id.next_month, R.id.previous_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_month:
                onNextMonth();
                break;
            case R.id.previous_month:
                onPreviousMonth();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(CalenderActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dbAdapter.openMdsDB();
        GridView calendarDayGrid = (GridView) findViewById(R.id.calendar_days_grid);
        final GestureDetector swipeDetector = new GestureDetector(
                CalenderActivity.this, new SwipeGesture(CalenderActivity.this));

        calendarAdapter = new CalendarAdapter(CalenderActivity.this, calendar, dbAdapter.getAppointmentDatesList());
        updateCurrentMonth();
        dbAdapter.closeMdsDB();

        calendarGrid.setOnItemClickListener(new DayItemClickListener());

        calendarGrid.setAdapter(calendarAdapter);
        calendarGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return swipeDetector.onTouchEvent(event);
            }
        });


        calendarDayGrid.setAdapter(new ArrayAdapter<String>(CalenderActivity.this, R.layout.item_day, getResources().getStringArray(R.array.days_array)));
        dbAdapter.openMdsDB();
        notesList = dbAdapter.getAppointmentList(currentMonth.getText().toString());
        BoneMarrowAdapter adapter = new BoneMarrowAdapter(CalenderActivity.this, notesList);
        appointmentsListView.setAdapter(adapter);
        dbAdapter.closeMdsDB();
        updateCurrentMonth();
        notifyAlret();

    }

    @Override
    public void onBackPressed() {

//       Intent service = new Intent(CalenderActivity.this,NotifyService.class);
//        startService(service);
        finish();
    }


    @OnClick(R.id.listAllText)
    public void listAllText() {
        currentDateText.setText("All Appointments");
        listAllText.setVisibility(View.GONE);
        appointmentsListView.setVisibility(View.VISIBLE);
        dbAdapter.openMdsDB();
        BoneMarrowAdapter adapter = new BoneMarrowAdapter(CalenderActivity.this, dbAdapter.getAppointmentList(""));
        appointmentsListView.setAdapter(adapter);
        dbAdapter.closeMdsDB();

    }

    // get day of month from its long value
    private int[] getDate(long milliSeconds) {
        int[] date_ = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        date_[0] = calendar.get(Calendar.DAY_OF_MONTH);
        date_[1] = (calendar.get(Calendar.MONTH) + 1);
        date_[2] = calendar.get(Calendar.YEAR);
        return date_;
    }

    private long getMillis(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        long millis = 0;
        try {
            Date selectedDate = sdf.parse(date);
            millis = selectedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }


    private String formateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
            Date date = originalFormat.parse(dateString);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 55) {
            switch (resultCode) {
                case MainActivity.RESULT_OK:
                    /** Refresh all views after event page done click */
                    refereshAllViews();
                    break;

                default:
                    break;
            }
        }
    }

    @OnClick(R.id.addAppointmentsImgBtn)
    public void addAppointmentsImgBtn() {

        startActivity(new Intent(CalenderActivity.this, AddAppointmentsActivity.class));
    }

    private void refereshAllViews() {
        // getMonthData("_" + (calendar.get(Calendar.MONTH) + 1) + "_"
        // + calendar.get(Calendar.YEAR));

        updateCurrentMonth();
        calendarAdapter.notifyDataSetChanged();
        // String[] dateArr = dateString.split("\\|");
        // String tag = dateArr[0];
        // selectedDayTask(tag);
    }

    private void changeLastDayPeriod() {
        // getMonthData("_" + (calendar.get(Calendar.MONTH) + 1) + "_"
        // + calendar.get(Calendar.YEAR));
        updateCurrentMonth();
        calendarAdapter.notifyDataSetChanged();
    }

    // Calendar methods
    protected void updateCurrentMonth() {

        calendarAdapter.refreshDays(monthYearDb);
        currentMonth.setText(String.format(locale, "%tB", calendar) + " "
                + calendar.get(Calendar.YEAR));

        System.out.println("calender>>>>"
                + String.format(locale, "%tB", calendar) + " "
                + calendar.get(Calendar.YEAR) + ">>>"
                + calendar.get(Calendar.MONTH));
//        current_month.setText(
//                String.format(String.format(locale, "%tB", calendar)));
    }

    @Override
    public void onFinish() {

    }

    private final class DayItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final TextView dayView = (TextView) view
                    .findViewById(R.id.date);
            listAllText.setVisibility(View.VISIBLE);
            currentDateText.setVisibility(View.VISIBLE);
            final String text = dayView.getText().toString();
            if (text != null && !"".equals(text)) {

                calendarAdapter.setSelected(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        Integer.valueOf(String.valueOf(text)));

                Calendar cal_ = Calendar.getInstance();
                cal_.set(calendar.get(Calendar.YEAR),
                        (calendar.get(Calendar.MONTH)), Integer.parseInt(text));
                currentDateText.setText(
                        String.format(text + " " + String.format(locale, "%tB", calendar) + " "
                                + calendar.get(Calendar.YEAR)));

                int day = Integer.parseInt(text);
                int month = calendar.get(Calendar.MONTH);
                String dayString = day + "", monthString = (month + 1) + "";

                if ((month + 1) < 10) {
                    monthString = "0" + (month + 1);
                }
                if (day < 10) {
                    dayString = "0" + day;
                }
//                dateString = monthString + "-" + dayString + "-" + year;
                String dateString = monthString + "-" + dayString + "-" + calendar.get(Calendar.YEAR);
                dbAdapter.openMdsDB();
                BoneMarrowAdapter adapter = new BoneMarrowAdapter(CalenderActivity.this, dbAdapter.getAppointmentListPerTicurDate(dateString));
                appointmentsListView.setAdapter(adapter);
                dbAdapter.closeMdsDB();
            }
        }
    }


    protected final void onNextMonth() {

        calendarSwitcher.setInAnimation(CalenderActivity.this, R.anim.in_from_right);
        calendarSwitcher.setOutAnimation(CalenderActivity.this, R.anim.out_to_left);
        calendarSwitcher.showNext();

        if (calendarCurrent.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendarCurrent.set((calendarCurrent.get(Calendar.YEAR) + 1),
                    Calendar.JANUARY, 1);
        } else {
            calendarCurrent.set(Calendar.MONTH,
                    calendarCurrent.get(Calendar.MONTH) + 1);
        }

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set((calendar.get(Calendar.YEAR) + 1), Calendar.JANUARY, 1);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }
        // dateOfPeriod = 0;

        String[] param = {"_" + (calendar.get(Calendar.MONTH) + 1) + "_"
                + calendar.get(Calendar.YEAR)};
        System.out.println("param>>>>" + param[0]);

        // new LastMonthsTasks().execute(param);
        updateCurrentMonth();
        dbAdapter.openMdsDB();
        BoneMarrowAdapter adapter = new BoneMarrowAdapter(CalenderActivity.this, dbAdapter.getAppointmentList(currentMonth.getText().toString()));
        appointmentsListView.setAdapter(adapter);
        dbAdapter.closeMdsDB();
        currentDateText.setText("All Appointments");

    }

    private class BoneMarrowAdapter extends BaseAdapter {
        Context context;
        ArrayList<AppointmentsWrapper> rowItems;


        public BoneMarrowAdapter(Context context,
                                 ArrayList<AppointmentsWrapper> arrayList) {
            this.context = context;
            this.rowItems = arrayList;
        }

        /* private view holder class */
        private class ViewHolder {
            TextView dateTxt, descriptionTxt, boneBlastTxt;
            ImageView delete_btn;

        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_appointment_item,
                        null);
                holder = new ViewHolder();
                holder.dateTxt = (TextView) convertView
                        .findViewById(R.id.dateTxt);
                holder.descriptionTxt = (TextView) convertView
                        .findViewById(R.id.descriptionTxt);
                holder.boneBlastTxt = (TextView) convertView
                        .findViewById(R.id.boneBlastTxt);
                holder.delete_btn = (ImageView) convertView.findViewById(R.id.delete_btn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                String dateNTime = rowItems.get(position).getDateNtime();
                System.out.println("dateNTime" + dateNTime);
                String arr[] = dateNTime.split("###");
                holder.boneBlastTxt.setText(rowItems.get(position).getProvider());

                holder.dateTxt.setText(arr[0]);
                if (arr.length > 1)
                    holder.descriptionTxt.setText(arr[1]);


            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this item?")
                            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbAdapter.openMdsDB();
                            dbAdapter.deleteAppointment(String.valueOf(rowItems.get(position).getArowid()));
                            rowItems.remove(position);
                            new UpdateOnClass(MyApplication.getApplication(),CalenderActivity.this);
//                            notifyDataSetChanged();
//                            refereshAllViews();
                            dbAdapter.closeMdsDB();

                            Intent i = new Intent(CalenderActivity.this, CalenderActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    showCarGiverDialog(rowItems.get(position), "Update Caregiver");
                    mHomeWatcher.stopWatch();
                    startActivity(new Intent(CalenderActivity.this, AddAppointmentsActivity.class)
                            .putExtra("id", rowItems.get(position).getArowid()));
                }
            });

            return convertView;
        }

        @Override
        public int getCount() {
            return rowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems.indexOf(position);

        }
    }

    protected final void onPreviousMonth() {

        calendarSwitcher.setInAnimation(CalenderActivity.this, R.anim.in_from_left);
        calendarSwitcher.setOutAnimation(CalenderActivity.this, R.anim.out_to_right);
        calendarSwitcher.showPrevious();

        if (calendarCurrent.get(Calendar.MONTH) == Calendar.JANUARY) {
            calendarCurrent.set((calendarCurrent.get(Calendar.YEAR) - 1),
                    Calendar.DECEMBER, 1);
        } else {
            calendarCurrent.set(Calendar.MONTH,
                    calendarCurrent.get(Calendar.MONTH) - 1);
        }

        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            calendar.set((calendar.get(Calendar.YEAR) - 1), Calendar.DECEMBER,
                    1);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }
        String[] param = {"_" + (calendar.get(Calendar.MONTH) + 1) + "_"
                + calendar.get(Calendar.YEAR)};

        System.out.println("param>>>>" + param[0]);

        // dateOfPeriod = 0;
        // new LastMonthsTasks().execute(param);
        updateCurrentMonth();
        dbAdapter.openMdsDB();
        BoneMarrowAdapter adapter = new BoneMarrowAdapter(CalenderActivity.this, dbAdapter.getAppointmentList(currentMonth.getText().toString()));
        appointmentsListView.setAdapter(adapter);
        dbAdapter.closeMdsDB();
        currentDateText.setText("All Appointments");

    }


    private final class SwipeGesture extends GestureDetector.SimpleOnGestureListener {
        private final int swipeMinDistance;
        private final int swipeThresholdVelocity;

        public SwipeGesture(Context context) {
            final ViewConfiguration viewConfig = ViewConfiguration.get(context);
            swipeMinDistance = viewConfig.getScaledTouchSlop();
            swipeThresholdVelocity = viewConfig.getScaledMinimumFlingVelocity();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (e1.getX() - e2.getX() > swipeMinDistance
                    && Math.abs(velocityX) > swipeThresholdVelocity) {
                onNextMonth();
            } else if (e2.getX() - e1.getX() > swipeMinDistance
                    && Math.abs(velocityX) > swipeThresholdVelocity) {
                onPreviousMonth();
            }
            return false;
        }
    }

    //task when swipe last months
    private class LastMonthsTasks extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Calendar current = Calendar.getInstance();
            if (calendar.before(current)) {
                // Previous or current month
                // getMonthData(params[0]);
            } else {
                // future month
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>this is after month");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            updateCurrentMonth();
        }

    }

    public void notifyAlret() {


        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (notesList.size() > 0) {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
                    String formattedDate = df.format(c.getTime());
                    SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                    String time = sdfs.format(c.getTime());
                    String matchString = formattedDate + " " + time;

                    for (int i = 0; i < notesList.size(); i++) {
                        String dateNTime = notesList.get(i).getDateNtime();
                        System.out.println("dateNTime" + dateNTime);
                        String arr[] = dateNTime.split("###");
                        if (arr.length>1) {
                            if (matchString.equalsIgnoreCase(arr[0] + " " + arr[1])) {
                                if (setnotyfy) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
                                    if (TextUtils.isEmpty(notesList.get(i).getProvider())) {
                                        builder.setMessage("Today you have an appointment")
                                                .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                    }else
                                    {
                                        builder.setMessage("Today you have an appointment with " + notesList.get(i).getProvider())
                                                .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                    }


                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                setnotyfy = false;

                            }
                        }
                    }
                    if (setnotyfy) {
                        start();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mHomeWatcher.stopWatch();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
