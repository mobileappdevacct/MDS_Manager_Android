package com.mymdsmanager.activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.NotesWrapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class AddNotesActivity extends ActionBarActivity implements OnFinishActivity{
    @Bind(R.id.DateBtn)
    Button DateBtn;
    @Bind(R.id.topicEdt)
    EditText topicEdt;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    private int id = -1;
    DBAdapter dbAdapter;
    private Toolbar toolbar;
    private final int DATE_DIALOG_ID = 1;
    private final int TIME_DIALOG_ID = 2;
    String timeString = "";
    private int year;
    private int month;
    private int day;
    Calendar cal;
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        ButterKnife.bind(this);
        cal = Calendar.getInstance();
        dbAdapter = new DBAdapter(AddNotesActivity.this);
        id = getIntent().getIntExtra("id", -1);
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
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            String formattedDate = df.format(c.getTime());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int mints = c.get(Calendar.MINUTE);
            String time = "" + hour + "-" + mints;
            DateBtn.setText(formattedDate + " " + formateTime(time));
            getSupportActionBar().setTitle("Add Note");
            saveBtn.setText("Save Note");
        } else {
            setData();
            saveBtn.setText("Save Note");
            getSupportActionBar().setTitle("Add Note");
        }

//        warningPopUp();
//        notesEdt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    saveData();
//                }
//                return false;
//            }
//        });
        mHomeWatcher = new HomeWatcher(AddNotesActivity.this);
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
        DateBtn.addTextChangedListener(textWatcher);
        topicEdt.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
    }
    boolean isActivityFound = false;
    @Override
    protected void onResume() {
        super.onResume();
        try {
            mHomeWatcher.startWatch();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (isActivityFound) {
            new UpdateDataDialog(AddNotesActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
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

    private void saveData() {
        if (TextUtils.isEmpty(DateBtn.getText().toString()) || TextUtils.isEmpty(notesEdt.getText().toString())) {
            Toast.makeText(AddNotesActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {
            dbAdapter.openMdsDB();
            NotesWrapper wrapper = new NotesWrapper();
            wrapper.setDate(DateBtn.getText().toString());
            wrapper.setNotes(notesEdt.getText().toString());
            wrapper.setTopic(topicEdt.getText().toString());
            if (id == -1) {
                dbAdapter.saveNotesData(wrapper);
                MyApplication.saveLocalData(true);
            } else {
                dbAdapter.updateNotesData(wrapper, id);
                MyApplication.saveLocalData(true);
            }
            dbAdapter.closeMdsDB();
//            Intent service_intent = new Intent(AddNotesActivity.this, DataUploadService.class);
//            startService(service_intent);
//            getUpdateDataOn();
//            new UpdateOnData().execute();

            new UpdateOnClass(MyApplication.getApplication(),this);


            finish();
        }
    }

    @OnClick(R.id.DateBtn)
    public void DateBtn() {
        showDialog(DATE_DIALOG_ID);
    }

    @Override
    public void onBackPressed() {
        if (dataChanged) {
            saveAlert();
        } else {

            finish();
        }

    }

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNotesActivity.this);
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

            String dateString = monthString + "-" + dayString + "-" + year;
            // set selected date into textview
            DateBtn.setText(formateDate(dateString));
            showDialog(TIME_DIALOG_ID);


        }
    };

    private void setData() {

        dbAdapter.openMdsDB();

        NotesWrapper wrapper = dbAdapter.getNotesWrapper(id);


        DateBtn.setText(wrapper.getDate());
        topicEdt.append(wrapper.getTopic());

        notesEdt.append(wrapper.getNotes());

        dbAdapter.closeMdsDB();
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        saveData();
    }

    private String formateDate(String dateString) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(dateString);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

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

            timeString = hourString + "-" + minuteString;
            DateBtn.setText(DateBtn.getText().toString() + " " + formateTime(timeString));

        }
    };

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
    public void onFinish() {

//        finish();
    }
    boolean dataChanged;
    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            dataChanged = true;

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            dataChanged = true;
        }
    };

}
