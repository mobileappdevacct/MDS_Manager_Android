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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.Transfusionwrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddTranfusionDiagnosis extends AppCompatActivity implements OnFinishActivity {
    @Bind(R.id.DateBtn)
    Button DateBtn;
    @Bind(R.id.diagnosisTxt)
    TextView diagnosisTxt;
    @Bind(R.id.diagnosisSpinner)
    Spinner diagnosisSpinner;
    @Bind(R.id.unitEdt)
    EditText unitEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    String title = "";
    int pos;
    private final int DATE_DIALOG_ID = 1;
    @Bind(R.id.bloodTypeBtn)
    Button bloodTypeBtn;
    @Bind(R.id.bloodGroupTxt)
    TextView bloodGroupTxt;
    private int year;
    private int month;
    private int day;
    private Toolbar toolbar;
    String diagnosis_str = "";
    private DBAdapter dbAdapter;
    ArrayList<String> add_diagnosis = new ArrayList<>();
    String bloodTypeString;
    private ArrayList<String> bloodGroupArr = new ArrayList<String>();
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transfusion_diagnosis_layout);
        ButterKnife.bind(this);
//        title = getIntent().getStringExtra("title");
        pos = getIntent().getIntExtra("pos", 0);
        add_diagnosis.add("Select type");
        add_diagnosis.add("PRBCs");
        add_diagnosis.add("Platelets");
        bloodTypeString = MyApplication.blood_typeStr;
        bloodTypeBtn.setText(bloodTypeString);
        ArrayAdapter<String> diagnosisSAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, add_diagnosis);
        diagnosisSpinner.setAdapter(diagnosisSAdapter);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        dbAdapter = new DBAdapter(AddTranfusionDiagnosis.this);
        if (pos == -1) {
            getSupportActionBar().setTitle("Add Transfusion");
            saveBtn.setText("Save");
        } else {
            setData();
            getSupportActionBar().setTitle("Update Transfusion");
            saveBtn.setText("Save");
        }

        toolbar.setNavigationIcon(R.mipmap.icn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        diagnosisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!diagnosisSpinner.getSelectedItem().toString().equalsIgnoreCase("Select type")) {
                    diagnosis_str = diagnosisSpinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bloodGroupArr.add("A+");
        bloodGroupArr.add("A-");
        bloodGroupArr.add("B+");
        bloodGroupArr.add("B-");
        bloodGroupArr.add("AB+");
        bloodGroupArr.add("AB-");
        bloodGroupArr.add("O+");
        bloodGroupArr.add("O-");
        bloodGroupArr.add("Other");
        mHomeWatcher = new HomeWatcher(AddTranfusionDiagnosis.this);
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
        bloodTypeBtn.addTextChangedListener(textWatcher);
        DateBtn.addTextChangedListener(textWatcher);
        unitEdt.addTextChangedListener(textWatcher);
    }

    boolean isActivityFound = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityFound) {
            new UpdateDataDialog(AddTranfusionDiagnosis.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @OnClick(R.id.bloodTypeBtn)
    public void bloodTypeBtn() {
        showPopUp("", bloodGroupArr);
    }

    private void showPopUp(final String title, final ArrayList<String> arr) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arr);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                bloodTypeString = arr.get(position);
                MyApplication.blood_typeStr = bloodTypeString;
                bloodTypeBtn.setText(bloodTypeString);
//                    if (bloodTypeString.equalsIgnoreCase("Others")) {
//                        showAddOthersDialog(SELECT_BLOOD_GORUP);
//                    }
            }
        });
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        if (TextUtils.isEmpty(bloodTypeBtn.getText().toString())) {
            bloodTypeBtn.setError("Please select blood type");
            bloodTypeBtn.requestFocus();
        } else if (TextUtils.isEmpty(DateBtn.getText().toString())) {
            DateBtn.setError("Please enter date");
            DateBtn.requestFocus();
        }else {
            dbAdapter.openMdsDB();
            Transfusionwrapper transfusionwrapper = new Transfusionwrapper();
            transfusionwrapper.setUnit(unitEdt.getText().toString());
            transfusionwrapper.setTtype(diagnosis_str);
            transfusionwrapper.setDate(DateBtn.getText().toString());
            transfusionwrapper.setBlood_type(bloodTypeBtn.getText().toString());

            if (pos == -1) {
                dbAdapter.saveTransfusionData(transfusionwrapper);
                MyApplication.saveLocalData(true);
            } else {
                dbAdapter.updateTransfusion(transfusionwrapper, Integer.parseInt(transfusionwrapper1.getId()));
                MyApplication.saveLocalData(true);
            }
            dbAdapter.closeMdsDB();
//            Intent service_intent = new Intent(AddTranfusionDiagnosis.this,DataUploadService.class);
//            startService(service_intent);
            new UpdateOnClass(MyApplication.getApplication(), this);
            finish();
        }
    }

    @OnClick(R.id.DateBtn)
    public void DateBtn() {

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
            // set selected date into textview
            String dayString = day + "", monthString = (month + 1) + "";

            if ((month + 1) < 10) {
                monthString = "0" + (month + 1);
            }
            if (day < 10) {
                dayString = "0" + day;
            }

            String dateString = monthString + "-" + dayString + "-" + year;
            DateBtn.setText(formateDate(dateString));


        }
    };

    @Override
    public void onBackPressed() {
        if (transfusionwrapper1!=null) {
            if (!transfusionwrapper1.getTtype().equalsIgnoreCase(diagnosis_str)) {
                dataChanged = true;
            }
        }
        if (dataChanged ||(!TextUtils.isEmpty(unitEdt.getText().toString()) || !TextUtils.isEmpty(DateBtn.getText().toString()) || !TextUtils.isEmpty(diagnosis_str))) {
            saveAlert();
        } else {

            finish();
        }
    }

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTranfusionDiagnosis.this);
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
        Date date = null;
        String dateStr = null;
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            date = originalFormat.parse(dateString);

            dateStr = targetFormat.format(date);
            return dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    private String formategetDate(String dateString) {
        Date date = null;
        String dateStr = null;
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            date = targetFormat.parse(dateString);

            dateStr = originalFormat.format(date);
            return dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    Transfusionwrapper transfusionwrapper1;

    public void setData() {
//        dbAdapter.openMdsDB();
//        ArrayList<Transfusionwrapper> list = dbAdapter.getTransfusion();
//        Collections.reverse(list);
        transfusionwrapper1 = (Transfusionwrapper) getIntent().getSerializableExtra("model");
        bloodTypeBtn.setText(transfusionwrapper1.getBlood_type());
        DateBtn.setText(transfusionwrapper1.getDate());
        unitEdt.append(transfusionwrapper1.getUnit());


        diagnosisSpinner.setSelection(add_diagnosis.indexOf(transfusionwrapper1.getTtype()));
//        dbAdapter.closeMdsDB();
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
        } catch (Exception ex) {
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
