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

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.TreatmentMedicineInfoWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AttachOtherTreatment extends AppCompatActivity {
    @Bind(R.id.startDateBtn)
    Button startDateBtn;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    private final int DATE_DIALOG_ID = 1;
    @Bind(R.id.edit_name)
    EditText editName;
    private int year;
    private int month;
    private int day;
    private Toolbar toolbar;
    TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper;
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attech_other_treatment_layout);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
//        You haven't saved data yet. Do you really want to cancel the process

        toolbar.setNavigationIcon(R.mipmap.icn_back);
        getSupportActionBar().setTitle("Attach Treatment");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c.getTime());
        startDateBtn.setText(formattedDate);
        try {
            treatmentMedicineInfoWrapper = (TreatmentMedicineInfoWrapper) getIntent().getExtras().getSerializable("model");
            notesEdt.setEnabled(false);
            startDateBtn.setEnabled(false);
            saveBtn.setVisibility(View.GONE);

            notesEdt.setText(treatmentMedicineInfoWrapper.getMedicinename());
            startDateBtn.setText(treatmentMedicineInfoWrapper.getDays());
            editName.setText(treatmentMedicineInfoWrapper.getDosage());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        notesEdt.addTextChangedListener(textWatcher);
        startDateBtn.addTextChangedListener(textWatcher);
        editName.addTextChangedListener(textWatcher);
         mHomeWatcher = new HomeWatcher(AttachOtherTreatment.this);
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
    @OnClick(R.id.startDateBtn)
    public void startDateBtn() {

        showDialog(DATE_DIALOG_ID);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityFound) {
            new UpdateDataDialog(AttachOtherTreatment.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @Override
    public void onBackPressed() {

        if (dataChanged&&(!TextUtils.isEmpty(editName.getText().toString()) || !TextUtils.isEmpty(notesEdt.getText().toString()))) {
            saveAlert();
        } else {

            finish();
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
    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AttachOtherTreatment.this);
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

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
         if (TextUtils.isEmpty(startDateBtn.getText().toString())) {
            startDateBtn.setError("Please Enter Date");
            startDateBtn.requestFocus();
        } else {

            TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper = new TreatmentMedicineInfoWrapper();
//            treatmentMedicineInfoWrapper.setCyclenumber(cyclenumberEdt.getText().toString());
            treatmentMedicineInfoWrapper.setDays("Other Treatment");
            treatmentMedicineInfoWrapper.setDosage(editName.getText().toString());
            treatmentMedicineInfoWrapper.setMedicinename(notesEdt.getText().toString());
            treatmentMedicineInfoWrapper.setType("T");
            DataManager.getInstance().treatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
             DataManager.getInstance().updatetreatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
            notesEdt.setText("");
            editName.setText("");
            MyApplication.getApplication().hideSoftKeyBoard(AttachOtherTreatment.this);
            finish();
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
            String dayString = day + "", monthString = (month + 1) + "";

            if ((month + 1) < 10) {
                monthString = "0" + (month + 1);
            }
            if (day < 10) {
                dayString = "0" + day;
            }

            String dateString = monthString + "-" + dayString + "-" + year;
            // set selected date into textview
            startDateBtn.setText(dateString);


        }
    };
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
}
