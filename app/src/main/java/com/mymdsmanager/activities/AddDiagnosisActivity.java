package com.mymdsmanager.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.DiagnosisWrapper;
import com.mymdsmanager.wrapper.MedicalProfessionalWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDiagnosisActivity extends AppCompatActivity implements OnFinishActivity  {
    @Bind(R.id.diagnosisTxt)
    TextView diagnosisTxt;
    @Bind(R.id.diagnosisEdt)
    EditText diagnosisEdt;
    @Bind(R.id.DateBtn)
    Button DateBtn;
    @Bind(R.id.notesEdt)
    EditText notesEdt;
    @Bind(R.id.managingProviderBtn)
    Button managingProviderBtn;
    @Bind(R.id.addProviderBtn)
    Button addProviderBtn;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    @Bind(R.id.addProviderLl)
    LinearLayout addProviderLl;
    @Bind(R.id.diagnosisSpinner)
    Spinner diagnosisSpinner;
    private Toolbar toolbar;
    String title = "";
    private final int DATE_DIALOG_ID = 1;
    private int year;
    private int month;
    private int day;
    private DBAdapter dbAdapter;
    private int id;
    private String type;
    String diagnosis_str = "";
    HomeWatcher mHomeWatcher;
    boolean dataChanged;
    DiagnosisWrapper wrapper;
    boolean changediagnosis=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_diagnosis);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");

        dbAdapter = new DBAdapter(AddDiagnosisActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });



        if (DataManager.getInstance().getDiagnosis_Arraylist().size()==0)
    {
        ArrayList<String> add_diagnosis = new ArrayList<>();

        add_diagnosis.add("Serum Erythropoietin");
        add_diagnosis.add("White Blood Cell Count");
        add_diagnosis.add("Absolute Neutrophil Count");
        DataManager.getInstance().setDiagnosis_Arraylist(add_diagnosis);
    }

        if (title.contains("d")) {
            getSupportActionBar().setTitle("Add Diagnosis");
            type = "d";
            diagnosisTxt.setText("Diagnosis" +" *");
            diagnosisSpinner.setVisibility(View.VISIBLE);
            diagnosisEdt.setVisibility(View.GONE);

            ArrayAdapter<String> diagnosisSAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DataManager.getInstance().getDiagnosis_Arraylist());
            diagnosisSpinner.setAdapter(diagnosisSAdapter);
            diagnosisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!changediagnosis)
                    {
                      dataChanged=true;
                    }
                    if (!diagnosisSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Diagnosis")) {
                        diagnosis_str = diagnosisSpinner.getSelectedItem().toString();
                    }
                    changediagnosis=false;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } else {
            getSupportActionBar().setTitle("Add Surgery");
            type = "s";
            diagnosisTxt.setText("Surgery" +" *");
            diagnosisSpinner.setVisibility(View.GONE);
            diagnosisEdt.setVisibility(View.VISIBLE);
            diagnosisEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_NEXT)
                    {
                        MyApplication.getApplication().hideSoftKeyBoard(AddDiagnosisActivity.this);
                        showDialog(DATE_DIALOG_ID);
                        return true;
                    }
                    return false;
                }
            });
            diagnosisSpinner.setVisibility(View.GONE);
        }
        if (id!=-1) {
            if (title.equalsIgnoreCase("d")) {

                getSupportActionBar().setTitle("Update Diagnosis");
            } else {
                getSupportActionBar().setTitle("Update Surgery");
            }
        }

        setData();


         mHomeWatcher = new HomeWatcher(AddDiagnosisActivity.this);
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
        diagnosisTxt.addTextChangedListener(textWatcher);
        diagnosisEdt.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
    }

    @OnClick(R.id.addProviderLl)
    public void addProviderLl() {
        showPopUp("Select Professional", managingProviderBtn);


    }
    @Override
    public void onBackPressed() {
        if (title.contains("d")) {
            if ( dataChanged) {
                saveAlert();
            }else
            {

                finish();
            }
        }else
        {
            if ( dataChanged) {
                saveAlert();
            }else
            {

                finish();
            }
        }


    }


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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDiagnosisActivity.this);
        builder.setTitle("" + getResources().getString(R.string.app_name3));
        builder.setMessage("You haven't saved data yet. Do you really want to cancel the process?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                mHomeWatcher.stopWatch();
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
    boolean isActivityFound = false;
    @Override
    protected void onResume() {
        super.onResume();
        if (!DataManager.getInstance().getProvider_name().equalsIgnoreCase("")) {
            managingProviderBtn.setText(DataManager.getInstance().getProvider_name());
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(c.getTime());
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(AddDiagnosisActivity.this, R.style.Dialog);
            isActivityFound = false;
        }
//        DateBtn.setText(formattedDate);
    }

    private void saveData() {
        dbAdapter.openMdsDB();
         wrapper = dbAdapter.getDiagnosisWrapper(id);
        managingProviderBtn.setText(wrapper.getManagingprovider());
        diagnosisEdt.setText(wrapper.getDiagnosis());

        diagnosisSpinner.setSelection(DataManager.getInstance().getDiagnosis_Arraylist().indexOf(wrapper.getDiagnosis()));
        notesEdt.setText(wrapper.getNotes());
        DateBtn.setText(wrapper.getDate());
        diagnosisEdt.addTextChangedListener(textWatcher);
        notesEdt.addTextChangedListener(textWatcher);
        DateBtn.addTextChangedListener(textWatcher);
        managingProviderBtn.addTextChangedListener(textWatcher);
        dbAdapter.closeMdsDB();


    }

    @OnClick(R.id.managingProviderBtn)
    public void managingProviderBtn() {
        showPopUp("Select Professional", managingProviderBtn);
         dataChanged = true;
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

        Collections.sort(professionalList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        dbAdapter.closeMdsDB();
        ListView modeList = new ListView(this);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, professionalList);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        if (professionalList.isEmpty()) {
            startActivity(new Intent(AddDiagnosisActivity.this, AddMedicalProfessionalActivity.class));
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

    @OnClick(R.id.addProviderBtn)
    public void addProviderBtn() {
        startActivity(new Intent(AddDiagnosisActivity.this, AddMedicalProfessionalActivity.class));
        dataChanged = true;
    }

    @OnClick(R.id.DateBtn)
    public void DateBtn() {
       MyApplication.getApplication().hideSoftKeyBoard(AddDiagnosisActivity.this);
        showDialog(DATE_DIALOG_ID);
        dataChanged = true;
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
            DateBtn.setText(formateDate1(dateString));


        }
    };

    private void setData() {
        saveData();
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        if (title.contains("d")) {
            if (diagnosisSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Diagnosis")||TextUtils.isEmpty(DateBtn.getText().toString())) {
                Toast.makeText(AddDiagnosisActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
            } else {
                dbAdapter.openMdsDB();
                DiagnosisWrapper wrapper = new DiagnosisWrapper();

                wrapper.setDate(DateBtn.getText().toString());
                wrapper.setNotes(notesEdt.getText().toString());
                wrapper.setManagingprovider(managingProviderBtn.getText().toString());
                wrapper.setHistorytype(type);

                wrapper.setDiagnosis(diagnosis_str);


                if (id == -1) {


                    dbAdapter.saveDiagnosisData(wrapper);
                    MyApplication.saveLocalData(true);

                } else {
                    dbAdapter.updateDiagnosisData(wrapper, id);
                    MyApplication.saveLocalData(true);
                }


                dbAdapter.closeMdsDB();
//                Intent service_intent = new Intent(AddDiagnosisActivity.this,DataUploadService.class);
//                startService(service_intent);
                new UpdateOnClass(MyApplication.getApplication(),this);
                finish();
            }
        } else {
            if ( TextUtils.isEmpty(diagnosisEdt.getText().toString())||TextUtils.isEmpty(DateBtn.getText().toString())) {
                Toast.makeText(AddDiagnosisActivity.this, "Please fill required fields", Toast.LENGTH_LONG).show();
            } else {
                dbAdapter.openMdsDB();
                DiagnosisWrapper wrapper = new DiagnosisWrapper();

                wrapper.setDate(DateBtn.getText().toString());
                wrapper.setNotes(notesEdt.getText().toString());
                wrapper.setManagingprovider(managingProviderBtn.getText().toString());
                wrapper.setHistorytype(type);


                wrapper.setDiagnosis(diagnosisEdt.getText().toString());


                if (id == -1) {


                    dbAdapter.saveDiagnosisData(wrapper);

                } else {
                    dbAdapter.updateDiagnosisData(wrapper, id);
                }


                dbAdapter.closeMdsDB();
                new UpdateOnClass(MyApplication.getApplication(),this);
                finish();
            }
        }
    }
    private String formateDate1(String dateString) {
        try {

            SimpleDateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = originalFormat.parse(dateString);

//            timeInMillies = date.getTime();
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
}
