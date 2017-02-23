package com.mymdsmanager.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.database.DBAdapter;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.ClinicalWrapper;
import com.mymdsmanager.wrapper.TreatmentMedicineInfoWrapper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddClinicalTrail extends AppCompatActivity implements OnFinishActivity{
    @Bind(R.id.trialnuberEdt)
    EditText trialnuberEdt;
    @Bind(R.id.nameofTrialEdt)
    EditText nameofTrialEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    @Bind(R.id.locationEdt)
    EditText locationEdt;
    private Toolbar toolbar;
    DBAdapter dbAdapter;
    TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper;
    HomeWatcher mHomeWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_clinical_layout);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        dbAdapter = new DBAdapter(AddClinicalTrail.this);
        getSupportActionBar().setTitle("Add Clinical Trial");

        toolbar.setNavigationIcon(R.mipmap.icn_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try {
            treatmentMedicineInfoWrapper = (TreatmentMedicineInfoWrapper) getIntent().getExtras().getSerializable("model");
            trialnuberEdt.setEnabled(false);
            nameofTrialEdt.setEnabled(false);
            locationEdt.setEnabled(false);
            saveBtn.setVisibility(View.GONE);

            trialnuberEdt.setText(treatmentMedicineInfoWrapper.getDays());
            nameofTrialEdt.setText(treatmentMedicineInfoWrapper.getCyclenumber());
            locationEdt.setText(treatmentMedicineInfoWrapper.getMedicinename());

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

         mHomeWatcher = new HomeWatcher(AddClinicalTrail.this);
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


        trialnuberEdt.addTextChangedListener(textWatcher);
        nameofTrialEdt.addTextChangedListener(textWatcher);
        locationEdt.addTextChangedListener(textWatcher);

    }
    boolean isActivityFound = false;
    @Override
    protected void onResume() {
        super.onResume();
        mHomeWatcher.startWatch();
        if (isActivityFound) {
            new UpdateDataDialog(AddClinicalTrail.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
      dbAdapter.openMdsDB();
      if (TextUtils.isEmpty(nameofTrialEdt.getText().toString()))
        {
            nameofTrialEdt.setError("Please enter name of trial");
            nameofTrialEdt.requestFocus();

        }else {
            TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper = new TreatmentMedicineInfoWrapper();
            treatmentMedicineInfoWrapper.setCyclenumber(nameofTrialEdt.getText().toString());
            treatmentMedicineInfoWrapper.setDays(trialnuberEdt.getText().toString());
            treatmentMedicineInfoWrapper.setDosage("Clinical Trial");
            treatmentMedicineInfoWrapper.setMedicinename(locationEdt.getText().toString());
            treatmentMedicineInfoWrapper.setType("T");
            DataManager.getInstance().treatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
          DataManager.getInstance().updatetreatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
//            finish();
            ClinicalWrapper clinicalWrapper = new ClinicalWrapper();
            clinicalWrapper.setName_ofTrial(nameofTrialEdt.getText().toString());
            clinicalWrapper.setTrialNumber(trialnuberEdt.getText().toString());
            clinicalWrapper.setLocation(locationEdt.getText().toString());
            clinicalWrapper.setTreatementid("");
            MyApplication.getApplication().hideSoftKeyBoard(AddClinicalTrail.this);
            dbAdapter.saveClinicalData(clinicalWrapper);
            dbAdapter.closeMdsDB();
//            Intent service_intent = new Intent(AddClinicalTrail.this,DataUploadService.class);
//            startService(service_intent);
            new UpdateOnClass(MyApplication.getApplication(),this);
            mHomeWatcher.stopWatch();
            finish();
        }

    }

    @Override
    public void onBackPressed() {


           if (dataChanged) {
               saveAlert();
           }else
           {
               try {
                   mHomeWatcher.stopWatch();
               }catch (Exception ex)
               {
                   ex.printStackTrace();
               }
               finish();
           }

        }



    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddClinicalTrail.this);
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

    @Override
    public void onFinish() {
//        mHomeWatcher.stopWatch();
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
