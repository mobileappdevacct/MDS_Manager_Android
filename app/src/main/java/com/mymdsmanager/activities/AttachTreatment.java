package com.mymdsmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mymdsmanager.MyApplication.MyApplication;
import com.mymdsmanager.R;
import com.mymdsmanager.datacontrollers.DataManager;
import com.mymdsmanager.task.HomeWatcher;
import com.mymdsmanager.task.OnHomePressedListener;
import com.mymdsmanager.wrapper.TreatmentMedicineInfoWrapper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AttachTreatment extends AppCompatActivity {
    @Bind(R.id.dosageEdt)
    EditText dosageEdt;
    @Bind(R.id.daysEdt)
    EditText daysEdt;
    @Bind(R.id.cyclenumberEdt)
    EditText cyclenumberEdt;
    @Bind(R.id.saveBtn)
    Button saveBtn;
    @Bind(R.id.medicineEdt)
    TextView medicineEdt;
    private Toolbar toolbar;
    TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper;
    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attech_treatment_layout);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        dosageEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dosageEdt.setCursorVisible(true);
            }
        });

        toolbar.setNavigationIcon(R.mipmap.icn_back);
        getSupportActionBar().setTitle("Treatment");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        if (DataManager.getInstance().getMds_treatment_medineArr().size() == 0) {
            ArrayList<String> mdstreaments = new ArrayList<>();
            mdstreaments.add("decitabine intravenous");
            mdstreaments.add("Vidaza injection");
            mdstreaments.add("filgrastim injection");
            mdstreaments.add("azacitidine injection");
            mdstreaments.add("Leukine injection");
            mdstreaments.add("amifostine crystalline intravenous");
            mdstreaments.add("Dacogen intravenous");
            mdstreaments.add("cytarabine injection");
            mdstreaments.add("cytarabine injection");
            mdstreaments.add("Other");
            DataManager.getInstance().setMds_treatment_medineArr(mdstreaments);

        }
        try {
            treatmentMedicineInfoWrapper = (TreatmentMedicineInfoWrapper) getIntent().getExtras().getSerializable("model");
            dosageEdt.setEnabled(false);
            daysEdt.setEnabled(false);
            cyclenumberEdt.setEnabled(false);

            saveBtn.setVisibility(View.GONE);

            medicineEdt.setText(treatmentMedicineInfoWrapper.getNotes());
            dosageEdt.setText(treatmentMedicineInfoWrapper.getDosage());
            daysEdt.setText(treatmentMedicineInfoWrapper.getDays());
            cyclenumberEdt.setText(treatmentMedicineInfoWrapper.getCyclenumber());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        medicineEdt.addTextChangedListener(textWatcher);
        dosageEdt.addTextChangedListener(textWatcher);
        daysEdt.addTextChangedListener(textWatcher);
        cyclenumberEdt.addTextChangedListener(textWatcher);

        mHomeWatcher = new HomeWatcher(AttachTreatment.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityFound) {
            new UpdateDataDialog(AttachTreatment.this, R.style.Dialog);
            isActivityFound = false;
        }
    }

    @Override
    public void onBackPressed() {

        if (dataChanged&&(!TextUtils.isEmpty(medicineEdt.getText().toString()))) {
            saveAlert();
        } else {

            finish();
        }
    }

    private void saveAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AttachTreatment.this);
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

    @OnClick(R.id.medicineEdt)
    public void medicineEdt() {
        showPopUp("Medicines", DataManager.getInstance().getMds_treatment_medineArr());
    }

    @OnClick(R.id.saveBtn)
    public void saveBtn() {
        if (TextUtils.isEmpty(medicineEdt.getText().toString())) {
            Toast.makeText(AttachTreatment.this, "Please fill required fields", Toast.LENGTH_LONG).show();
        } else {
            TreatmentMedicineInfoWrapper treatmentMedicineInfoWrapper = new TreatmentMedicineInfoWrapper();
            treatmentMedicineInfoWrapper.setCyclenumber(cyclenumberEdt.getText().toString());
            treatmentMedicineInfoWrapper.setDays(daysEdt.getText().toString());
            treatmentMedicineInfoWrapper.setDosage(dosageEdt.getText().toString());
            treatmentMedicineInfoWrapper.setNotes(medicineEdt.getText().toString());
            treatmentMedicineInfoWrapper.setMedicinename(medicineEdt.getText().toString());
            treatmentMedicineInfoWrapper.setOthertreatmentname("MDS Treatment");
            treatmentMedicineInfoWrapper.setType("T");
            DataManager.getInstance().treatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
            DataManager.getInstance().updatetreatmentMedicineInfoWrappers.add(treatmentMedicineInfoWrapper);
            MyApplication.getApplication().hideSoftKeyBoard(AttachTreatment.this);
            finish();
        }
    }

    private void showAddOthersDialog() {


        final Dialog dialog = new Dialog(AttachTreatment.this, R.style.AppCompatTheme);
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up_add_others);
// Check if no view has focus:

        final EditText enterValueEdt = (EditText) dialog.findViewById(R.id.enterValueEdt);


        enterValueEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (TextUtils.isEmpty(enterValueEdt.getText().toString())) {
                            Toast.makeText(AttachTreatment.this, "Please fill required field", Toast.LENGTH_LONG).show();
                        } else {


                            DataManager.getInstance().getMds_treatment_medineArr().add(DataManager.getInstance().getMds_treatment_medineArr().size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");


                            dialog.dismiss();

                            hideKeyboard();
                        }
                }
                return true;

            }
        });
        Button addBtn, closeBtn;
        addBtn = (Button) dialog.findViewById(R.id.addBtn);
        closeBtn = (Button) dialog.findViewById(R.id.closeBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(enterValueEdt.getText().toString())) {
                    Toast.makeText(AttachTreatment.this, "Please fill required field", Toast.LENGTH_LONG).show();
                } else {


                    DataManager.getInstance().getMds_treatment_medineArr().add(DataManager.getInstance().getMds_treatment_medineArr().size() - 1, enterValueEdt.getText().toString());
//                            typeArr.add("Others");

                    medicineEdt.setText(enterValueEdt.getText().toString());


                    dialog.dismiss();


                    hideKeyboard();
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
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


                medicineEdt.setText(arr.get(position));
                if (arr.get(position).equalsIgnoreCase("Other")) {
                    showAddOthersDialog();
                }


            }
        });


    }

    private void hideKeyboard() {
        // Check if no view has focus:

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
