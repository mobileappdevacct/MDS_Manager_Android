package com.mymdsmanager.activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.AddIpssContactWrapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class AddIpssContactActivity extends AppCompatActivity implements OnFinishActivity {
    Button dateBtn, addBtn;
    EditText notesEdt, scoreEdit;
    private Toolbar toolbar;
    private final int DATE_DIALOG_ID = 1;
    private int year;
    private int month;
    private int day;
    private DBAdapter dbAdapter;
    String id = "-1";
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ipss_contact_layout);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        notesEdt = (EditText) findViewById(R.id.notesEdt);
        scoreEdit = (EditText) findViewById(R.id.scoreEdit);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        dbAdapter = new DBAdapter(AddIpssContactActivity.this);
        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        id = getIntent().getStringExtra("id");
        if (id.equalsIgnoreCase("-1")) {
            getSupportActionBar().setTitle("Add Score");
            addBtn.setText("ADD");
        } else {
            getSupportActionBar().setTitle("Add Score");
            addBtn.setText("ADD");
            setData();
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c.getTime());
        dateBtn.setText(formateDate(formattedDate));
         mHomeWatcher = new HomeWatcher(AddIpssContactActivity.this);
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
        dateBtn.addTextChangedListener(textWatcher);
        scoreEdit.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
    }
    boolean isActivityFound = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityFound) {
            new UpdateDataDialog(AddIpssContactActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date

                return new DatePickerDialog(this, datePickerListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


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
//            if (isStartDateSelected)
            // set selected date into textview
            String dayString = day + "", monthString = (month + 1) + "";

            if ((month + 1) < 10) {
                monthString = "0" + (month + 1);
            }
            if (day < 10) {
                dayString = "0" + day;
            }

            String dateString = monthString + "-" + dayString + "-" + year;
            dateBtn.setText(formateDate(dateString));
//            else
//                endDateBtn.setText(new StringBuilder().append(month + 1)
//                        .append("-").append(day).append("-").append(year)
//                        .append(" "));

        }
    };

    private void saveData() {
        if (TextUtils.isEmpty(dateBtn.getText().toString()) || TextUtils.isEmpty(scoreEdit.getText().toString())) {
            Toast.makeText(AddIpssContactActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {
            dbAdapter.openMdsDB();
            AddIpssContactWrapper wrapper = new AddIpssContactWrapper();

            wrapper.setDate(dateBtn.getText().toString());
            wrapper.setIpss_score(scoreEdit.getText().toString());
            wrapper.setNotes(notesEdt.getText().toString());


            if (id.equalsIgnoreCase("-1")) {


                dbAdapter.saveIPSSScore(wrapper);
                MyApplication.saveLocalData(true);

            } else {
                dbAdapter.updateIPSSScore(wrapper, Integer.parseInt(id));
                MyApplication.saveLocalData(true);
            }


            dbAdapter.closeMdsDB();
//
//            Intent service_intent = new Intent(AddIpssContactActivity.this,DataUploadService.class);
//            startService(service_intent);
            new UpdateOnClass(MyApplication.getApplication(),this);
            finish();
        }
    }

    private void setData() {

        AddIpssContactWrapper wrapper = DataManager.getInstance().getAddIpssContactWrapper();
        dateBtn.setText(wrapper.getDate());
        scoreEdit.append(wrapper.getIpss_score());
        notesEdt.append(wrapper.getNotes());




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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddIpssContactActivity.this);
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

    @Override
    public void onFinish() {
//        finish();

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
