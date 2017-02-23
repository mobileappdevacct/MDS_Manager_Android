package com.mymdsmanager.activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.AppointmentsWrapper;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class AddAppointmentsActivity extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.addProviderBtn)
    Button addProviderBtn;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    @Bind(R.id.dateAndTimeBtn)
    Button dateAndTimeBtn;
    @Bind(R.id.providerBtn)
    Button providerBtn;
    @Bind(R.id.exportToCalBtn)
    Button exportToCalBtn;
    @Bind(R.id.addProviderLl)
    LinearLayout addProviderLl;
    private Toolbar toolbar;
    String title = "";
    private final int DATE_DIALOG_ID = 1;
    String dateString = "", timeString = "",dateString1 = "";
    private int year;
    private int month;
    private int day;
    private DBAdapter dbAdapter;
    private int id;
    private String type;
    private final int TIME_DIALOG_ID = 2;
    long timeInMillies;
    Calendar cal;
    AppointmentsWrapper wrapper = null;
    HomeWatcher mHomeWatcher ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointments);
        ButterKnife.bind(this);
        cal = Calendar.getInstance();
        id = getIntent().getIntExtra("id", -1);
        dbAdapter = new DBAdapter(AddAppointmentsActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (id == -1) {
            getSupportActionBar().setTitle("Add Appointments");
            saveBtn.setText("Save");
        } else {
            setData();
            getSupportActionBar().setTitle("Add Appointments");
            saveBtn.setText("Save");
        }
        mHomeWatcher = new HomeWatcher(AddAppointmentsActivity.this);

        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...
                isActivityFound=true;
            }
            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
    }
    boolean isActivityFound = false;
    @Override
    protected void onResume() {
        super.onResume();
        if (wrapper == null) {
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            String formattedDate = df.format(c.getTime());
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
            String time = sdfs.format(c.getTime());
            new UpdateDataDialog(AddAppointmentsActivity.this, R.style.Dialog).dismiss();
            mHomeWatcher.startWatch();
            if (isActivityFound) {
                new UpdateDataDialog(AddAppointmentsActivity.this, R.style.Dialog);
                isActivityFound = false;
            }

        }

    }

    @Override
    public void onBackPressed() {

        if (!TextUtils.isEmpty(dateAndTimeBtn.getText().toString()) || !TextUtils.isEmpty(providerBtn.getText().toString())) {
//            if ()
            if (wrapper!=null) {
                String dateNTime = wrapper.getDateNtime();
                String arr[] = dateNTime.split("###");
                dateString = arr[0];
                timeString = arr[1];
                String dateTime = arr[0] + " " + arr[1];

                if (!dateTime.equalsIgnoreCase(dateAndTimeBtn.getText().toString()) || !wrapper.getNotes().equalsIgnoreCase(notesEdt.getText().toString()) || !wrapper.getProvider().equalsIgnoreCase(providerBtn.getText().toString())) {
                    saveAlert();
                } else {
                    finish();
                }
            }else
            {
                saveAlert();
            }

        } else {
            finish();
        }

    }

    private void saveData() {
        try {
            dbAdapter.openMdsDB();
            wrapper = dbAdapter.getAppointmentsWrapper(id);
            String dateNTime = wrapper.getDateNtime();
            String arr[] = dateNTime.split("###");
            dateString = updateformateDate(arr[0]);
            formateDate(dateString);
            timeString = arr[1];
            dateAndTimeBtn.setText(arr[0] + " " + arr[1]);
            dateString1= formateDateupdate(arr[0]);
            providerBtn.setText(wrapper.getProvider());
            notesEdt.setText(wrapper.getNotes());
            dbAdapter.closeMdsDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.providerBtn)
    public void providerBtn() {
        showPopUp("Select Professional", providerBtn);
    }
    @OnClick(R.id.addProviderLl)
    public void addProviderLl() {
        showPopUp("Select Professional", providerBtn);
    }
    @OnClick(R.id.exportToCalBtn)
    public void exportToCalBtn() {
        if (!TextUtils.isEmpty(dateString)) {
            exportToCal(providerBtn.getText().toString(), notesEdt.getText().toString(), cal.getTimeInMillis());
        }
    }
    private void exportToCal(String title, String notes, long millie) {
        try {
            final ContentValues event = new ContentValues();
            event.put(CalendarContract.Events.CALENDAR_ID, 1);
            event.put(CalendarContract.Events.TITLE, "MyMdsManager : " + title);
            event.put(CalendarContract.Events.DESCRIPTION, notes);
            event.put(CalendarContract.Events.EVENT_LOCATION, "");
            event.put(CalendarContract.Events.DTSTART, millie);
            event.put(CalendarContract.Events.DTEND, millie + 3600000);
            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
            Uri baseUri;
            if (Build.VERSION.SDK_INT >= 8) {
                baseUri = Uri.parse("content://com.android.calendar/events");
            } else {
                baseUri = Uri.parse("content://calendar/events");
            }
            getContentResolver().insert(baseUri, event);
            Toast.makeText(AddAppointmentsActivity.this, "Event successfully exported", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopUp(final String title, final Button button) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        dbAdapter.openMdsDB();

        ArrayList<MedicalProfessionalWrapper> wrapperList = dbAdapter.getMedicalProfessionalWrapper();

        final ArrayList<String> professionalList = new ArrayList<>();
        for (int i = 0; i < wrapperList.size(); i++) {
            professionalList.add(wrapperList.get(i).getProvidername());
        }


        dbAdapter.closeMdsDB();
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, professionalList);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        if (professionalList.isEmpty()) {
            startActivity(new Intent(AddAppointmentsActivity.this, AddMedicalProfessionalActivity.class));
        }
        else {
            dialog.show();
        }
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                button.setText(professionalList.get(position));
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            mHomeWatcher.stopWatch();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @OnClick(R.id.addProviderBtn)
    public void addProviderBtn() {
        startActivity(new Intent(AddAppointmentsActivity.this, AddMedicalProfessionalActivity.class));
    }

    @OnClick(R.id.dateAndTimeBtn)
    public void dateAndTimeBtn() {

        showDialog(DATE_DIALOG_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date

                return new DatePickerDialog(this, datePickerListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            case TIME_DIALOG_ID:
                // set date picker as current date

                return new TimePickerDialog(this, timePickerListener, c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE), false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {


        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            String dayString = day + "", monthString = (month + 1) + "";

            if ((month + 1) < 10) {
                monthString = "0" + (month + 1);
            }
            if (day < 10) {
                dayString = "0" + day;
            }
            dateString = monthString + "-" + dayString + "-" + year;
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.YEAR, year);
            dateString1=formateDate1(dateString);
            dateAndTimeBtn.setText(formateDate(dateString));
            showDialog(TIME_DIALOG_ID);


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hourString = hourOfDay + "", minuteString = minute + "";

            if (hourOfDay < 10) {
                hourString = "0" + hourOfDay;
            }
            if (minute < 10) {
                minuteString = "0" + minute;
            }
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);

            timeString = formateTime(hourString + "-" + minuteString);
            dateAndTimeBtn.setText(dateAndTimeBtn.getText().toString() + " " + timeString);

        }
    };

  String matchDate="";
    private String formateDate(String dateString) {
        try {

            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
            Date date = originalFormat.parse(dateString);

            timeInMillies = date.getTime();
            String formattedDate = targetFormat.format(date);
            matchDate=formattedDate;
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    private String updateformateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
            Date date = targetFormat.parse(dateString);
            String formattedDate = originalFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    private String formateDate1(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM yyyy");
            Date date = originalFormat.parse(dateString);
            timeInMillies = date.getTime();
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    private String formateDateupdate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM yyyy");
            Date date = originalFormat.parse(dateString);
            timeInMillies = date.getTime();
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

    private void setData() {
        saveData();
    }
    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        if (TextUtils.isEmpty(dateAndTimeBtn.getText().toString())) {
            Toast.makeText(AddAppointmentsActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {
            Calendar c = Calendar.getInstance();
            Date currentDate = null,selectDate = null;
            SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
            String formattedDate = df.format(c.getTime());
            try {
                currentDate=df.parse(formattedDate);
                selectDate=df.parse(matchDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
             try
             {
            if (!selectDate.before(currentDate)) {
                dbAdapter.openMdsDB();
                AppointmentsWrapper wrapper = new AppointmentsWrapper();
                wrapper.setDatetimestamp(dateString);
                wrapper.setNotes(notesEdt.getText().toString());
                wrapper.setProvider(providerBtn.getText().toString());
                wrapper.setDateNtime(formateDate(dateString) + "###" + timeString);
                wrapper.setSearch_date(dateString1);
                if (id == -1) {
                    dbAdapter.saveAppoinementsData(wrapper);
                    MyApplication.saveLocalData(true);
//                Intent service_intent = new Intent(AddAppointmentsActivity.this,DataUploadService.class);
//                startService(service_intent);
                } else {
                    dbAdapter.updateAppointmentsData(wrapper, id);
                    MyApplication.saveLocalData(true);
//                Intent service_intent = new Intent(AddAppointmentsActivity.this,DataUploadService.class);
//                startService(service_intent);
                }
                dbAdapter.closeMdsDB();
                new UpdateOnClass(MyApplication.getApplication(),this);
                finish();
            }else
            {
                Toast.makeText(AddAppointmentsActivity.this, "Please select correct date", Toast.LENGTH_LONG).show();
            }
             }catch (Exception ex)
             {
                 AppointmentsWrapper wrapper = new AppointmentsWrapper();
                 wrapper.setDatetimestamp(dateString);
                 wrapper.setNotes(notesEdt.getText().toString());
                 wrapper.setProvider(providerBtn.getText().toString());
                 wrapper.setDateNtime(formateDate(dateString) + "###" + timeString);
                 wrapper.setSearch_date(dateString1);
                 dbAdapter.openMdsDB();
                 if (id == -1) {
                     dbAdapter.saveAppoinementsData(wrapper);
                     MyApplication.saveLocalData(true);
//                Intent service_intent = new Intent(AddAppointmentsActivity.this,DataUploadService.class);
//                startService(service_intent);
                 } else {
                     dbAdapter.updateAppointmentsData(wrapper, id);
                     MyApplication.saveLocalData(true);
//                Intent service_intent = new Intent(AddAppointmentsActivity.this,DataUploadService.class);
//                startService(service_intent);

                 }
                 dbAdapter.closeMdsDB();
                 new UpdateOnClass(MyApplication.getApplication(),this);
                 finish();
                 ex.printStackTrace();
             }

        }
    }

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAppointmentsActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();


                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    public void onFinish() {
//        finish();
    }
}
